package com.akai.reader

import android.app.*
import android.content.*
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.*
import android.view.WindowManager
import java.io.File
import java.io.FileOutputStream

class ScreenCaptureService : Service() {
    private var projection: MediaProjection? = null
    private var display: VirtualDisplay? = null
    private var reader: ImageReader? = null
    private var thread: HandlerThread? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val channel = "garuda_capture"
        if (Build.VERSION.SDK_INT >= 26) {
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(
                    NotificationChannel(channel, "Screen capture", NotificationManager.IMPORTANCE_LOW)
                )
        }
        val notification = if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(this, channel).setSmallIcon(android.R.drawable.ic_menu_camera)
                .setContentTitle("Garuda Gems Analyzer")
                .setContentText("Capturing one screen image")
                .build()
        else Notification.Builder(this).setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentTitle("Garuda Gems Analyzer").build()

        if (Build.VERSION.SDK_INT >= 29)
            startForeground(41, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION)
        else startForeground(41, notification)

        val code = intent?.getIntExtra("result_code", 0) ?: 0
        val data: Intent? = if (Build.VERSION.SDK_INT >= 33)
            intent?.getParcelableExtra("result_data", Intent::class.java)
        else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra("result_data")
        }
        if (data == null || code != Activity.RESULT_OK) {
            stopSelf()
            return START_NOT_STICKY
        }

        try {
            val manager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
            projection = manager.getMediaProjection(code, data)
            val metrics = android.util.DisplayMetrics()
            @Suppress("DEPRECATION")
            (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealMetrics(metrics)
            val w = metrics.widthPixels
            val h = metrics.heightPixels
            thread = HandlerThread("capture").also { it.start() }
            val handler = Handler(thread!!.looper)
            reader = ImageReader.newInstance(w, h, PixelFormat.RGBA_8888, 2)
            reader!!.setOnImageAvailableListener({ r ->
                val image = r.acquireLatestImage() ?: return@setOnImageAvailableListener
                try {
                    val plane = image.planes[0]
                    val stride = plane.rowStride / plane.pixelStride
                    val padded = Bitmap.createBitmap(stride, h, Bitmap.Config.ARGB_8888)
                    padded.copyPixelsFromBuffer(plane.buffer)
                    val bitmap = Bitmap.createBitmap(padded, 0, 0, w, h)
                    padded.recycle()
                    val file = File(filesDir, "latest-screen.png")
                    FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                    bitmap.recycle()
                    sendBroadcast(Intent("com.akai.reader.CAPTURE_DONE").setPackage(packageName)
                        .putExtra("path", file.absolutePath))
                } catch (e: Exception) {
                    sendBroadcast(Intent("com.akai.reader.CAPTURE_ERROR").setPackage(packageName)
                        .putExtra("error", e.message ?: "Capture failed"))
                } finally {
                    image.close()
                    stopCapture()
                }
            }, handler)
            projection!!.registerCallback(object : MediaProjection.Callback() {
                override fun onStop() { stopCapture() }
            }, handler)
            display = projection!!.createVirtualDisplay(
                "GarudaCapture", w, h, metrics.densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, reader!!.surface, null, handler
            )
        } catch (_: Exception) {
            stopCapture()
        }
        return START_NOT_STICKY
    }

    private fun stopCapture() {
        try { display?.release() } catch (_: Exception) {}
        display = null
        try { reader?.close() } catch (_: Exception) {}
        reader = null
        try { projection?.stop() } catch (_: Exception) {}
        projection = null
        thread?.quitSafely()
        thread = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
}

package com.akai.reader

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var message: TextView
    private val captureRequest = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.rgb(18, 18, 24))
        }

        val title = TextView(this).apply {
            text = "Garuda Gems Analyzer"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        message = TextView(this).apply {
            text = "Tap below and allow screen capture."
            textSize = 16f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 32)
        }

        val button = Button(this).apply {
            text = "Capture Screen"
            setOnClickListener { requestCapture() }
        }

        layout.addView(title)
        layout.addView(message)
        layout.addView(button)
        setContentView(layout)
    }

    private fun requestCapture() {
        val manager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityFor

startForegroundService(serviceIntent)
                message.text = "Capture requested. Checking result..."
            } catch (e: Exception) {
                message.text = "Could not start capture: ${e.message}"
            }
        } else {
            message.text = "Screen capture permission was cancelled."
        }
    }
}

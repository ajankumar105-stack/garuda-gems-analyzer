package com.akai.reader

import android.app.Activity
import android.content.Context
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
            setPadding(24, 24, 24, 24)
            setBackgroundColor(Color.rgb(18, 18, 28))
        }

        val title = TextView(this).apply {
            text = "Garuda Gems Analyzer"
            textSize = 26f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        message = TextView(this).apply {
            text = "\nScreen reader setup\n\nTap below to request screen-capture permission."
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        val button = Button(this).apply {
            text = "Allow Screen Capture"
            setOnClickListener {
                val manager = getSystemService(
                    Context.MEDIA_PROJECTION_SERVICE
                ) as MediaProjectionManager
                startActivityForResult(
                    manager.createScreenCaptureIntent(),
                    captureRequest
                )
            }
        }

        layout.addView(title)
        layout.addView(message)
        layout.addView(button)
        setContentView(layout)
    }

    @Deprecated("Deprecated by Android, retained for compatibility")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureRequest) {
            message.text = if (resultCode == RESULT_OK) {
                "Permission granted for this request.\n\nActual screen reading is not added yet."
            } else {
                "Screen capture permission was not granted."
            }
        }
    }
}

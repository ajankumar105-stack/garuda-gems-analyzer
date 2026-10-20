package com.akai.reader

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
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

        val message = TextView(this).apply {
            text = "\nScreen reader setup\n\nNext: add Android screen-capture permission."
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        val button = Button(this).apply {
            text = "Screen Reader Setup"
            setOnClickListener {
                message.text = "Screen capture will be added next."
            }
        }

        layout.addView(title)
        layout.addView(message)
        layout.addView(button)
        setContentView(layout)
    }
}

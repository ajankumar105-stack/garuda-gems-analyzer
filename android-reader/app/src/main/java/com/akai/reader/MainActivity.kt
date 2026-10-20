
package com.akai.reader

import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar

class MainActivity : Activity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)

val layout = FrameLayout(this)
layout.setBackgroundColor(Color.BLACK)

val webView = WebView(this)
webView.layoutParams = FrameLayout.LayoutParams(
ViewGroup.LayoutParams.MATCH_PARENT,
ViewGroup.LayoutParams.MATCH_PARENT
)

webView.settings.javaScriptEnabled = true
webView.settings.javaScri
webView.webViewClient = WebViewClient()

                                                                                    layout.addView(webView)
                                                                                            setContentView(layout)

                                                                                                    webView.loadUrl(
                                                                                                                    "https://ajankumar105-stack.github.io/garuda-gems-analyzer/"
                                                                                                    )
        }
}

                                                    )
        }
}
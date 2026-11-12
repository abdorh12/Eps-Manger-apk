package com.abdorrh.peteacher

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var offlineView: LinearLayout
    private val startUrl = "https://rhpeman-z8yc.whacka.app/#/calendar?tab=schedule"
    private val allowedHost = "rhpeman-z8yc.whacka.app"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

        webView = WebView(this).apply {
            setBackgroundColor(Color.WHITE)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            settings.userAgentString = settings.userAgentString + " EPSMangerAndroid"
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, false)
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val uri = request.url
                    return uri.scheme != "https" || (uri.host != allowedHost && uri.host != "www.$allowedHost")
                }
                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: android.webkit.WebResourceError) {
                    if (request.isForMainFrame) showOffline()
                }
                override fun onPageFinished(view: WebView, url: String) {
                    offlineView.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                }
            }
        }
        offlineView = createOfflineView()
        val root = android.widget.FrameLayout(this)
        root.addView(webView, android.widget.FrameLayout.LayoutParams(-1, -1))
        root.addView(offlineView, android.widget.FrameLayout.LayoutParams(-1, -1))
        setContentView(root)
        if (savedInstanceState != null) webView.restoreState(savedInstanceState)
        else loadSite()
    }

    private fun createOfflineView() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        setPadding(32, 32, 32, 32)
        setBackgroundColor(Color.rgb(247, 249, 248))
        addView(TextView(this@MainActivity).apply {
            text = "لا يوجد اتصال بالإنترنت\nتحقق من الاتصال ثم حاول مرة أخرى."
            textSize = 20f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(30, 45, 39))
        })
        addView(Button(this@MainActivity).apply {
            text = "إعادة المحاولة"
            setOnClickListener { loadSite() }
        })
    }

    private fun isOnline(): Boolean {
        val manager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun loadSite() {
        if (!isOnline()) { showOffline(); return }
        offlineView.visibility = View.GONE
        webView.visibility = View.VISIBLE
        webView.loadUrl(startUrl)
    }

    private fun showOffline() {
        webView.visibility = View.GONE
        offlineView.visibility = View.VISIBLE
    }

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}

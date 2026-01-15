package com.example.appcuaban

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var etUrl: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        etUrl = findViewById(R.id.etUrl)
        progressBar = findViewById(R.id.progressBar)
        
        val btnClear = findViewById<ImageButton>(R.id.btnClear)

        setupWebView()
        loadUrl("https://www.google.com")

        // Nút GO
        findViewById<Button>(R.id.btnGo).setOnClickListener { loadUrl(etUrl.text.toString()) }
        
        // Nút Home
        findViewById<ImageButton>(R.id.btnHome).setOnClickListener { loadUrl("https://www.google.com") }

        // Nút Xóa nhanh
        btnClear.setOnClickListener { etUrl.setText("") }

        // Nút điều hướng
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { if (webView.canGoBack()) webView.goBack() }
        findViewById<ImageButton>(R.id.btnForward).setOnClickListener { if (webView.canGoForward()) webView.goForward() }
        findViewById<ImageButton>(R.id.btnReload).setOnClickListener { webView.reload() }

        // Nhấn Enter để truy cập
        etUrl.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                loadUrl(etUrl.text.toString())
                true
            } else false
        }
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                etUrl.setText(url)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
            }
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                Toast.makeText(this@MainActivity, "Lỗi kết nối!", Toast.LENGTH_SHORT).show()
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }
    }

    private fun loadUrl(input: String) {
        var url = input.trim()
        if (url.isEmpty()) return
        if (!url.startsWith("http")) {
            url = if (url.contains(".")) "https://$url" else "https://www.google.com/search?q=$url"
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
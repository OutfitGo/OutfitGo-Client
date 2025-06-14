package com.outfitgo.store.presentation.checkout

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CheckoutScreen(checkoutUrl:String,onOrderConfirm:()->Unit,modifier: Modifier = Modifier) {
    AndroidView(factory = { context ->
        WebView(context).apply {

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.userAgentString = settings.userAgentString + " MobileApp"

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    Log.d("``t``", "shouldOverrideUrlLoading: $url")
                    if (url.contains("thank_you", ignoreCase = true)) {
                        onOrderConfirm()
                        return true
                    }
                    return false
                }
            }

            clearCache(true)
            clearHistory()
            CookieManager.getInstance().removeSessionCookies(null)
            CookieManager.getInstance().flush()
            loadUrl(checkoutUrl)
        }
    }, modifier = Modifier.fillMaxSize())
}
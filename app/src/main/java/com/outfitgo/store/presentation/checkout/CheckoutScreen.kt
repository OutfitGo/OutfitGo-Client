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

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("``TAG``", "onPageFinished: $url")
                    if (url?.contains("thank-you", ignoreCase = true) == true) {
                        Log.d("``TAG``", "onPageFinished: true")
                        onOrderConfirm()
                    }else{
                        Log.d("``TAG``", "onPageFinished: false")
                    }
                }

            }

            loadUrl(checkoutUrl)
        }
    }, modifier = Modifier.fillMaxSize())
}
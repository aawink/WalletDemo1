package com.example.walletdemo.web

import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.example.walletdemo.utils.MenuEventBus
import java.lang.ref.WeakReference

class SuperAppJSInterface(webView:WebView) {
    inner class MenuItemRecord(val label: String, val iconUrl: String, val callback: String)

    val mWebView:WeakReference<WebView>
    private val actions:HashMap<Int, MenuItemRecord> = HashMap<Int, MenuItemRecord>()
    private val labelToId:HashMap<String,Int> = HashMap<String, Int>()

    init {
        mWebView = WeakReference(webView)
    }

    @JavascriptInterface
    fun addActionItem(label: String, iconUrl:String, callback:String) {
        val id:Int = MenuEventBus.addMenuItemFrom(mWebView.get(), label, iconUrl, this )
        actions[id] = MenuItemRecord(label, iconUrl, callback)
        labelToId[label] = id
        // dispatch Event Menu Change Request
    }

    @JavascriptInterface
    fun removeAction(label:String) {
        labelToId.get(label)?.let {
            MenuEventBus.removeMenuItem(it)
            actions.remove(labelToId.get(label))
            labelToId.remove(label)
        }
    }

    @JavascriptInterface
    fun createInvoice(jsonObject:String) {
    }

    @JavascriptInterface
    fun requestUserInfo(jsonObject:String) {
    }

    @JavascriptInterface
    fun launchService(jsonObject:String) { // Video Player, Messenger, Email?
    }
}
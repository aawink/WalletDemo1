package com.example.walletdemo.utils

import android.R.drawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.example.walletdemo.web.SuperAppJSInterface
import java.net.URL
import java.util.*


object MenuEventBus {
    lateinit var menu:Menu
    lateinit var activeWebView:WebView

    public fun addMenuItemFrom(webView: WebView?, label:String, iconURL:String, listener:SuperAppJSInterface) : Int {
        if (webView == this.activeWebView) {
            val id: Int = UUID.randomUUID().hashCode()

            try {
                var icon = Drawable.createFromStream(URL(iconURL).openStream(), null)
                val item: MenuItem = this.menu.add(0, id, 0, label)

                item.setIcon(icon)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            //item.setOnMenuItemClickListener {  }
            return id
        }
        return -1
    }

    public fun addMenuItemFromThirdParty(webView: WebView?, label:String, iconURL:String, listener:SuperAppJSInterface) : Int {


        return 0
    }

    public fun launchVideoCom() {

    }

    public fun removeMenuItem(id:Int) {
        this.menu.removeItem(id)
    }

}
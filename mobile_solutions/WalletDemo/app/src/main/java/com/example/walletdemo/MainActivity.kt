package com.example.walletdemo

import android.os.Bundle
import android.view.Menu
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.walletdemo.databinding.ActivityMainBinding
import com.example.walletdemo.ui.IWebViewContainer
import com.example.walletdemo.ui.SmartBar
import com.example.walletdemo.utils.AndroidUtils
import com.example.walletdemo.utils.MenuEventBus
import com.example.walletdemo.web.SuperAppJSInterface
import com.google.android.material.navigation.NavigationView

// installWidget
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val webViewContainers: HashSet<IWebViewContainer> = HashSet<IWebViewContainer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        MenuEventBus.menu = navView.menu
        MenuEventBus.activeWebView = findViewById<WebView>(R.id.webView)

        binding.appBarMain.toolbar.addEventListener(fun(url:CharSequence) {
            val webView:WebView = MenuEventBus.activeWebView
            try {
                webView.webViewClient = WebViewClient()
                webView.settings.javaScriptEnabled = true;
                webView.addJavascriptInterface(SuperAppJSInterface(webView), "__SuperApp__")
                webView.postDelayed(
                    Runnable { webView.loadUrl(url.toString()) },
                    100
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        webViewContainers.addAll(
            AndroidUtils.getChildViewsByClass(drawerLayout, IWebViewContainer::class)
            )

    }

   override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
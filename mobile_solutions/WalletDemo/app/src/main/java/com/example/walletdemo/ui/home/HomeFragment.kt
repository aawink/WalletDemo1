package com.example.walletdemo.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.walletdemo.databinding.FragmentHomeBinding
import com.example.walletdemo.ui.IWebViewContainer

class HomeFragment : Fragment(), IWebViewContainer {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val webView = binding.webView
        webView.loadUrl("http://10.0.2.2:8001/noprox/testAction.html")

        return root
    }

    override fun getWebView(): WebView {
        return binding.webView
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.walletdemo.ui

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.EditText
import androidx.appcompat.R
import java.util.*

class SmartBar(context:Context, attrs: AttributeSet?, defStyleAttr:Int) :
    androidx.appcompat.widget.Toolbar (context, attrs, defStyleAttr) {
    constructor(context:Context) : this(context, null)
    constructor(context:Context, attrs:AttributeSet?) : this (context, attrs, R.attr.toolbarStyle)
    private val mEditText: EditText = EditText(this.context)
    private var mUrl: CharSequence = ""
    private val mListeners: ArrayList<(CharSequence)->Unit> = ArrayList<(CharSequence)->Unit>()

    override fun setTitle (seq: CharSequence) {
        setUrl(seq)
        return
    }

    fun addEventListener (listener: (url : CharSequence) -> Unit) {
        mListeners.add(listener)
    }

    fun removeEventListener(listener: (url : CharSequence) -> Unit) {
        mListeners.remove(listener)
    }

    fun setUrl(value: CharSequence) {
        this.mUrl = value

        // addToHistoryManager

        mEditText.setText(value)

        if (mEditText.getParent() != this) {
            mEditText.setSingleLine(true)
            mEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            //mEditText.setImeActionLabel(getResources().getString(R.string.done), EditorInfo.IME_ACTION_DONE);
            mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

            mEditText.setOnEditorActionListener { v, actionId, event ->
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    // dispatchEve
                    for (listener in mListeners) {
                        listener.invoke(v.text)
                    }
                    this.mUrl = v.text
                    true
                } else {
                    false
                }
            }

            addView(mEditText, generateDefaultLayoutParams())
        }
        // dispatchEvent url_changed
    }

/*    override fun onMeasure(widthMeasureSpec:Int, heightMeasureSpec:Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (shouldLayout(mEditText)) {

        }
    }

    override fun onLayout(changed:Boolean, l:Int, t:Int, r:Int, b:Int) {
        super.onLayout(changed, l, t, r, b)

        if (shouldLayout(mEditText)) {

        }
    }
    private fun shouldLayout(view: View?): Boolean {
        return view != null && view.parent === this && view.visibility != GONE
    }*/
}
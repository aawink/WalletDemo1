package com.example.walletdemo.utils

import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass

object AndroidUtils {
    fun <T : Any> getChildViewsByClass(parent: ViewGroup, clazz: KClass<T>): ArrayList<T> {
        val children = ArrayList<T>()

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            if (clazz.isInstance(child)) {
                @Suppress("UNCHECKED_CAST")
                children.add(child as T)
            } else if (child is ViewGroup) {
                children.addAll(getChildViewsByClass(child, clazz))
            }
        }

        return children
    }
}
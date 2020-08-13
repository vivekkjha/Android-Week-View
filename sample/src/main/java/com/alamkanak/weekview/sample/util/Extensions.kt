package com.alamkanak.weekview.sample.util

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <reified T : View> Fragment.lazyView(
    @IdRes viewId: Int
): Lazy<T> = lazy { requireActivity().findViewById<T>(viewId) }

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observe: (T) -> Unit) {
    observe(owner, Observer { observe(it) })
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

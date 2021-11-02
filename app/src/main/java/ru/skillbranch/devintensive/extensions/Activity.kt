package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.internal.ViewUtils.dpToPx

fun Activity.hideKeyboard() {

    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    inputManager.hideSoftInputFromWindow(
        currentFocus?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun Activity.isKeyboardOpen(): Boolean {
    val contentView: View = window.decorView
    val screenHeight: Int = contentView.rootView.height
    val rect = Rect()
    contentView.getWindowVisibleDisplayFrame(rect)
    // If activity height is >15% less than screen height, probably keyboard is opened
    return screenHeight - rect.bottom > screenHeight * 0.15
}

fun Activity.isKeyboardClosed() = !this.isKeyboardOpen()
package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    val focus = this.currentFocus
    focus?.let {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let {
            it.hideSoftInputFromWindow(focus.windowToken, 0)
        }
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    val rect = Rect()
    val rootView = findViewById<View>(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(rect)
    val heightRoot = rootView.height
    val heightRect = rect.height()
    return (heightRoot - heightRect > 128)
}

fun Activity.isKeyboardClosed(): Boolean {
    return this.isKeyboardOpen().not()
}

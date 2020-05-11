package ru.skillbranch.devintensive.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

fun Int.dpToPx() =  (this * Resources.getSystem().displayMetrics.density + .5f).toInt()
fun Int.pxToDp() =  (this / Resources.getSystem().displayMetrics.density + .5f).toInt()

fun Int.spToPx() : Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this*1f, Resources.getSystem().displayMetrics)

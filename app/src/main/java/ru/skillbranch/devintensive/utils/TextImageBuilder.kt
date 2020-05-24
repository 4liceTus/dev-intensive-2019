package ru.skillbranch.devintensive.utils

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.annotation.Dimension

class TextImageBuilder(val width: Int, val height: Int) {
    private var text: String = ""
    private var textSize: Int = 0
    private var textColor: Int = Color.WHITE
    private var backgroundColor: Int = Color.BLACK
    private var bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    fun setText(text: String) = apply { this.text = text }
    fun setTextSize(@Dimension size: Int) = apply { this.textSize = size }
    fun setTextColor(@ColorInt color: Int) = apply { this.textColor = color }
    fun setBackgroundColor(@ColorInt color: Int) = apply { this.backgroundColor = color }

    fun build():Bitmap {
        val canvas = Canvas(bitmap)
        canvas.drawColor(backgroundColor)

        if(text.isNotEmpty()) {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = textColor
            paint.textSize = textSize.toFloat()

            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            val x = (bitmap.width - bounds.width()) / 2F
            val y = (bitmap.height + bounds.height()) / 2F

            canvas.drawText(text, x, y, paint)
        }
        return bitmap
    }
}
package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R
import kotlin.math.min


class CircleImageView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH: Float = 2F
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null) // прозрачность
    }

    @Dimension
    fun getBorderWidth():Int = borderWidth.toInt()

    fun setBorderWidth(@Dimension dp:Int) {
        borderWidth = dp.toFloat()
        invalidate()
    }

    fun getBorderColor():Int = borderColor

    fun setBorderColor(hex:String) {
        borderColor = Color.parseColor(hex)
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        if(height == 0 || width == 0) return
        val bitmap = getBitmapFromDrawable(width, height) ?: return

        val centerHeight = height / 2F
        val centerWidth = width / 2F
        val radius = min(centerHeight, centerWidth)

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.drawCircle(centerWidth, centerHeight, radius, circlePaint)

        circlePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0F, 0F, circlePaint)

        if (borderWidth > 0) {
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            borderPaint.color = borderColor
            borderPaint.strokeWidth = borderWidth
            borderPaint.style = Paint.Style.STROKE
            canvas.drawCircle(centerWidth, centerHeight, radius - borderWidth / 2F, borderPaint)
        }
    }

    private fun getBitmapFromDrawable(width: Int, height: Int): Bitmap? {
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return (drawable as BitmapDrawable).bitmap
        }
        val bitmap = drawable.toBitmap(height, width, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}

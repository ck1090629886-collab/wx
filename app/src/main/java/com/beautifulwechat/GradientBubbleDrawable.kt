package com.beautifulwechat

import android.graphics.*
import android.graphics.drawable.Drawable

class GradientBubbleDrawable(
    private val startColor: Int,
    private val endColor: Int,
    private val cornerRadius: Float,
    private val alpha: Int = 217, // 85% 不透明度 (255 * 0.85)
    private val isSender: Boolean = true
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        // 轻微阴影效果
        setShadowLayer(8f, 0f, 2f, Color.argb(40, 0, 0, 0))
    }
    
    private var gradient: LinearGradient? = null
    private val rect = RectF()
    private val path = Path()

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        rect.set(bounds)
        
        // 初始化渐变（对角渐变：左上到右下）
        if (gradient == null || rect.width() > 0 && rect.height() > 0) {
            gradient = LinearGradient(
                rect.left, rect.top,
                rect.right, rect.bottom,
                startColor, endColor,
                Shader.TileMode.CLAMP
            )
            paint.shader = gradient
            paint.alpha = alpha
        }
        
        // 绘制圆角矩形路径
        path.reset()
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        
        // 绘制阴影（先绘制阴影层）
        canvas.drawPath(path, shadowPaint)
        
        // 绘制渐变气泡
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat")
    )
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int = -1
    override fun getIntrinsicHeight(): Int = -1
}

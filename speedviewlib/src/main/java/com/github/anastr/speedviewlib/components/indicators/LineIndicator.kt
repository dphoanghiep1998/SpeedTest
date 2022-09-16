package com.github.anastr.speedviewlib.components.indicators

import android.content.Context
import android.graphics.*
import android.opengl.ETC1.getHeight


/**
 * this Library build By Anas Altair
 * see it on [GitHub](https://github.com/anastr/SpeedView)
 */
class LineIndicator(context: Context, private val length: Float) :
    Indicator<LineIndicator>(context) {

    private val indicatorPath = Path()

    init {
        require(length in 0f..1f) { "Length must be between [0,1]." }
        width = dpTOpx(8f)
    }

    override fun getBottom(): Float {
        return getCenterY() * length
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(indicatorPath, indicatorPaint)
    }

    override fun updateIndicator() {
        indicatorPath.reset()
        indicatorPath.moveTo(getCenterX() - 5f, getCenterY() - 5f)
        indicatorPath.lineTo(getCenterX(), getCenterY() * length)

        indicatorPaint.style = Paint.Style.STROKE
        indicatorPaint.strokeWidth = width
        indicatorPaint.shader = LinearGradient(
            getCenterX(),
            getCenterY(),
            getCenterX(),
            getCenterY() * length,
            "#F7F7FC".toInt(),
            "#F7F7FC".toInt(),
            Shader.TileMode.MIRROR
        )

    }

    override fun setWithEffects(withEffects: Boolean) {
        if (withEffects && !speedometer!!.isInEditMode) {
            indicatorPaint.maskFilter = BlurMaskFilter(15f, BlurMaskFilter.Blur.SOLID)
        } else {
            indicatorPaint.maskFilter = null
        }
    }
}

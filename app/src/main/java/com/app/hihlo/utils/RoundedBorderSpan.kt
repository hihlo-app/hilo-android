package com.app.hihlo.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

class RoundedBorderSpan(
    private val strokeColor: Int,
    private val textColor: Int,
    private val radius: Float,
    private val padding: Float
) : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return (paint.measureText(text, start, end) + 2 * padding).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val textStr = text.subSequence(start, end).toString()

        val width = paint.measureText(textStr)
        val rect = RectF(
            x,
            y + paint.ascent(),
            x + width + 2 * padding,
            y + paint.descent()
        )

        val bgPaint = Paint(paint)
        bgPaint.style = Paint.Style.STROKE
        bgPaint.strokeWidth = 3f
        bgPaint.color = strokeColor
        bgPaint.isAntiAlias = true

        // Draw border rounded rect
        canvas.drawRoundRect(rect, radius, radius, bgPaint)

        // Draw text
        paint.color = textColor
        canvas.drawText(textStr, x + padding, y.toFloat(), paint)
    }
}
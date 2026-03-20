package com.app.hihlo.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
import android.text.style.ClickableSpan
import android.view.View

class PillClickableSpan(
    private val context: Context,               // ← pass context to get density
    private val textColor: Int,
    private val bgColor: Int = Color.parseColor("#1AFFFFFF"), // semi-transparent
    private val borderColor: Int = Color.parseColor("#B90A66"),
    private val borderWidthDp: Float = 1f,
    private val cornerRadiusDp: Float = 4f,
    private val horizontalPaddingDp: Int = 5,
    private val verticalPaddingDp: Int = 4,
    private val onClick: (View) -> Unit         // lambda for click
) : ReplacementSpan() {

    private val rect = RectF()
    private val paint = Paint().apply {
        isAntiAlias = true
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: android.graphics.Paint.FontMetricsInt?
    ): Int {
        // Measure the text width + padding
        val textWidth = paint.measureText(text, start, end)
        val density = context.resources.displayMetrics.density
        val hPad = horizontalPaddingDp * density
        return (textWidth + hPad * 2 + 2).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val originalColor = paint.color

        val density = context.resources.displayMetrics.density
        val hPad = horizontalPaddingDp * density
        val vPad = verticalPaddingDp * density
        val borderWidth = borderWidthDp * density
        val cornerRadius = cornerRadiusDp * density

        // Background
        this.paint.color = bgColor
        this.paint.style = Paint.Style.FILL
        rect.set(
            x - hPad,
            top - vPad,
            x + paint.measureText(text, start, end) + hPad,
            bottom + vPad
        )
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, this.paint)

        // Border
        this.paint.color = borderColor
        this.paint.style = Paint.Style.STROKE
        this.paint.strokeWidth = borderWidth
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, this.paint)

        // Draw the actual text
        paint.color = textColor
        canvas.drawText(text!!, start, end, x, y.toFloat(), paint)

        paint.color = originalColor
    }
}
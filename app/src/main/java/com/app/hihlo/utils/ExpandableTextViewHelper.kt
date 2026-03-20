package com.app.hihlo.utils

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.app.hihlo.R

class ExpandableTextViewHelper(
    private val textView: TextView,
    private val maxCollapsedLines: Int = 1,
    private val readMoreText: String = " ... More",
    private val readLessText: String = " Less"
) {

    private var originalText: String = ""
    private var isExpanded = false
    private var listenerAdded = false

    fun setText(text: String?, isExpanded: Boolean = false) {
        this.isExpanded = isExpanded
        if (text.isNullOrBlank()) {
            textView.text = ""
            textView.maxLines = Int.MAX_VALUE
            textView.movementMethod = null
            return
        }

        originalText = text.trim()

        if (isExpanded) {
            showExpanded()
        } else {
            prepareAndCollapse()
        }
    }

    private fun prepareAndCollapse() {
        textView.text = originalText
        textView.maxLines = Int.MAX_VALUE
        textView.ellipsize = null

        // Remove old listener if any
        if (listenerAdded) {
            textView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
            listenerAdded = false
        }

        if (textView.viewTreeObserver.isAlive) {
            textView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
            listenerAdded = true
        } else {
            Log.e("ExpandHelper", "ViewTreeObserver not alive → fallback to post")
            textView.post { collapseIfNeeded() }
        }
    }

    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (!textView.viewTreeObserver.isAlive) return

            textView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            listenerAdded = false

            collapseIfNeeded()
        }
    }

    private fun collapseIfNeeded() {
        val lineCount = textView.lineCount
        Log.e("ExpandHelper", "collapseIfNeeded → lineCount = $lineCount, width = ${textView.width}")

        if (lineCount <= maxCollapsedLines || lineCount == 0) {
            textView.movementMethod = null
            return
        }

        textView.maxLines = maxCollapsedLines
        textView.ellipsize = TextUtils.TruncateAt.END

        // Force measure again
        textView.requestLayout()

        textView.post {
            if (textView.layout == null) {
                Log.e("ExpandHelper", "layout null after requestLayout")
                return@post
            }

            val endLine = maxCollapsedLines - 1
            var endIndex = textView.layout.getLineEnd(endLine).coerceAtMost(originalText.length)

            while (endIndex > 0 && !originalText[endIndex - 1].isWhitespace()) endIndex--

            val trimmed = originalText.substring(0, endIndex).trimEnd()

            val spannable = SpannableStringBuilder("$trimmed$readMoreText")

            val clickable = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    this@ExpandableTextViewHelper.isExpanded = true
                    showExpanded()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = textView.context.getColor(R.color.theme)
                }
            }

            spannable.setSpan(clickable, trimmed.length, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textView.text = spannable
            textView.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun showExpanded() {
        val spannable = SpannableStringBuilder(originalText + readLessText)

        val clickable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                isExpanded = false
                prepareAndCollapse()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                ds.color = textView.context.getColor(android.R.color.holo_blue_light)
            }
        }

        spannable.setSpan(clickable, originalText.length, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannable
        textView.maxLines = Int.MAX_VALUE
        textView.ellipsize = null
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    // Call this from ViewHolder when view is recycled (optional but good)
    fun reset() {
        if (listenerAdded) {
            textView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
            listenerAdded = false
        }
        textView.text = ""
    }
}
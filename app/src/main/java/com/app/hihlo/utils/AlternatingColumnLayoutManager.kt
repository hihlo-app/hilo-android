package com.app.hihlo.utils

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AlternatingColumnLayoutManager : RecyclerView.LayoutManager() {

    private var totalHeight = 0
    private val verticalSpacing = dpToPx(14)
    private val horizontalSpacing = dpToPx(10)
    private var offsetTop = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun canScrollVertically(): Boolean = true

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val delta = when {
            offsetTop + dy < 0 -> -offsetTop
            offsetTop + dy > totalHeight - height -> (totalHeight - height - offsetTop).coerceAtLeast(-offsetTop)
            else -> dy
        }

        offsetChildrenVertical(-delta)
        offsetTop += delta
        return delta
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        if (itemCount == 0) {
            offsetTop = 0
            totalHeight = 0
            return
        }

        val oldOffsetTop = offsetTop
        offsetTop = oldOffsetTop.coerceAtMost(totalHeight - height).coerceAtLeast(0)

        var index = 0
        var leftColumnY = paddingTop
        var rightColumnY = paddingTop
        val parentWidth = width
        val columnWidth = (parentWidth - paddingLeft - paddingRight - horizontalSpacing) / 2
        var placeLeftTall = true

        while (index < itemCount) {
            if (placeLeftTall) {
                // Tall item on left
                val tallView = recycler.getViewForPosition(index++)
                addView(tallView)
                val tallParams = tallView.layoutParams as RecyclerView.LayoutParams
                val tallWidthSpec = View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY)
                val tallHeightSpec = ViewGroup.getChildMeasureSpec(0, 0, tallParams.height)
                tallView.measure(tallWidthSpec, tallHeightSpec)

//                measureChildWithMargins(tallView, 0, 0)
                val tallHeight = getDecoratedMeasuredHeight(tallView)
                layoutDecorated(
                    tallView,
                    paddingLeft,
                    leftColumnY,
                    paddingLeft + columnWidth,
                    leftColumnY + tallHeight
                )
                val nextLeftY = leftColumnY + tallHeight + verticalSpacing

                // Two short items on right
                var currentRightY = rightColumnY
                var lastRightY = rightColumnY
                for (i in 0 until 2) {
                    if (index >= itemCount) break
                    val shortView = recycler.getViewForPosition(index++)
                    addView(shortView)
                    val shortParams = shortView.layoutParams as RecyclerView.LayoutParams
                    val shortWidthSpec = View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY)
                    val shortHeightSpec = ViewGroup.getChildMeasureSpec(0, 0, shortParams.height)
                    shortView.measure(shortWidthSpec, shortHeightSpec)

//                    measureChildWithMargins(shortView, 0, 0)
                    val shortHeight = getDecoratedMeasuredHeight(shortView)
                    layoutDecorated(
                        shortView,
                        paddingLeft + columnWidth + horizontalSpacing,
                        currentRightY,
                        paddingLeft + columnWidth * 2 + horizontalSpacing,
                        currentRightY + shortHeight
                    )
                    lastRightY = currentRightY + shortHeight
                    currentRightY = lastRightY + verticalSpacing
                }

                leftColumnY = nextLeftY
                rightColumnY = maxOf(rightColumnY, lastRightY + verticalSpacing)
            } else {
                // Tall item on right
                val tallView = recycler.getViewForPosition(index++)
                addView(tallView)
                val tallParams = tallView.layoutParams as RecyclerView.LayoutParams
                val tallWidthSpec = View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY)
                val tallHeightSpec = ViewGroup.getChildMeasureSpec(0, 0, tallParams.height)
                tallView.measure(tallWidthSpec, tallHeightSpec)

//                measureChildWithMargins(tallView, 0, 0)
                val tallHeight = getDecoratedMeasuredHeight(tallView)
                layoutDecorated(
                    tallView,
                    paddingLeft + columnWidth + horizontalSpacing,
                    rightColumnY,
                    paddingLeft + columnWidth * 2 + horizontalSpacing,
                    rightColumnY + tallHeight
                )
                val nextRightY = rightColumnY + tallHeight + verticalSpacing

                // Two short items on left
                var currentLeftY = leftColumnY
                var lastLeftY = leftColumnY
                for (i in 0 until 2) {
                    if (index >= itemCount) break
                    val shortView = recycler.getViewForPosition(index++)
                    addView(shortView)
                    val shortParams = shortView.layoutParams as RecyclerView.LayoutParams
                    val shortWidthSpec = View.MeasureSpec.makeMeasureSpec(columnWidth, View.MeasureSpec.EXACTLY)
                    val shortHeightSpec = ViewGroup.getChildMeasureSpec(0, 0, shortParams.height)
                    shortView.measure(shortWidthSpec, shortHeightSpec)


//                    measureChildWithMargins(shortView, 0, 0)
                    val shortHeight = getDecoratedMeasuredHeight(shortView)
                    layoutDecorated(
                        shortView,
                        paddingLeft,
                        currentLeftY,
                        paddingLeft + columnWidth,
                        currentLeftY + shortHeight
                    )
                    lastLeftY = currentLeftY + shortHeight
                    currentLeftY = lastLeftY + verticalSpacing
                }

                rightColumnY = nextRightY
                leftColumnY = maxOf(leftColumnY, lastLeftY + verticalSpacing)
            }

            placeLeftTall = !placeLeftTall
        }

        totalHeight = maxOf(leftColumnY, rightColumnY)

        totalHeight = maxOf(leftColumnY, rightColumnY)

        // Restore scroll position after layout
        offsetChildrenVertical(-offsetTop)
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
    fun findLastVisibleItemPosition(): Int {
        var lastVisiblePosition = -1
        for (i in 0 until childCount) {
            val view = getChildAt(i) ?: continue
            val position = getPosition(view)
            if (getDecoratedBottom(view) > offsetTop && getDecoratedTop(view) < offsetTop + height) {
                lastVisiblePosition = maxOf(lastVisiblePosition, position)
            }
        }
        return lastVisiblePosition
    }
    fun getTotalHeight(): Int {
        return totalHeight
    }


}

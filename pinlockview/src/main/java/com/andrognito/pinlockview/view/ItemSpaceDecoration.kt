package com.andrognito.pinlockview.view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemSpaceDecoration(
    private val mHorizontalSpaceWidth: Int,
    private val mVerticalSpaceHeight: Int,
    private val mSpanCount: Int,
    private val mIncludeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view)
        val column = position % mSpanCount

        if (mIncludeEdge) {
            outRect.left = mHorizontalSpaceWidth - column * mHorizontalSpaceWidth / mSpanCount
            outRect.right = (column + 1) * mHorizontalSpaceWidth / mSpanCount

            if (position < mSpanCount) {
                outRect.top = mVerticalSpaceHeight
            }
            outRect.bottom = mVerticalSpaceHeight
        } else {
            outRect.left = column * mHorizontalSpaceWidth / mSpanCount
            outRect.right = mHorizontalSpaceWidth - (column + 1) * mHorizontalSpaceWidth / mSpanCount
            if (position >= mSpanCount) {
                outRect.top = mVerticalSpaceHeight
            }
        }
    }
}

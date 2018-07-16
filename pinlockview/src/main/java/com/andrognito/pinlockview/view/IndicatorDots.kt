package com.andrognito.pinlockview.view

import android.animation.LayoutTransition
import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.andrognito.pinlockview.R
import com.andrognito.pinlockview.util.ResourceUtils
import android.view.animation.AnimationUtils
import android.view.animation.Animation



class IndicatorDots @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mDotDiameter: Int = 0
    private var mDotSpacing: Int = 0
    private var mFillDrawable: Int = 0
    private var mEmptyDrawable: Int = 0
    private var mPinLength: Int = 0
    private var mIndicatorType: IndicatorType = IndicatorType.FILL

    private var mPreviousLength: Int = 0

    var pinLength: Int
        get() = mPinLength
        set(pinLength) {
            this.mPinLength = pinLength
            removeAllViews()
            initView(context)
        }

    var indicatorType: IndicatorType
        get() = mIndicatorType
        set(type) {
            this.mIndicatorType = type
            removeAllViews()
            initView(context)
        }

    enum class IndicatorType(val type: Int) {
        FIXED(0),
        FILL(1),
        FILL_WITH_ANIMATION(2),
    }

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorDots)

            try {
                mDotDiameter = typedArray.getDimension(R.styleable.IndicatorDots_dotDiameter, ResourceUtils.getDimensionInPx(getContext(), R.dimen.default_dot_diameter)).toInt()
                mDotSpacing = typedArray.getDimension(R.styleable.IndicatorDots_dotSpacing, ResourceUtils.getDimensionInPx(getContext(), R.dimen.default_dot_spacing)).toInt()
                mFillDrawable = typedArray.getResourceId(R.styleable.IndicatorDots_dotFilledBackground,
                    R.drawable.dot_filled)
                mEmptyDrawable = typedArray.getResourceId(R.styleable.IndicatorDots_dotEmptyBackground,
                    R.drawable.dot_empty)
                mIndicatorType = when (typedArray.getInt(R.styleable.IndicatorDots_indicatorType, IndicatorType.FIXED.type)) {
                    0 -> IndicatorType.FIXED
                    1 -> IndicatorType.FILL
                    2 -> IndicatorType.FILL_WITH_ANIMATION
                    else -> throw IllegalArgumentException("Unexpected IndicatorType")
                }
            } finally {
                typedArray.recycle()
            }

            initView(context)
        }
    }

    private fun initView(context: Context) {
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR)
        when (mIndicatorType) {
            IndicatorType.FIXED -> {
                for (i in 0 until mPinLength) {
                    val dot = View(context)
                    emptyDot(dot)

                    val params = LinearLayout.LayoutParams(mDotDiameter,
                        mDotDiameter)
                    params.setMargins(mDotSpacing, 0, mDotSpacing, 0)
                    dot.layoutParams = params

                    addView(dot)
                }
            }
            IndicatorType.FILL_WITH_ANIMATION -> layoutTransition = LayoutTransition()
            else -> {
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // If the indicator type is not fixed
        if (mIndicatorType != IndicatorType.FIXED) {
            val params = this.layoutParams
            params.height = mDotDiameter
            requestLayout()
        }
    }

    internal fun updateDot(length: Int) {
        if (mIndicatorType == IndicatorType.FIXED) {
            if (length > 0) {
                if (length > mPreviousLength) {
                    fillDot(getChildAt(length - 1))
                } else {
                    emptyDot(getChildAt(length))
                }
                mPreviousLength = length
            } else {
                // When {@code mPinLength} is 0, we need to reset all the views back to empty
                for (i in 0 until childCount) {
                    val v = getChildAt(i)
                    emptyDot(v)
                }
                mPreviousLength = 0
            }
        } else {
            if (length > 0) {
                if (length > mPreviousLength) {
                    val dot = View(context)
                    fillDot(dot)

                    val params = LinearLayout.LayoutParams(mDotDiameter, mDotDiameter)
                    params.setMargins(mDotSpacing, 0, mDotSpacing, 0)
                    dot.layoutParams = params

                    addView(dot, length - 1)
                } else {
                    removeViewAt(length)
                }
                mPreviousLength = length
            } else {
                removeAllViews()
                mPreviousLength = 0
            }
        }
    }

    fun vibrate(listener: Animation.AnimationListener) {
        val vibrate = AnimationUtils.loadAnimation(context, R.anim.vibrate)
        vibrate.setAnimationListener(listener)
        this.startAnimation(vibrate)
    }

    private fun emptyDot(dot: View) {
        dot.setBackgroundResource(mEmptyDrawable)
    }

    private fun fillDot(dot: View) {
        dot.setBackgroundResource(mFillDrawable)
    }

    companion object {
        private val DEFAULT_PIN_LENGTH = 4
    }
}

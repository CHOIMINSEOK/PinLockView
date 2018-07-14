package com.andrognito.pinlockview.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.andrognito.pinlockview.R
import com.andrognito.pinlockview.data.CustomizedOptionsBundle
import com.andrognito.pinlockview.util.ResourceUtils

class PinLockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var mPin = ""
    private var mPinLength: Int = 0
    private var mHorizontalSpacing: Int = 0
    private var mVerticalSpacing: Int = 0
    private var mTextColor: Int = 0
    private var mDeleteButtonPressedColor: Int = 0
    private var mTextSize: Int = 0
    private var mButtonSize: Int = 0
    private var mDeleteButtonSize: Int = 0
    private var mButtonBackgroundDrawable: Drawable? = null
    private var mDeleteButtonDrawable: Drawable? = null
    private var mShowDeleteButton: Boolean = false

    private var mIndicatorDots: IndicatorDots? = null
    private lateinit var mAdapter: PinLockAdapter
    private var mPinLockListener: PinLockListener? = null
    private var mCustomizedOptionsBundle = CustomizedOptionsBundle()

    private val mOnNumberClickListener = object : PinLockAdapter.OnNumberClickListener {
        override fun onNumberClicked(keyValue: Int) {
            if (mPin.length < mPinLength) {
                mPin += keyValue.toString()

                mIndicatorDots?.updateDot(mPin.length)

                if (mPin.length == 1) {
                    mAdapter.pinLength = mPin.length
                    mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
                }

                if (mPin.length == mPinLength) {
                    mPinLockListener?.onComplete(mPin)
                } else {
                    mPinLockListener?.onPinChange(mPin.length, mPin)
                }

            } else {
                if (!mShowDeleteButton) {
                    resetPinLockView()
                    mPin += keyValue.toString()

                    mIndicatorDots?.updateDot(mPin.length)
                    mPinLockListener?.onPinChange(mPin.length, mPin)

                } else {
                    mPinLockListener?.onComplete(mPin)

                }
            }
        }
    }

    private val mOnDeleteClickListener = object : PinLockAdapter.OnDeleteClickListener {
        override fun onDeleteClicked() {
            if (mPin.isNotEmpty()) {
                mPin = mPin.substring(0, mPin.length - 1)

                mIndicatorDots?.updateDot(mPin.length)
                if (mPin.isEmpty()) {
                    mAdapter.pinLength = mPin.length
                    mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
                }

                if (mPin.isEmpty()) {
                    mPinLockListener?.onEmpty()
                    clearInternalPin()
                } else {
                    mPinLockListener?.onPinChange(mPin.length, mPin)
                }
            } else {
                mPinLockListener?.onEmpty()
            }
        }

        override fun onDeleteLongClicked() {
            resetPinLockView()
            mPinLockListener?.onEmpty()
        }
    }

    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinLockView)

            try {
                mPinLength = typedArray.getInt(R.styleable.PinLockView_pinLength, DEFAULT_PIN_LENGTH)
                mHorizontalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadHorizontalSpacing, ResourceUtils.getDimensionInPx(context, R.dimen.default_horizontal_spacing)).toInt()
                mVerticalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadVerticalSpacing, ResourceUtils.getDimensionInPx(context, R.dimen.default_vertical_spacing)).toInt()
                mTextColor = typedArray.getColor(R.styleable.PinLockView_keypadTextColor, ResourceUtils.getColor(context, R.color.white))
                mTextSize = typedArray.getDimension(R.styleable.PinLockView_keypadTextSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_text_size)).toInt()
                mButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_button_size)).toInt()
                mDeleteButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadDeleteButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_delete_button_size)).toInt()
                mButtonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadButtonBackgroundDrawable)
                mDeleteButtonDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadDeleteButtonDrawable)
                mShowDeleteButton = typedArray.getBoolean(R.styleable.PinLockView_keypadShowDeleteButton, true)
                mDeleteButtonPressedColor = typedArray.getColor(R.styleable.PinLockView_keypadDeleteButtonPressedColor, ResourceUtils.getColor(context, R.color.greyish))
            } finally {
                typedArray.recycle()
            }

            mCustomizedOptionsBundle.textColor = mTextColor
            mCustomizedOptionsBundle.textSize = mTextSize
            mCustomizedOptionsBundle.buttonSize = mButtonSize
            mCustomizedOptionsBundle.buttonBackgroundDrawable = mButtonBackgroundDrawable
            mCustomizedOptionsBundle.deleteButtonDrawable = mDeleteButtonDrawable
            mCustomizedOptionsBundle.deleteButtonSize = mDeleteButtonSize
            mCustomizedOptionsBundle.showDeleteButton = mShowDeleteButton
            mCustomizedOptionsBundle.deleteButtonPressesColor = mDeleteButtonPressedColor

            initView()
        }

    }

    private fun initView() {
        layoutManager = LTRGridLayoutManager(context, 3)

        mAdapter = PinLockAdapter(mCustomizedOptionsBundle)
        mAdapter.onItemClickListener = mOnNumberClickListener
        mAdapter.onDeleteClickListener = mOnDeleteClickListener
        adapter = mAdapter

        addItemDecoration(ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3, false))
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    /**
     * Sets a [PinLockListener] to the to listen to pin update events
     *
     * @param pinLockListener the listener
     */
    fun setPinLockListener(pinLockListener: PinLockListener) {
        this.mPinLockListener = pinLockListener
    }

    private fun clearInternalPin() {
        mPin = ""
    }

    /**
     * Resets the [PinLockView], clearing the entered pin
     * and resetting the [IndicatorDots] if attached
     */
    fun resetPinLockView() {

        clearInternalPin()

        mAdapter.pinLength = mPin.length
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)

        mIndicatorDots?.updateDot(mPin.length)
    }

    /**
     * Attaches [IndicatorDots] to [PinLockView]
     *
     * @param mIndicatorDots the view to attach
     */
    fun attachIndicatorDots(mIndicatorDots: IndicatorDots) {
        this.mIndicatorDots = mIndicatorDots.apply { this.pinLength = mPinLength }
    }

    companion object {

        private val DEFAULT_PIN_LENGTH = 4
        private val DEFAULT_KEY_SET = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    }
}

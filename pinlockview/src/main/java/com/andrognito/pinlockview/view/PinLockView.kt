package com.andrognito.pinlockview.view

import android.content.Context
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

    private lateinit var mAdapter: PinLockAdapter
    private var mCustomizedOptionsBundle = CustomizedOptionsBundle()

    init {
        if (attrs != null) {
            try {
                initCustomOptions(attrs)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            initView()
        }

    }

    private fun initCustomOptions(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinLockView)

        val mPinLength = typedArray.getInt(R.styleable.PinLockView_pinLength, DEFAULT_PIN_LENGTH)
        val mHorizontalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadHorizontalSpacing, ResourceUtils.getDimensionInPx(context, R.dimen.default_horizontal_spacing)).toInt()
        val mVerticalSpacing = typedArray.getDimension(R.styleable.PinLockView_keypadVerticalSpacing, ResourceUtils.getDimensionInPx(context, R.dimen.default_vertical_spacing)).toInt()
        val mTextColor = typedArray.getColor(R.styleable.PinLockView_keypadTextColor, ResourceUtils.getColor(context, R.color.white))
        val mTextSize = typedArray.getDimension(R.styleable.PinLockView_keypadTextSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_text_size)).toInt()
        val mButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_button_size)).toInt()
        val mDeleteButtonSize = typedArray.getDimension(R.styleable.PinLockView_keypadDeleteButtonSize, ResourceUtils.getDimensionInPx(context, R.dimen.default_delete_button_size)).toInt()
        val mButtonBackgroundDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadButtonBackgroundDrawable)
        val mDeleteButtonDrawable = typedArray.getDrawable(R.styleable.PinLockView_keypadDeleteButtonDrawable)
        val mShowDeleteButton = typedArray.getBoolean(R.styleable.PinLockView_keypadShowDeleteButton, true)
        val mDeleteButtonPressedColor = typedArray.getColor(R.styleable.PinLockView_keypadDeleteButtonPressedColor, ResourceUtils.getColor(context, R.color.greyish))

        mCustomizedOptionsBundle.pinLength = mPinLength
        mCustomizedOptionsBundle.textColor = mTextColor
        mCustomizedOptionsBundle.textSize = mTextSize
        mCustomizedOptionsBundle.buttonSize = mButtonSize
        mCustomizedOptionsBundle.buttonBackgroundDrawable = mButtonBackgroundDrawable
        mCustomizedOptionsBundle.deleteButtonDrawable = mDeleteButtonDrawable
        mCustomizedOptionsBundle.deleteButtonSize = mDeleteButtonSize
        mCustomizedOptionsBundle.showDeleteButton = mShowDeleteButton
        mCustomizedOptionsBundle.deleteButtonPressesColor = mDeleteButtonPressedColor

        addItemDecoration(ItemSpaceDecoration(mHorizontalSpacing, mVerticalSpacing, 3, false))

        typedArray.recycle()
    }

    private fun initView() {
        layoutManager = LTRGridLayoutManager(context, 3)

        mAdapter = PinLockAdapter(mCustomizedOptionsBundle)
        adapter = mAdapter

        overScrollMode = View.OVER_SCROLL_NEVER
    }

    /**
     * Sets a [PinLockListener] to the to listen to pin update events
     *
     * @param pinLockListener the listener
     */
    fun setPinLockListener(pinLockListener: PinLockListener) {
        this.mAdapter.mPinLockListener = pinLockListener
    }

    /**
     * Attaches [IndicatorDots] to [PinLockView]
     *
     * @param mIndicatorDots the view to attach
     */
    fun attachIndicatorDots(mIndicatorDots: IndicatorDots) {
        mAdapter.indicatorDots = mIndicatorDots.apply {
           pinLength = mAdapter.pinLength
        }
    }

    companion object {

        private val DEFAULT_PIN_LENGTH = 4
        private val DEFAULT_KEY_SET = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
    }
}

package com.andrognito.pinlockviewapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.andrognito.pinlockview.view.IndicatorDots
import com.andrognito.pinlockview.view.PinLockListener
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity: AppCompatActivity() {
    val TAG = "PinLockView"

    private val mPinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            Log.d(TAG, "Pin complete: $pin")
        }

        override fun onEmpty() {
            Log.d(TAG, "Pin empty")
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
            Log.d(TAG, "Pin changed, new length $pinLength with intermediate pin $intermediatePin")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        with(pin_lock_view) {
            attachIndicatorDots(indicator_dots)
            setPinLockListener(mPinLockListener)
        }

        indicator_dots.indicatorType = IndicatorDots.IndicatorType.FILL_WITH_ANIMATION



    }
}

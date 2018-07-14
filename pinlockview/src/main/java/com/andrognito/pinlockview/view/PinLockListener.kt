package com.andrognito.pinlockview.view

interface PinLockListener {
    fun onComplete(pin: String)

    fun onEmpty()

    fun onPinChange(pinLength: Int, intermediatePin: String)
}

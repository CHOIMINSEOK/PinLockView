package com.andrognito.pinlockview.view

import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.andrognito.pinlockview.R
import com.andrognito.pinlockview.data.CustomizedOptionsBundle

class PinLockAdapter(
    var customizationOptions: CustomizedOptionsBundle = CustomizedOptionsBundle()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemClickListener: OnNumberClickListener? = null
    var onDeleteClickListener: OnDeleteClickListener? = null
    var pinLength: Int = 0

    private var mKeyValues: IntArray = getAdjustKeyValues(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))

    var keyValues: IntArray
        get() = mKeyValues
        set(keyValues) {
            this.mKeyValues = getAdjustKeyValues(keyValues)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val inflater = LayoutInflater.from(parent.context)

        if (viewType == VIEW_TYPE_NUMBER) {
            val view = inflater.inflate(R.layout.viewholder_lock_password_number, parent, false)
            viewHolder = NumberViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.viewholder_lock_password_delete, parent, false)
            viewHolder = DeleteViewHolder(view)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_NUMBER) {
            val vh1 = holder as NumberViewHolder
            configureNumberButtonHolder(vh1, position)
        } else if (holder.itemViewType == VIEW_TYPE_DELETE) {
            val vh2 = holder as DeleteViewHolder
            configureDeleteButtonHolder(vh2)
        }
    }

    private fun configureNumberButtonHolder(holder: NumberViewHolder, position: Int) {
        if (position == 9) {
            holder.mNumberButton.visibility = View.GONE
        } else {
            holder.mNumberButton.text = mKeyValues[position].toString()
            holder.mNumberButton.visibility = View.VISIBLE
            holder.mNumberButton.tag = mKeyValues[position]
        }

        customizationOptions.let {
            holder.mNumberButton.setTextColor(it.textColor)
            if (it.buttonBackgroundDrawable != null) {
                holder.mNumberButton.background = it.buttonBackgroundDrawable
            }
            holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, it.textSize.toFloat())
            val params = LinearLayout.LayoutParams(
                it.buttonSize,
                it.buttonSize)
            holder.mNumberButton.layoutParams = params
        }
    }

    private fun configureDeleteButtonHolder(holder: DeleteViewHolder) {
        if (customizationOptions.showDeleteButton && pinLength > 0) {
            holder.mButtonImage.visibility = View.VISIBLE
            if (customizationOptions.deleteButtonDrawable != null) {
                holder.mButtonImage.setImageDrawable(customizationOptions.deleteButtonDrawable)
            }
            holder.mButtonImage.setColorFilter(customizationOptions.textColor, PorterDuff.Mode.SRC_ATOP)
            val params = LinearLayout.LayoutParams(
                customizationOptions.deleteButtonSize,
                customizationOptions.deleteButtonSize
            )
            holder.mButtonImage.layoutParams = params
        } else {
            holder.mButtonImage.visibility = View.GONE
        }
    }

    override fun getItemCount() = 12

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            VIEW_TYPE_DELETE
        } else VIEW_TYPE_NUMBER
    }

    private fun getAdjustKeyValues(keyValues: IntArray): IntArray {
        val adjustedKeyValues = IntArray(keyValues.size + 1)
        for (i in keyValues.indices) {
            if (i < 9) {
                adjustedKeyValues[i] = keyValues[i]
            } else {
                adjustedKeyValues[i] = -1
                adjustedKeyValues[i + 1] = keyValues[i]
            }
        }
        return adjustedKeyValues
    }

    inner class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNumberButton: Button = itemView.findViewById(R.id.button)

        init {
            mNumberButton.setOnClickListener { v ->
                onItemClickListener?.onNumberClicked(v.tag as Int)
            }
        }
    }

    inner class DeleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mDeleteButton: LinearLayout = itemView.findViewById(R.id.button)
        var mButtonImage: ImageView = itemView.findViewById(R.id.buttonImage)

        init {

            if (customizationOptions.showDeleteButton && pinLength > 0) {
                mDeleteButton.setOnClickListener { onDeleteClickListener?.onDeleteClicked() }

                mDeleteButton.setOnLongClickListener {
                    onDeleteClickListener?.onDeleteLongClicked()

                    true
                }
            }
        }
    }

    interface OnNumberClickListener {
        fun onNumberClicked(keyValue: Int)
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked()

        fun onDeleteLongClicked()
    }

    companion object {
        private const val VIEW_TYPE_NUMBER = 0
        private const val VIEW_TYPE_DELETE = 1
    }
}


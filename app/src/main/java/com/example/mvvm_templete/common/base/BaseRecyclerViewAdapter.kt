package com.example.mvvm_templete.common.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

// 공통 RecyclerView 클래스
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var arrayList: MutableList<T>? = null
    private var onItemClickListener: OnItemClickListener? = null
    var context: Context
    lateinit var TAG: String

    constructor(context: Context) {
        this.context = context
        TAG = javaClass.simpleName
    }

    constructor(context: Context, arrayList: MutableList<T>) {
        this.context = context
        this.arrayList = arrayList
    }

    override fun getItemCount(): Int {
        return if (arrayList == null) 0 else arrayList!!.size

    }

    fun getItem(position: Int): T? {
        return if (arrayList == null) null else arrayList!![position]
    }

    fun getItem(): MutableList<T>? {
        return arrayList
    }

    fun updateItems(items: List<T>) {

        if (this.arrayList == null) {
            arrayList = ArrayList()
        }
        this.arrayList!!.clear()
        this.arrayList!!.addAll(items)

        notifyDataSetChanged()
    }

    fun addItems(items: MutableList<T>) {

        if (this.arrayList == null) {
            this.arrayList = items
        } else {
            this.arrayList!!.addAll(items)
        }

        notifyDataSetChanged()
    }

    fun removeItems(position: Int) {
        this.arrayList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearItems() {
        if (arrayList != null) {

            arrayList!!.clear()
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener!!.onItemClick(holder.itemView, position)
            }
        }
        onBindView(holder, position)
    }

    abstract fun onBindView(holder: RecyclerView.ViewHolder, position: Int)

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}

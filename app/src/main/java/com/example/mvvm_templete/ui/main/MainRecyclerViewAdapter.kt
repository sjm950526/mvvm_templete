package com.example.mvvm_templete.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_templete.R
import com.example.mvvm_templete.common.base.BaseRecyclerViewAdapter
import com.example.mvvm_templete.databinding.RecyclerviewMainItemBinding
import com.example.mvvm_templete.remote.data.ResponseTest

class MainRecyclerViewAdapter(context: Context) : BaseRecyclerViewAdapter<ResponseTest>(context) {
    class FileLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RecyclerviewMainItemBinding = DataBindingUtil.bind(itemView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FileLogViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_main_item, parent, false)
        )
    }

    override fun onBindView(holder: RecyclerView.ViewHolder, position: Int) {
        holder as FileLogViewHolder
        holder.binding.tvId.text = "${getItem()?.get(position)?.id}"
        holder.binding.tvTitle.text = "${getItem()?.get(position)?.title}"
        holder.binding.tvCompleted.text =
            if (getItem()?.get(position)?.completed ?: false) "미완료" else "완료"

    }

}
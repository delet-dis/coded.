package com.hits.coded.presentation.views.codeBlocks.variables.creationBlock.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.hits.coded.databinding.SpinnerCustomItemBinding

class VariableTypeSpinnerAdapter(private val items: List<String>) : BaseAdapter() {

    private lateinit var binding: SpinnerCustomItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: Holder

        if (convertView == null) {
            binding = SpinnerCustomItemBinding.inflate(LayoutInflater.from(parent?.context))
            view = binding.root

            holder = Holder(binding)

            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as Holder
        }

        holder.textView.text = items[position]

        return view
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    private class Holder(binding: SpinnerCustomItemBinding) {
        val textView: TextView = binding.text
    }
}
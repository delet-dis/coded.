package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragments.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hits.coded.R
import com.hits.coded.data.models.types.VariableType
import com.hits.coded.databinding.RecyclerViewVariableTypeBinding

class VariableTypeChangerAdapter(
    private val values: Array<VariableType>,
    val clickListener: (VariableType, Boolean) -> Unit,
    val isArray: Boolean
) : RecyclerView.Adapter<VariableTypeChangerAdapter.VariableTypeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariableTypeHolder {
        return VariableTypeHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_view_variable_type, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VariableTypeHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int = values.size

    inner class VariableTypeHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding: RecyclerViewVariableTypeBinding =
            RecyclerViewVariableTypeBinding.bind(view)

        fun bind(data: VariableType) {
            binding.type.setText(data.typeAsStringResource)

            binding.root.setOnClickListener {
                clickListener.invoke(data, isArray)
            }
        }
    }
}
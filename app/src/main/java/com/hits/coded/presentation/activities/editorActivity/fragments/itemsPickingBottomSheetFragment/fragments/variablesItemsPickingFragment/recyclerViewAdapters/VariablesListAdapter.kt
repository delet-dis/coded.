package com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheetFragment.fragments.variablesItemsPickingFragment.recyclerViewAdapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hits.coded.R
import com.hits.coded.data.models.codeBlocks.dataClasses.VariableBlock
import com.hits.coded.databinding.ViewVariableBlockBinding
import com.hits.coded.presentation.views.codeBlocks.UIVariableBlock

class VariablesListAdapter(
    private val variables: Array<VariableBlock>,
    private val clickListenerForVariable: (VariableBlock) -> Unit,
    private val longListenerForVariable: (VariableBlock) -> Unit,
    private val clickListenerForAddButton: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            0 -> {
                CreateVariableButtonHolder(UIVariableBlock(parent.context))
            }
            else -> {
                CreateVariableButtonHolder(UIVariableBlock(parent.context))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreateVariableButtonHolder -> holder.bindCreateVariableButton()
            is VariableButtonHolder -> holder.bindVariable(variables[position])
        }
    }

    override fun getItemCount(): Int = variables.size + 1

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> 0
            else -> 1
        }

    private inner class CreateVariableButtonHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindCreateVariableButton() = with(ViewVariableBlockBinding.bind(view)) {
            this.variableName.text = view.context.getString(R.string.createVariable)

            this.root.setOnClickListener {
                clickListenerForAddButton()
            }
        }
    }

    private inner class VariableButtonHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bindVariable(variableBlock: VariableBlock) {
            (view as? UIVariableBlock)?.let {
                it.setVariableBlock(variableBlock)

                ViewVariableBlockBinding.bind(view).root.apply {
                    setOnClickListener {
                        clickListenerForVariable(variableBlock)
                    }

                    setOnLongClickListener {
                        longListenerForVariable(variableBlock)

                        true
                    }
                }
            }
        }
    }
}
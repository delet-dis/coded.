package com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.hits.coded.R
import com.hits.coded.data.interfaces.ui.typeChangerBottomSheet.UIBottomSheetTypeChangerFragmentInterface
import com.hits.coded.data.models.typeChangerBottomSheet.enums.BottomSheetTypeChangersScreens
import com.hits.coded.data.models.types.VariableType
import com.hits.coded.databinding.IncludeVariableTypeChangerBottomSheetBinding
import com.hits.coded.domain.extensions.dpToPx
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.fragmentStateAdapters.VariableTypeChangerViewPagerAdapter
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.viewModel.VariableTypeChangerViewModel

class VariableTypeChangerBottomSheetController(
    private val binding: IncludeVariableTypeChangerBottomSheetBinding,
    private val viewModel: VariableTypeChangerViewModel,
    private val parentActivity: Activity
) {

    private val behaviour = BottomSheetBehavior.from(binding.typeChangerBottomSheetLayout)

    init {
        initParentViewOnTouchListener()

        initDismissButtonOnClickListener()

        initViewPager()

        initTopMargin()

        initTabLayoutMediator()
    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(
            binding.typeChangerTabLayout,
            binding.typeChangerViewPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text =
                        binding.root.context.getString(R.string.variableType)
                            .lowercase()
                            .replaceFirstChar {
                                it.uppercase()
                            }
                }

                1 -> {
                    tab.text =
                        binding.root.context.getString(R.string.arrayType)
                            .lowercase()
                            .replaceFirstChar {
                                it.uppercase()
                            }
                }
            }

        }.attach()
    }

    private fun initTopMargin() {
        binding.typeChangerBottomSheetLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(0, 50.dpToPx(binding.root.context), 0, 0)
        }
    }

    fun show(closureToInvokeAfterTypePick: (VariableType, Boolean) -> Unit) {
        behaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        initFragmentsRecyclers(closureToInvokeAfterTypePick)
    }

    private fun initFragmentsRecyclers(closureToInvokeAfterTypePick: (VariableType, Boolean) -> Unit) {
        BottomSheetTypeChangersScreens.values().forEach {
            (it.bottomSheetTypeChangerScreen.screen as? UIBottomSheetTypeChangerFragmentInterface)?.initRecyclerView(
                binding.root.context,
                viewModel.getVariablesTypes(),
                closureToInvokeAfterTypePick
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initParentViewOnTouchListener() {
        binding.typeChangerBottomSheetLayout.setOnTouchListener { _, _ ->
            true
        }
    }

    private fun initDismissButtonOnClickListener() =
        binding.typeChangerxMarkButton.setOnClickListener {
            behaviour.state = BottomSheetBehavior.STATE_HIDDEN
        }

    private fun initViewPager() {
        binding.typeChangerViewPager.adapter =
            VariableTypeChangerViewPagerAdapter(
                parentActivity as FragmentActivity,
                viewModel.getTypeChangerScreens()
            )
    }
}

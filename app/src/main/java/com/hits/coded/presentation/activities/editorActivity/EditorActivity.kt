package com.hits.coded.presentation.activities.editorActivity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hits.coded.R
import com.hits.coded.data.interfaces.callbacks.ui.UIEditorActivityShowBottomSheetCallback
import com.hits.coded.data.models.types.VariableType
import com.hits.coded.databinding.ActivityEditorBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.ItemsPickingBottomSheetController
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.viewModel.ItemsPickingBottomSheetViewModel
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.VariableTypeChangerBottomSheetController
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.viewModel.VariableTypeChangerViewModel
import com.hits.coded.presentation.activities.editorActivity.viewModel.EditorActivityViewModel
import com.hits.coded.presentation.views.codeField.CodeField
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditorActivity : AppCompatActivity(), UIEditorActivityShowBottomSheetCallback {
    private lateinit var binding: ActivityEditorBinding

    private val viewModel: EditorActivityViewModel by viewModels()

    private lateinit var itemsPickingBottomSheetController: ItemsPickingBottomSheetController
    private lateinit var typeChangerBottomSheetController: VariableTypeChangerBottomSheetController

    private lateinit var codeField: CodeField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initSystemBarsDimensionChangesListener()

        initIsBarsCollapsedObserver()

        initCodeField()

        initScrollableLayoutDimensions()

        initBarsStateChangingBasedOnFieldClick()

        initItemsPickingBottomSheet()

        initVariableTypeChangerBottomSheet()

        initBottomBarButtonsOnClicks()
    }

    private fun initBinding() {
        binding = ActivityEditorBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun initSystemBarsDimensionChangesListener() =
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _: View?, insets: WindowInsetsCompat ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            changeTopBarHeight(statusBarHeight)
            changeBottomBarHeight(navigationBarHeight)

            WindowInsetsCompat.CONSUMED
        }

    private fun changeTopBarHeight(statusBarHeight: Int) {
        val openedTopBarHeight = resources.getDimension(R.dimen.openedTopBarHeight).toInt()
        val recalculatedOpenedBarHeight = openedTopBarHeight + statusBarHeight

        val collapsedBarHeight = resources.getDimension(R.dimen.collapsedTopBarHeight).toInt()
        val recalculatedCollapsedBarHeight = collapsedBarHeight + statusBarHeight

        with(binding) {
            motionLayout.getConstraintSet(R.id.barsOpened)
                .constrainHeight(topBarWrapper.id, recalculatedOpenedBarHeight)

            motionLayout.getConstraintSet(R.id.barsCollapsed)
                .constrainHeight(topBarWrapper.id, recalculatedCollapsedBarHeight)

            topBarContentLayout.layoutParams.height = openedTopBarHeight
        }
    }

    private fun changeBottomBarHeight(bottomBarHeight: Int) {
        val openedBottomBarHeight = resources.getDimension(R.dimen.openedBottomBarHeight).toInt()
        val recalculatedHeight = openedBottomBarHeight + bottomBarHeight

        with(binding) {
            motionLayout.getConstraintSet(R.id.barsOpened)
                .constrainHeight(bottomBarWrapper.id, recalculatedHeight)

            bottomBarContentLayout.layoutParams.height = openedBottomBarHeight
        }
    }

    private fun collapseBars() {
        binding.motionLayout.transitionToState(R.id.barsCollapsed)
        binding.topBarContentLayout.transitionToState(R.id.topBarProjectNameMinified)
    }

    private fun openBars() {
        binding.motionLayout.transitionToState(R.id.barsOpened)
        binding.topBarContentLayout.transitionToState(R.id.topBarProjectNameDisplaying)
    }

    private fun initIsBarsCollapsedObserver() =
        viewModel.isBarsCollapsed.observe(this) {
            if (it) {
                collapseBars()
            } else {
                openBars()
            }
        }

    private fun initCodeField() {
        codeField = CodeField(this)

        binding.zoomLayout.addView(codeField)

        codeField.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun initScrollableLayoutDimensions() {
        with(resources.displayMetrics) {
            codeField.layoutParams.apply {
                height = heightPixels * 3
                width = widthPixels * 3
            }
        }
    }

    private fun initBarsStateChangingBasedOnFieldClick() =
        codeField.setOnClickListener {
            viewModel.toggleBars()
        }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        initScrollableLayoutDimensions()

        viewModel.hideBars()
    }

    private fun initItemsPickingBottomSheet() {
        val bottomSheetViewModel: ItemsPickingBottomSheetViewModel by viewModels()
        itemsPickingBottomSheetController = ItemsPickingBottomSheetController(
            binding.itemsPickingBottomSheet,
            bottomSheetViewModel,
            this
        )
    }

    private fun initVariableTypeChangerBottomSheet() {
        val bottomSheetViewModel: VariableTypeChangerViewModel by viewModels()

        typeChangerBottomSheetController = VariableTypeChangerBottomSheetController(
            binding.variableTypeChangerBottomSheet,
            bottomSheetViewModel, this
        )
    }

    private fun showBottomSheet() {
        itemsPickingBottomSheetController.show()
    }

    private fun initBottomBarButtonsOnClicks() =
        with(binding) {
            menuButton.setOnClickListener {
                showBottomSheet()
            }
        }

    override fun showTypeChangingBottomSheet(closureToInvoke: (VariableType, Boolean) -> Unit) {
        typeChangerBottomSheetController.show(closureToInvoke)
    }
}
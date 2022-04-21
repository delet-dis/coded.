package com.hits.coded.presentation.activities.editorActivity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hits.coded.R
import com.hits.coded.databinding.ActivityEditorBinding
import com.hits.coded.presentation.activities.editorActivity.viewModel.EditorActivityViewModel


class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding

    private val viewModel: EditorActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initSystemBarsDimensionChangesListener()

        initIsBarsCollapsedObserver()

        binding.collapseButton.setOnClickListener {
            viewModel.collapseBars()
        }

        binding.openButton.setOnClickListener {
            viewModel.openBars()
        }
    }

    private fun initBinding() {
        binding = ActivityEditorBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun initSystemBarsDimensionChangesListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _: View?, insets: WindowInsetsCompat ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            changeTopBarHeight(statusBarHeight)
            changeBottomBarHeight(navigationBarHeight)

            WindowInsetsCompat.CONSUMED
        }
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

    private fun initIsBarsCollapsedObserver() {
        viewModel.isBarsCollapsed.observe(this) {
            if (it) {
                collapseBars()
            } else {
                openBars()
            }
        }
    }
}
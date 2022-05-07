package com.hits.coded.presentation.activities.editorActivity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.hits.coded.R
import com.hits.coded.data.interfaces.callbacks.ui.UIEditorActivityShowBottomSheetCallback
import com.hits.coded.data.interfaces.ui.UIElementHandlesCodeBlocksDeletingInterface
import com.hits.coded.data.interfaces.ui.UIElementHandlesCustomRemoveViewProcessInterface
import com.hits.coded.data.interfaces.ui.codeBlocks.UICodeBlockElementHandlesDragAndDropInterface
import com.hits.coded.data.models.codeBlocks.dataClasses.StartBlock
import com.hits.coded.data.models.sharedTypes.VariableType
import com.hits.coded.databinding.ActivityEditorBinding
import com.hits.coded.presentation.activities.editorActivity.fragments.consoleBottomSheet.ConsoleBottomSheetController
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.ItemsPickingBottomSheetController
import com.hits.coded.presentation.activities.editorActivity.fragments.itemsPickingBottomSheet.viewModel.ItemsPickingBottomSheetViewModel
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.VariableTypeChangerBottomSheetController
import com.hits.coded.presentation.activities.editorActivity.fragments.variableTypeChangerBottomSheet.viewModel.VariableTypeChangerViewModel
import com.hits.coded.presentation.activities.editorActivity.viewModel.EditorActivityViewModel
import com.hits.coded.presentation.views.codeField.CodeField
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates


@AndroidEntryPoint
class EditorActivity : AppCompatActivity(), UIEditorActivityShowBottomSheetCallback,
    UIElementHandlesCodeBlocksDeletingInterface, UIElementHandlesCustomRemoveViewProcessInterface,
    UICodeBlockElementHandlesDragAndDropInterface {
    private lateinit var binding: ActivityEditorBinding

    private val viewModel: EditorActivityViewModel by viewModels()

    private lateinit var itemsPickingBottomSheetController: ItemsPickingBottomSheetController
    private lateinit var typeChangerBottomSheetController: VariableTypeChangerBottomSheetController
    private lateinit var consoleBottomSheetController: ConsoleBottomSheetController

    private lateinit var codeField: CodeField

    private var statusBarHeight by Delegates.notNull<Int>()
    private var navigationBarHeight by Delegates.notNull<Int>()

    override var animationSet = AnimatorSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initSystemBarsDimensionChangesListener()

        initIsBarsCollapsedObserver()

        initIsCodeExecutingObserver()

        initCodeField()

        initScrollableLayoutDimensions()

        initBarsStateChangingBasedOnFieldClick()

        initItemsPickingBottomSheet()

        initVariableTypeChangerBottomSheet()

        initConsoleBottomSheet()

        initBottomBarButtonsOnClicks()

        initIsConsoleInputAvailableObserver()

        initCodeExecutionResultObserver()

        initTrashButtonDragAndDropListener()
    }

    private fun initBinding() {
        binding = ActivityEditorBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun initSystemBarsDimensionChangesListener() =
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _: View?, insets: WindowInsetsCompat ->
            val navigationBarsInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            navigationBarHeight =
                navigationBarsInsets.bottom

            changeTopBarHeight(statusBarHeight)
            changeBottomBarHeight(navigationBarHeight)
            updateParentLayoutMargins(navigationBarsInsets.left, 0, navigationBarsInsets.right, 0)

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
        codeField = CodeField(this).apply {
            parentView = this@EditorActivity
        }

        binding.zoomLayout.addView(codeField)

        codeField.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun initScrollableLayoutDimensions() =
        with(resources.displayMetrics) {
            codeField.layoutParams.apply {
                height = heightPixels * 3
                width = widthPixels * 3
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

    private fun initBottomBarButtonsOnClicks() =
        with(binding) {
            menuButton.setOnClickListener {
                itemsPickingBottomSheetController.show(navigationBarHeight)
            }

            consoleButton.setOnClickListener {
                showConsole()
            }

            startButton.setOnClickListener {
                codeField.calculateBlocksHierarchyIds()

                (codeField.getEntryPointBlock().block as? StartBlock)?.let {
                    viewModel.executeCode(it)
                }
            }

            stopButton.setOnClickListener {
                viewModel.stopCodeExecution()
            }
        }

    private fun initConsoleBottomSheet() {
        consoleBottomSheetController = ConsoleBottomSheetController(
            binding.consoleBottomSheet
        )
    }

    private fun showStartStopButton(isStopDisplaying: Boolean) {
        val viewToHide: ImageButton
        val viewToShow: ImageButton

        if (isStopDisplaying) {
            viewToHide = binding.stopButton
            viewToShow = binding.startButton
        } else {
            viewToHide = binding.startButton
            viewToShow = binding.stopButton
        }

        val firstAnimation =
            ObjectAnimator.ofFloat(viewToHide, "alpha", 1f, 0f).apply {
                duration = BUTTONS_ANIMATION_DURATION
            }

        firstAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                viewToHide.visibility = View.GONE

                viewToShow.visibility = View.VISIBLE
                val secondAnimation =
                    ObjectAnimator.ofFloat(viewToShow, "alpha", 0f, 1f).apply {
                        duration = BUTTONS_ANIMATION_DURATION
                    }

                secondAnimation.start()
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationRepeat(p0: Animator?) {}

        })

        firstAnimation.start()
    }

    private fun initIsCodeExecutingObserver() =
        viewModel.isCodeExecuting.observe(this) {
            showStartStopButton(!it)
        }

    private fun initIsConsoleInputAvailableObserver() =
        viewModel.isConsoleInputAvailable.observe(this) {
            if (it) {
                showConsole()
            }
        }

    private fun updateParentLayoutMargins(left: Int, top: Int, right: Int, bottom: Int) {
        binding.coordinatorLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            setMargins(left, top, right, bottom)
        }
    }

    private fun showConsole() = consoleBottomSheetController.show(navigationBarHeight)

    private fun initCodeExecutionResultObserver() =
        viewModel.codeExecutionResult.observe(this) {
            if (it == null) {
                codeField.hideError()
            } else {
                codeField.displayError(it.blockID)
            }
        }

    private fun initTrashButtonDragAndDropListener() =
        binding.bottomBarContentLayout.setOnDragListener { _, dragEvent ->
            val draggableItem = dragEvent?.localState as View

            val itemParent = draggableItem.parent as? ViewGroup

            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_LOCATION,
                DragEvent.ACTION_DRAG_ENDED ->
                    return@setOnDragListener true

                DragEvent.ACTION_DRAG_STARTED -> {
                    scaleMinusAnimation(binding.trashButton)

                    return@setOnDragListener true
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    scalePlusAnimation(binding.trashButton)

                    return@setOnDragListener true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    scaleMinusAnimation(binding.trashButton)

                    return@setOnDragListener true
                }

                DragEvent.ACTION_DROP -> {
                    itemParent?.let {
                        processViewWithCustomRemoveProcessRemoval(it, draggableItem)
                        it.removeView(draggableItem)
                    }

                    return@setOnDragListener true
                }

                else -> {
                    return@setOnDragListener false
                }
            }
        }

    override fun showTypeChangingBottomSheet(closureToInvoke: (VariableType, Boolean) -> Unit) =
        typeChangerBottomSheetController.show(closureToInvoke, navigationBarHeight)

    override fun hideTypeChangerBottomSheet() =
        typeChangerBottomSheetController.hide()

    override fun startDeleting() {
        val animationsArray = ArrayList<ObjectAnimator>()

        binding.bottomButtonsGroup.referencedIds.forEach {
            animationsArray.add(ObjectAnimator.ofFloat(findViewById(it), "alpha", 1f, 0f).apply {
                duration = BUTTONS_ANIMATION_DURATION
            })
        }

        animationSet.apply {
            playTogether(animationsArray as Collection<Animator>?)

            start()

            if (viewModel.isCodeExecuting.value == false) {
                binding.stopButton.visibility = View.GONE
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    binding.bottomButtonsGroup.visibility = View.GONE

                    binding.trashButton.visibility = View.VISIBLE

                    val trashButtonShowAnimation =
                        ObjectAnimator.ofFloat(binding.trashButton, "alpha", 0f, 1f).apply {
                            duration = BUTTONS_ANIMATION_DURATION
                        }

                    trashButtonShowAnimation.start()
                }

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationRepeat(p0: Animator?) {}
            })
        }
    }

    override fun stopDeleting() {
        binding.trashButton.visibility = View.VISIBLE

        val trashButtonHideAnimation =
            ObjectAnimator.ofFloat(binding.trashButton, "alpha", 1f, 0f).apply {
                duration = BUTTONS_ANIMATION_DURATION
            }
        trashButtonHideAnimation.start()

        binding.bottomButtonsGroup.visibility = View.VISIBLE
        binding.trashButton.visibility = View.GONE

        val animationsArray = ArrayList<ObjectAnimator>()
        binding.bottomButtonsGroup.referencedIds.forEach {
            animationsArray.add(
                ObjectAnimator.ofFloat(
                    findViewById(it),
                    "alpha",
                    0f,
                    1f
                ).apply {
                    duration = BUTTONS_ANIMATION_DURATION
                })
        }

        AnimatorSet().apply {
            playTogether(animationsArray as Collection<Animator>?)
            start()
        }

        if (viewModel.isCodeExecuting.value == true) {
            binding.startButton.visibility = View.GONE
        } else {
            binding.stopButton.visibility = View.GONE
        }
    }

    private companion object {
        const val BUTTONS_ANIMATION_DURATION: Long = 200
    }
}
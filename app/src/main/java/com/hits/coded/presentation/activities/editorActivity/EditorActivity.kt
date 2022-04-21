package com.hits.coded.presentation.activities.editorActivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hits.coded.R
import com.hits.coded.databinding.ActivityEditorBinding


class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()

        initSystemBarsDimensionChangesListener()
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
        val recalculatedBarHeight = openedTopBarHeight + statusBarHeight

        with(binding){
            topBarWrapper.layoutParams.height = recalculatedBarHeight
            topBarContentLayout.layoutParams.height = openedTopBarHeight
        }
    }

    private fun changeBottomBarHeight(bottomBarHeight: Int) {
        val openedBottomBarHeight = resources.getDimension(R.dimen.openedBottomBarHeight).toInt()
        val recalculatedHeight = openedBottomBarHeight + bottomBarHeight

        with(binding){
            bottomBarWrapper.layoutParams.height = recalculatedHeight
            bottomBarContentLayout.layoutParams.height = openedBottomBarHeight
        }
    }
}
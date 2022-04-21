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

        recalculateBarsHeights()
    }

    private fun initBinding() {
        binding = ActivityEditorBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun recalculateBarsHeights() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _: View?, insets: WindowInsetsCompat ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navigationBarHeight =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            recalculateTopBarHeight(statusBarHeight)
            recalculateBottomBarHeight(navigationBarHeight)

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun recalculateTopBarHeight(statusBarHeight: Int) {
        val recalculatedHeight =
            resources.getDimension(R.dimen.openedTopBarHeight) + statusBarHeight
        binding.topBarWrapper.layoutParams.height = recalculatedHeight.toInt()
    }

    private fun recalculateBottomBarHeight(bottomBarHeight: Int) {
        val recalculatedHeight =
            resources.getDimension(R.dimen.openedBottomBarHeight) + bottomBarHeight
        binding.bottomBarWrapper.layoutParams.height = recalculatedHeight.toInt()
    }
}
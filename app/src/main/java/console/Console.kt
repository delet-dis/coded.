package console

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import com.hits.coded.R

/**
 * Console like output view, which allows writing via static console methods
 * from anywhere of application.
 * If you want to see the output, you should use console in any of your layouts,
 * all calls to console static write methods will affect all instantiated consoles.
 */
class Console @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        @JvmStatic
        fun write(spannableString: SpannableStringBuilder?) {

            controller.write(spannableString)
        }

        internal val controller = ConsoleController()
    }

    val textView: TextView
    val scrollView: ScrollView

    val text: CharSequence
        get() = textView.text.toString()

    init {
        controller.set(this)

        LayoutInflater.from(context).inflate(R.layout.console_content, this)

        textView = findViewById(R.id.console_text)
        scrollView = findViewById(R.id.console_scroll_view)
    }

}

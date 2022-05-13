package console

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TextView
import com.hits.coded.R
import com.jraska.console.ConsoleController
import com.jraska.console.FlingProperty
import com.jraska.console.UserTouchingListener

/**
 * Console like output view, which allows writing via static console methods
 * from anywhere of application.
 * If you want to see the output, you should use console in any of your layouts,
 * all calls to console static write methods will affect all instantiated consoles.
 */
open class Console @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {

        @JvmStatic
        fun writeLine() {
            controller.writeLine()
        }

        /**
         * Write provided object String representation to console and starts new line
         * "null" is written if the object is null
         *
         * @param o Object to write
         */
        @JvmStatic
        fun writeLine(o: Any?) {
            controller.writeLine(o)
        }

        /**
         * Write Spannable to console and starts new line
         * "null" is written if the object is null
         *
         * @param spannableString SpannableString to write
         */
        @JvmStatic
        fun writeLine(spannableString: SpannableString?) {
            controller.writeLine(spannableString)
        }

        /**
         * Write provided object String representation to console
         * "null" is written if the object is null
         *
         * @param o Object to write
         */
        @JvmStatic
        fun write(o: Any?) {
            controller.write(o)
        }

        /**
         * Write SpannableString to the console
         * "null" is written if the object is null
         *
         * @param spannableString SpannableString to write
         */
        @JvmStatic
        fun write(spannableString: SpannableString?) {
            controller.write(spannableString)
        }

        /**
         * Clears the console text
         */
        @JvmStatic
        fun clear() {
            controller.clear()
        }

        @JvmStatic
        fun consoleCount(): Int {
            return controller.size()
        }

        internal val controller = ConsoleController()
    }

    private val textView: TextView
    private val scrollView: ScrollView
    private val userTouchingListener = UserTouchingListener()
    private val flingProperty: FlingProperty
    private val scrollDownRunnable = Runnable { scrollFullDown() }

    val text: CharSequence
        get() = textView.text.toString()

    // Fields are used to not schedule more than one runnable for scroll down
    private var fullScrollScheduled: Boolean = false

    private fun isUserInteracting(): Boolean {
        return userTouchingListener.isUserTouching || flingProperty.isFlinging
    }

    init {
        controller.add(this)

        LayoutInflater.from(context).inflate(R.layout.console_content, this)

        textView = findViewById(R.id.console_text)
        scrollView = findViewById(R.id.console_scroll_view)
        flingProperty = FlingProperty.create(scrollView)
        scrollView.setOnTouchListener(userTouchingListener)

        printBuffer()
        // need to have extra post here, because scroll view is fully initialized after another frame
        post { scrollDown() }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        fullScrollScheduled = false
    }

    internal fun printScroll() {
        printBuffer()
        scrollDown()
    }

    private fun printBuffer() {
        controller.printTo(textView)
    }

    private fun scrollDown() {
        if (!isUserInteracting() && !fullScrollScheduled) {
            post(scrollDownRunnable)
            fullScrollScheduled = true
        }
    }

    private fun scrollFullDown() {
        scrollView.fullScroll(View.FOCUS_DOWN)
    }
}

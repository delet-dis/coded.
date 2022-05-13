package console

import android.text.SpannableStringBuilder

internal class ConsoleController {

    var console: Console? = null
    private val lock = Any()

    fun set(console: Console) {
        this.console = console
    }

    fun write(spannableString: SpannableStringBuilder?) {

        console?.let { console ->
            synchronized(lock) {
                console.textView.post {
                    console.textView.text = spannableString
                }
            }

            console.scrollView.post {
                console.scrollView.scrollTo(0, console.scrollView.height)
            }
        }
    }

}

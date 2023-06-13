import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class SetSearchViewTextAction(private val text: String) : ViewAction {
    override fun getDescription(): String {
        return "Change view text"
    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isAssignableFrom(SearchView::class.java)
    }

    override fun perform(uiController: UiController?, view: View?) {
        (view as SearchView).setQuery(text, false)
    }
}

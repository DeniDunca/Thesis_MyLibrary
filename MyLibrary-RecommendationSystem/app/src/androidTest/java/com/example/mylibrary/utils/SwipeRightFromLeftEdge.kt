package com.example.mylibrary.utils
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*
import androidx.test.espresso.matcher.ViewMatchers

class SwipeRightFromLeftEdge : ViewAction {

    override fun getDescription(): String {
        return "Swipe right from the leftmost point of the screen"
    }

    override fun getConstraints(): org.hamcrest.Matcher<View> {
        return ViewMatchers.isDisplayed()
    }

    override fun perform(uiController: UiController?, view: View?) {
        val startCoordinates = calculateStartCoordinates(view)
        val endCoordinates = calculateEndCoordinates(view)

        val swipeAction = GeneralSwipeAction(
            Swipe.FAST,
            { startCoordinates },
            { endCoordinates },
            Press.FINGER
        )

        swipeAction.perform(uiController, view)
    }

    private fun calculateStartCoordinates(view: View?): FloatArray {
        val screenPos = IntArray(2)
        view?.getLocationOnScreen(screenPos)
        val startX = screenPos[0].toFloat()
        val startY = screenPos[1] + view?.height?.div(2)?.toFloat()!!
        return floatArrayOf(startX, startY)
    }

    private fun calculateEndCoordinates(view: View?): FloatArray {
        val screenPos = IntArray(2)
        view?.getLocationOnScreen(screenPos)
        val endX = screenPos[0].toFloat() + view?.width?.toFloat()!!
        val endY = screenPos[1] + view?.height?.div(2)?.toFloat()!!
        return floatArrayOf(endX, endY)
    }
}

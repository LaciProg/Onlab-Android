package hu.bme.aut.android.examapp.ui

interface ExamDestination {
    val route: String
}

object ScreenFirst : ExamDestination{
 override val route: String = "First"
}

object ScreenSecond : ExamDestination{
    override val route: String = "Second"
}
object ScreenThird : ExamDestination{
    override val route: String = "Third"
}
object ScreenFourth : ExamDestination{
    override val route: String = "Fourth"
}
object ScreenFive : ExamDestination{
    override val route: String = "Fifth"
}

/*
sealed class ExamDestination(val route: String) {
    data object ScreenFirst : ExamDestination("ScreenFirst")
    data object ScreenSecond : ExamDestination("ScreenSecond")
    data object ScreenThird : ExamDestination("ScreenThird")
    data object ScreenFourth : ExamDestination("ScreenFourth")
    data object ScreenFive : ExamDestination("ScreenFive")

    val examTabRowScreens = listOf(ScreenFirst, ScreenSecond, ScreenThird, ScreenFourth, ScreenFive)
}*/

val examTabRowScreens = listOf(ScreenFirst, ScreenSecond, ScreenThird, ScreenFourth, ScreenFive)
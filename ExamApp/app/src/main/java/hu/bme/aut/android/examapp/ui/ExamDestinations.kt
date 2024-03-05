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
    override val route: String = "Exit"
}
object ScreenFive : ExamDestination{
    override val route: String = "Fifth"
}

object NewTopic : ExamDestination{
    override val route: String = "NewTopic"
}

object TopicDetails : ExamDestination{
    override val route: String = "TopicDetails"
    const val topicNameArg = "itemId"
    val routeWithArgs = "$route/{$topicNameArg}"
}

val examTabRowScreens = listOf(ScreenFirst, ScreenSecond, ScreenThird, ScreenFourth, ScreenFive)

/*
open class ExamDestination(val route: String = "ScreenFirst") {
    object ScreenFirst : ExamDestination("ScreenFirst")
    object ScreenSecond : ExamDestination("ScreenSecond")
    object ScreenThird : ExamDestination("ScreenThird")
    object ScreenFourth : ExamDestination("ScreenFourth")
    object ScreenFive : ExamDestination("ScreenFive")
    object NewTopic : ExamDestination("NewTopic")

    companion object {
        val examTabRowScreens = listOf(ScreenFirst, ScreenSecond, ScreenThird, ScreenFourth, ScreenFive)
    }
    //val examTabRowScreens = listOf(ScreenFirst, ScreenSecond, ScreenThird, ScreenFourth, ScreenFive)
}*/


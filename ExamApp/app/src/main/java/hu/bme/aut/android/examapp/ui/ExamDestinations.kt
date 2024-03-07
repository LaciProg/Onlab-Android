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

object MainScreenDestination : ExamDestination {
    override val route = "MainScreen"
}

object TopicListDestination : ExamDestination {
    override val route = "TopicList"
}

object TopicDetailsDestination : ExamDestination{
    override val route: String = "TopicDetails"
    const val topicNameArg = "topicName"
    val routeWithArgs = "$route/{$topicNameArg}"
}

object TopicEditDestination : ExamDestination {
    override val route = "TopicEdit"
    const val topicNameArg = "topicName"
    val routeWithArgs = "$route/{$topicNameArg}"
}

object NewTopicDestination : ExamDestination{
    override val route: String = "NewTopic"
}

object PointListDestination : ExamDestination {
    override val route = "PointList"
}

object PointDetailsDestination : ExamDestination{
    override val route: String = "PointDetails"
    const val pointNameArg = "pointName"
    val routeWithArgs = "$route/{$pointNameArg}"
}

object PointEditDestination : ExamDestination {
    override val route = "PointEdit"
    const val pointNameArg = "pointName"
    val routeWithArgs = "$route/{$pointNameArg}"
}

object NewPointDestination : ExamDestination{
    override val route: String = "NewPoint"
}


val examTabRowScreens = listOf(MainScreenDestination, ScreenSecond, ScreenThird, ScreenFourth, ScreenFive)

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


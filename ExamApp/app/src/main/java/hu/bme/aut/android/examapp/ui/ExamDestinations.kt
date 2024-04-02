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
    const val topicIdArg = 0
    val routeWithArgs = "$route/{$topicIdArg}"
}

object TopicEditDestination : ExamDestination {
    override val route = "TopicEdit"
    const val topicIdArg = 0
    val routeWithArgs = "$route/{$topicIdArg}"
}

object NewTopicDestination : ExamDestination{
    override val route: String = "NewTopic"
}

object PointListDestination : ExamDestination {
    override val route = "PointList"
}

object PointDetailsDestination : ExamDestination{
    override val route: String = "PointDetails"
    const val pointIdArg = 0
    val routeWithArgs = "$route/{$pointIdArg}"
}

object PointEditDestination : ExamDestination {
    override val route = "PointEdit"
    const val pointIdArg = 0
    val routeWithArgs = "$route/{$pointIdArg}"
}

object NewPointDestination : ExamDestination{
    override val route: String = "NewPoint"
}

object TrueFalseQuestionListDestination : ExamDestination {
    override val route = "TrueFalseQuestionList"
}

object TrueFalseQuestionDetailsDestination : ExamDestination{
    override val route: String = "TrueFalseQuestionDetails"
    const val trueFalseQuestionIdArg = 0
    val routeWithArgs = "$route/{$trueFalseQuestionIdArg}"
}

object TrueFalseQuestionEditDestination : ExamDestination {
    override val route = "TrueFalseQuestionEdit"
    const val trueFalseQuestionIdArg = 0
    val routeWithArgs = "$route/{$trueFalseQuestionIdArg}"
}

object NewTrueFalseQuestionDestination : ExamDestination{
    override val route: String = "NewTrueFalseQuestion"
}

object MultipleChoiceQuestionListDestination : ExamDestination {
    override val route = "MultipleChoiceQuestionList"
}

object MultipleChoiceQuestionDetailsDestination : ExamDestination{
    override val route: String = "MultipleChoiceQuestionDetails"
    const val multipleChoiceQuestionIdArg = 0
    val routeWithArgs = "$route/{$multipleChoiceQuestionIdArg}"
}

object MultipleChoiceQuestionEditDestination : ExamDestination {
    override val route = "MultipleChoiceQuestionEdit"
    const val multipleChoiceQuestionIdArg = 0
    val routeWithArgs = "$route/{$multipleChoiceQuestionIdArg}"
}

object NewMultipleChoiceQuestionDestination : ExamDestination{
    override val route: String = "MultipleChoiceQuestion"
}


object ExamListDestination : ExamDestination {
    override val route = "ExamList"
}

object ExamDetailsDestination : ExamDestination{
    override val route: String = "ExamDetails"
    const val examIdArg = 0
    val routeWithArgs = "$route/{$examIdArg}"
}

object ExamEditDestination : ExamDestination {
    override val route = "ExamEdit"
    const val examIdArg = 0
    val routeWithArgs = "$route/{$examIdArg}"
}

object NewExamDestination : ExamDestination{
    override val route: String = "ExamQuestion"
}


object ExportExamListDestination : ExamDestination {
    override val route = "ExportExamList"
}

object ExportExamDetailsDestination : ExamDestination{
    override val route: String = "ExportExamDetails"
    const val examIdArg = 0
    val routeWithArgs = "$route/{$examIdArg}"
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


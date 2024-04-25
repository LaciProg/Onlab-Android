package hu.bme.aut.android.examapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.get
import androidx.navigation.navArgument
import hu.bme.aut.android.examapp.MainScreen
import hu.bme.aut.android.examapp.ui.auth.LoginScreen
import hu.bme.aut.android.examapp.ui.auth.RegisterScreen
import hu.bme.aut.android.examapp.ui.exam.ExamDetailsScreen
import hu.bme.aut.android.examapp.ui.exam.ExamEditScreen
import hu.bme.aut.android.examapp.ui.exam.ExamListScreen
import hu.bme.aut.android.examapp.ui.exam.ExportExamDetailsScreen
import hu.bme.aut.android.examapp.ui.exam.NewExamScreen
import hu.bme.aut.android.examapp.ui.multiplechoicequestion.MultipleChoiceQuestionDetailsScreen
import hu.bme.aut.android.examapp.ui.multiplechoicequestion.MultipleChoiceQuestionEditScreen
import hu.bme.aut.android.examapp.ui.multiplechoicequestion.MultipleChoiceQuestionListScreen
import hu.bme.aut.android.examapp.ui.multiplechoicequestion.NewMultipleChoiceQuestionScreen
import hu.bme.aut.android.examapp.ui.point.NewPoint
import hu.bme.aut.android.examapp.ui.point.PointDetailsScreen
import hu.bme.aut.android.examapp.ui.point.PointEditScreen
import hu.bme.aut.android.examapp.ui.point.PointListScreen
import hu.bme.aut.android.examapp.ui.topic.NewTopic
import hu.bme.aut.android.examapp.ui.topic.TopicDetailsScreen
import hu.bme.aut.android.examapp.ui.topic.TopicEditScreen
import hu.bme.aut.android.examapp.ui.topic.TopicListScreen
import hu.bme.aut.android.examapp.ui.truefalsequestion.NewTrueFalseQuestionScreen
import hu.bme.aut.android.examapp.ui.truefalsequestion.TrueFalseQuestionDetailsScreen
import hu.bme.aut.android.examapp.ui.truefalsequestion.TrueFalseQuestionEditScreen
import hu.bme.aut.android.examapp.ui.truefalsequestion.TrueFalseQuestionListScreen

@Composable
fun ExamNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ExamDestination.LoginScreenDestination.route,
        modifier = modifier
    ) {

        composable(ExamDestination.LoginScreenDestination.route) {
            LoginScreen(
                onSuccess = {
                    navController.navigate(ExamDestination.MainScreenDestination.route)
                },
                onRegisterClick = {
                    navController.navigate(ExamDestination.RegisterScreenDestination.route)
                }
            )
        }
        composable(ExamDestination.RegisterScreenDestination.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack(
                        route = ExamDestination.LoginScreenDestination.route,
                        inclusive = true
                    )
                    navController.navigate(ExamDestination.LoginScreenDestination.route)
                },
                onSuccess = {
                    navController.navigate(ExamDestination.MainScreenDestination.route)
                }
            )
        }


        composable(
            route = ExamDestination.MainScreenDestination.route
        ){
            MainScreen(
                navigateToTopicList = { navController.navigateSingleTopTo(ExamDestination.TopicListDestination.route) },
                navigateToPointList = { navController.navigateSingleTopTo(ExamDestination.PointListDestination.route) },
                navigateToTrueFalseQuestionList = { navController.navigateSingleTopTo(
                    ExamDestination.TrueFalseQuestionListDestination.route) },
                navigateToMultipleChoiceQuestionList = {navController.navigateSingleTopTo(
                    ExamDestination.MultipleChoiceQuestionListDestination.route)},
                navigateToExamList = {navController.navigateSingleTopTo(ExamDestination.ExamListDestination.route)},
                navigateToExportExamList = {navController.navigateSingleTopTo(ExamDestination.ExportExamListDestination.route)},
                onSignOut = {
                    navController.popBackStack(
                        route = ExamDestination.LoginScreenDestination.route,
                        inclusive = true
                    )
                    navController.navigate(ExamDestination.LoginScreenDestination.route)
                }
            )
        }

        composable(
            route = /*ExamDestination.*/ExamDestination.TopicListDestination.route,
        ) {
            TopicListScreen(
                addNewTopic = { navController.navigate(/*ExamDestination.*/ExamDestination.NewTopicDestination.route) },
                navigateToTopicDetails = { topicId ->
                    navController.navigate(/*ExamDestination.*/"${ExamDestination.TopicDetailsDestination.route}/${topicId}")
                }
            )
        }

        composable(
            route = ExamDestination.TopicDetailsDestination.routeWithArgs
        ) {
            TopicDetailsScreen(
                navigateToEditTopic = { navController.navigate("${ExamDestination.TopicEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ExamDestination.TopicEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ExamDestination.TopicEditDestination.topicIdArg) {
                type = NavType.StringType
            })
        ) {
            TopicEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = /*ExamDestination.*/ExamDestination.NewTopicDestination.route
        ) {
            NewTopic(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(
            route = /*ExamDestination.*/ExamDestination.PointListDestination.route,
        ) {
            PointListScreen(
                addNewPoint = { navController.navigate(/*ExamDestination.*/ExamDestination.NewPointDestination.route) },
                navigateToPointDetails = { pointId ->
                    navController.navigate(/*ExamDestination.*/"${ExamDestination.PointDetailsDestination.route}/$pointId")
                }
            )
        }

        composable(
            route = ExamDestination.PointDetailsDestination.routeWithArgs,
        ) {
            PointDetailsScreen(
                navigateToEditPoint = { navController.navigate("${ExamDestination.PointEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ExamDestination.PointEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ExamDestination.PointEditDestination.pointIdArg) {
                type = NavType.StringType
            })
        ) {
            PointEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = /*ExamDestination.*/ExamDestination.NewPointDestination.route
        ) {
            NewPoint(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }


        composable(
            route = ExamDestination.TrueFalseQuestionListDestination.route
        ) {
            TrueFalseQuestionListScreen(
                addNewTrueFalseQuestion = { navController.navigate(ExamDestination.NewTrueFalseQuestionDestination.route) },
                navigateToTrueFalseQuestionDetails = { navController.navigate("${ExamDestination.TrueFalseQuestionDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = ExamDestination.TrueFalseQuestionDetailsDestination.routeWithArgs
        ){
            TrueFalseQuestionDetailsScreen(
                navigateToEditTrueFalseQuestion = {navController.navigate("${ExamDestination.TrueFalseQuestionEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ExamDestination.TrueFalseQuestionEditDestination.routeWithArgs,
        ) {
            TrueFalseQuestionEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }


        composable(
            route = /*ExamDestination.*/ExamDestination.NewTrueFalseQuestionDestination.route
        ) {
            NewTrueFalseQuestionScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }


        composable(
            route = ExamDestination.MultipleChoiceQuestionListDestination.route
        ) {
            MultipleChoiceQuestionListScreen(
                addNewMultipleChoiceQuestion = { navController.navigate(ExamDestination.NewMultipleChoiceQuestionDestination.route) },
                navigateToMultipleChoiceQuestionDetails = { navController.navigate("${ExamDestination.MultipleChoiceQuestionDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = ExamDestination.MultipleChoiceQuestionDetailsDestination.routeWithArgs
        ){
            MultipleChoiceQuestionDetailsScreen(
                navigateToEditMultipleChoiceQuestion = {navController.navigate("${ ExamDestination.MultipleChoiceQuestionEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route =  ExamDestination.MultipleChoiceQuestionEditDestination.routeWithArgs,
        ) {
            MultipleChoiceQuestionEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }


        composable(
            route = ExamDestination.NewMultipleChoiceQuestionDestination.route
        ) {
            NewMultipleChoiceQuestionScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }





        composable(
            route = ExamDestination.ExamListDestination.route
        ) {
            ExamListScreen(
                addNewExam = { navController.navigate(ExamDestination.NewExamDestination.route) },
                navigateToExamDetails = { navController.navigate("${ ExamDestination.ExamDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = ExamDestination.ExamDetailsDestination.routeWithArgs
        ){
            ExamDetailsScreen(
                navigateToEditMultipleChoiceQuestion = {navController.navigate("${ ExamDestination.ExamEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route =  ExamDestination.ExamEditDestination.routeWithArgs,
        ) {
            ExamEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }


        composable(
            route = ExamDestination.NewExamDestination.route
        ) {
            NewExamScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }



        composable(
            route = ExamDestination.ExportExamListDestination.route
        ) {
            ExamListScreen(
                addNewExam = { navController.navigate(ExamDestination.NewExamDestination.route) },
                navigateToExamDetails = { navController.navigate("${ ExamDestination.ExportExamDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = ExamDestination.ExportExamDetailsDestination.routeWithArgs
        ){
            ExportExamDetailsScreen(
                //navigateToEditMultipleChoiceQuestion = {navController.navigate("${ ExamEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph[ExamDestination.MainScreenDestination.route].id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
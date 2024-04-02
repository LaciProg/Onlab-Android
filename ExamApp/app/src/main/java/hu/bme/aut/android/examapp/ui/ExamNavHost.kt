/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.bme.aut.android.examapp.ui


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hu.bme.aut.android.examapp.DefaultMulti
import hu.bme.aut.android.examapp.DefaultTrueFalse
import hu.bme.aut.android.examapp.MainScreen
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
import hu.bme.aut.android.examapp.ui.topic.TopicListScreen
import hu.bme.aut.android.examapp.ui.topic.TopicEditScreen
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
        startDestination = /*ExamDestination.*/MainScreenDestination.route,
        modifier = modifier
    ) {
        composable(
            route = MainScreenDestination.route
        ){
            MainScreen(
                navigateToTopicList = { navController.navigateSingleTopTo(TopicListDestination.route) },
                navigateToPointList = { navController.navigateSingleTopTo(PointListDestination.route) },
                navigateToTrueFalseQuestionList = { navController.navigateSingleTopTo(TrueFalseQuestionListDestination.route) },
                navigateToMultipleChoiceQuestionList = {navController.navigateSingleTopTo(MultipleChoiceQuestionListDestination.route)},
                navigateToExamList = {navController.navigateSingleTopTo(ExamListDestination.route)},
                navigateToExportExamList = {navController.navigateSingleTopTo(ExportExamListDestination.route)},
            )
        }

        composable(
            route = /*ExamDestination.*/TopicListDestination.route,
        ) {
            TopicListScreen(
                addNewTopic = { navController.navigate(/*ExamDestination.*/NewTopicDestination.route) },
                navigateToTopicDetails = { topicId ->
                    navController.navigate(/*ExamDestination.*/"${TopicDetailsDestination.route}/$topicId")
                }
            )
        }

        composable(
            route = TopicDetailsDestination.routeWithArgs
        ) {
            TopicDetailsScreen(
                navigateToEditTopic = { navController.navigate("${TopicEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = TopicEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TopicEditDestination.topicIdArg.toString()) {
                type = NavType.StringType
            })
        ) {
            TopicEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = /*ExamDestination.*/NewTopicDestination.route
        ) {
            NewTopic(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }

        composable(
            route = /*ExamDestination.*/PointListDestination.route,
        ) {
            PointListScreen(
                addNewPoint = { navController.navigate(/*ExamDestination.*/NewPointDestination.route) },
                navigateToPointDetails = { pointId ->
                    navController.navigate(/*ExamDestination.*/"${PointDetailsDestination.route}/$pointId")
                }
            )
        }

        composable(
            route = PointDetailsDestination.routeWithArgs
        ) {
            PointDetailsScreen(
                navigateToEditPoint = { navController.navigate("${PointEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = PointEditDestination.routeWithArgs,
            arguments = listOf(navArgument(PointEditDestination.pointIdArg.toString()) {
                type = NavType.StringType
            })
        ) {
            PointEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = /*ExamDestination.*/NewPointDestination.route
        ) {
            NewPoint(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }


        composable(
            route = TrueFalseQuestionListDestination.route
        ) {
            TrueFalseQuestionListScreen(
                addNewTrueFalseQuestion = { navController.navigate(/*ExamDestination.*/NewTrueFalseQuestionDestination.route) },
                navigateToTrueFalseQuestionDetails = { navController.navigate("${TrueFalseQuestionDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = TrueFalseQuestionDetailsDestination.routeWithArgs
        ){
            TrueFalseQuestionDetailsScreen(
                navigateToEditTrueFalseQuestion = {navController.navigate("${TrueFalseQuestionEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = TrueFalseQuestionEditDestination.routeWithArgs,
        ) {
            TrueFalseQuestionEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }


        composable(
            route = /*ExamDestination.*/NewTrueFalseQuestionDestination.route
        ) {
            NewTrueFalseQuestionScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }


        composable(
            route = MultipleChoiceQuestionListDestination.route
        ) {
            MultipleChoiceQuestionListScreen(
                addNewMultipleChoiceQuestion = { navController.navigate(/*ExamDestination.*/NewMultipleChoiceQuestionDestination.route) },
                navigateToMultipleChoiceQuestionDetails = { navController.navigate("${ MultipleChoiceQuestionDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = MultipleChoiceQuestionDetailsDestination.routeWithArgs
        ){
            MultipleChoiceQuestionDetailsScreen(
                navigateToEditMultipleChoiceQuestion = {navController.navigate("${ MultipleChoiceQuestionEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route =  MultipleChoiceQuestionEditDestination.routeWithArgs,
            /*arguments = listOf(navArgument(PointEditDestination.pointIdArg.toString()) {
                type = NavType.StringType
            })*/
        ) {
            MultipleChoiceQuestionEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }


        composable(
            route = /*ExamDestination.*/NewMultipleChoiceQuestionDestination.route
        ) {
            NewMultipleChoiceQuestionScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }





        composable(
            route = ExamListDestination.route
        ) {
            ExamListScreen(
                addNewExam = { navController.navigate(/*ExamDestination.*/NewExamDestination.route) },
                navigateToExamDetails = { navController.navigate("${ ExamDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = ExamDetailsDestination.routeWithArgs
        ){
            ExamDetailsScreen(
                navigateToEditMultipleChoiceQuestion = {navController.navigate("${ ExamEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route =  ExamEditDestination.routeWithArgs,
            /*arguments = listOf(navArgument(PointEditDestination.pointIdArg.toString()) {
                type = NavType.StringType
            })*/
        ) {
            ExamEditScreen(
                navigateBack = { navController.popBackStack() },
                //onNavigateUp = { navController.navigateUp() }
            )
        }


        composable(
            route = /*ExamDestination.*/NewExamDestination.route
        ) {
            NewExamScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }



        composable(
            route = ExportExamListDestination.route
        ) {
            ExamListScreen(
                addNewExam = { navController.navigate(/*ExamDestination.*/NewExamDestination.route) },
                navigateToExamDetails = { navController.navigate("${ ExportExamDetailsDestination.route}/$it") }
            )
        }


        composable(
            route = ExportExamDetailsDestination.routeWithArgs
        ){
            ExportExamDetailsScreen(
                //navigateToEditMultipleChoiceQuestion = {navController.navigate("${ ExamEditDestination.route}/$it") },
                navigateBack = { navController.popBackStack() }
            )
        }



        composable(route = /*ExamDestination.*/ScreenSecond.route) {
            DefaultTrueFalse(
                /*onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }*/
            )
        }
        composable(route = /*ExamDestination.*/ScreenThird.route) {
            DefaultMulti()
        }





    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
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
import androidx.navigation.compose.composable
import hu.bme.aut.android.examapp.DefaultPreview
import hu.bme.aut.android.examapp.Greeting
import hu.bme.aut.android.examapp.ui.components.TrueFalseQuestion

@Composable
fun ExamNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = /*ExamDestination.*/ScreenFirst.route,
        modifier = modifier
    ) {
        composable(route = /*ExamDestination.*/ScreenFirst.route) {
            Greeting(name = "Laci"
                /*onClickSeeAllAccounts = {
                    navController.navigateSingleTopTo(ExamDestination.ScreenSecond.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(Bills.route)
                },
                onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }*/
            )
        }
        composable(route = /*ExamDestination.*/ScreenSecond.route) {
            DefaultPreview(
                /*onAccountClick = { accountType ->
                    navController.navigateToSingleAccount(accountType)
                }*/
            )
        }
        composable(route = /*ExamDestination.*/ScreenThird.route) {
            TrueFalseQuestion()
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
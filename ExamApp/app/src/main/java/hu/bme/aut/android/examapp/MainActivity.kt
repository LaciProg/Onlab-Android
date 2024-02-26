package hu.bme.aut.android.examapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.examapp.ui.ExamDestination
//import hu.bme.aut.android.examapp.ui.ExamDestination.ScreenFirst.examTabRowScreens
import hu.bme.aut.android.examapp.ui.ExamNavHost
import hu.bme.aut.android.examapp.ui.ExamTabRow
import hu.bme.aut.android.examapp.ui.ScreenFirst
import hu.bme.aut.android.examapp.ui.components.EditMultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.EditTrueFalseQuestion
import hu.bme.aut.android.examapp.ui.components.MultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.TrueFalseQuestion
import hu.bme.aut.android.examapp.ui.examTabRowScreens
import hu.bme.aut.android.examapp.ui.navigateSingleTopTo
import hu.bme.aut.android.examapp.ui.theme.ExamAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()

           /* // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {

                    TrueFalseQuestion()
                    EditTrueFalseQuestion()
                    MultipleChoiceQuestion()
                    EditMultipleChoiceQuestion()
                }
                //Greeting("Android")
            }
        }*/
        }
    }
}

@Composable
fun DefaultPreview() {
    ExamAppTheme {
        Column {

            TrueFalseQuestion()
            EditTrueFalseQuestion()
            MultipleChoiceQuestion()
            EditMultipleChoiceQuestion()
        }
    }
}

@Composable
fun MainScreen() {
    ExamAppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            examTabRowScreens.find { it.route == currentDestination?.route } ?: /*ExamDestination.*/
            ScreenFirst

        Scaffold(
            topBar = {
                ExamTabRow(
                    allScreens = examTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            ExamNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    ExamAppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            examTabRowScreens.find { it.route == currentDestination?.route } ?: /*ExamDestination.*/ScreenFirst

        Scaffold(
            topBar = {
                ExamTabRow(
                    allScreens = examTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            ExamNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExamAppTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun TrueFalseQuestionPreview() {
    ExamAppTheme {
        Column {

            TrueFalseQuestion()
            EditTrueFalseQuestion()
            MultipleChoiceQuestion()
            EditMultipleChoiceQuestion()
        }
    }
}
package hu.bme.aut.android.examapp

import android.graphics.Insets.add
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.ExamNavHost
import hu.bme.aut.android.examapp.ui.ExamTabRow
import hu.bme.aut.android.examapp.ui.ScreenFirst
import hu.bme.aut.android.examapp.ui.ScreenThird
import hu.bme.aut.android.examapp.ui.components.EditMultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.EditTrueFalseQuestion
import hu.bme.aut.android.examapp.ui.components.MultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.TrueFalseQuestion
import hu.bme.aut.android.examapp.ui.navigateSingleTopTo
import hu.bme.aut.android.examapp.ui.examTabRowScreens
import hu.bme.aut.android.examapp.ui.theme.ExamAppTheme
import hu.bme.aut.android.examapp.ui.viewmodel.type.TypeViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", /*ExamDestination.*/ScreenThird.route)
        setContent {
            RegisterQuestionTypes()
            NavigationComponent()
        }
    }
}

@Composable
fun RegisterQuestionTypes(viewModel: TypeViewModel = viewModel(factory = AppViewModelProvider.Factory)){}

@Composable
fun DefaultTrueFalse() {
    ExamAppTheme {
        Column {
            TrueFalseQuestion()
            EditTrueFalseQuestion()
        }
    }

}

@Composable
fun DefaultMulti() {
    ExamAppTheme {
        Column {
            MultipleChoiceQuestion()
            EditMultipleChoiceQuestion()
        }
    }

}

@Composable
fun NavigationComponent() {
    ExamAppTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        Log.d("NavigationComponent", "currentDestination: $currentDestination")
        val currentScreen = //ExamDestination.ScreenFirst
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

@Composable
fun MainScreen(
    navigateToTopicList: () -> Unit,
    navigateToPointList: () -> Unit,
    navigateToTrueFalseQuestionList: () -> Unit,
    navigateToMultipleChoiceQuestionList: () -> Unit,
    navigateToExamList: () -> Unit,
    navigateToExportExamList: () -> Unit
){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = "Welcome to the Exam App!"
        )
        OutlinedButton(
            onClick = { navigateToTopicList() },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.topic))
        }
        OutlinedButton(
            onClick = { navigateToPointList() },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.point))
        }
        OutlinedButton(
            onClick = { navigateToTrueFalseQuestionList() },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.true_false_question))
        }
        OutlinedButton(
            onClick = { navigateToMultipleChoiceQuestionList() },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.multiple_choice_question))
        }
        OutlinedButton(
            onClick = { navigateToExamList() },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.exams))
        }
        OutlinedButton(
            onClick = { navigateToExportExamList() },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.export_exams))
        }
    }
    //VerticalReorderList()
}


//TODO: Remove
@Composable
fun VerticalReorderList() {
    val data = remember { mutableStateOf(List(100) { "Item $it" }) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        data.value = data.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })
    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(data.value, { it }) { item ->
            ReorderableItem(state, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                Column(
                    modifier = Modifier
                        .shadow(elevation.value)
                    //.background(MaterialTheme.colors.surface)
                ) {
                    Text(item)
                }
            }
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
fun MNavigationComponentPreview() {
    NavigationComponent()
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
fun DefaultTrueFalsePreview() {
    DefaultTrueFalse()
}

@Preview(showBackground = true)
@Composable
fun DefaultMultiPreview() {
    DefaultMulti()
}
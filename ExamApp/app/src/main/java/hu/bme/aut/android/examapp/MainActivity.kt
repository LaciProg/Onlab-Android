package hu.bme.aut.android.examapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.ui.ExamNavHost
import hu.bme.aut.android.examapp.ui.theme.ExamAppTheme
import hu.bme.aut.android.examapp.ui.viewmodel.MainScreenViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationComponent()
        }
    }
}

@Composable
fun NavigationComponent() {
    ExamAppTheme {
        val navController = rememberNavController()

        Scaffold() { innerPadding ->
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
    navigateToExportExamList: () -> Unit,
    navigateToSubmission: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel()//viewModel(factory = AppViewModelProvider.Factory),
){
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        try{
            ExamAppApi.authenticate()
        } catch (e: Exception){
            Toast.makeText(
                context,
                "Authentication failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {viewModel.signOut(); onSignOut() },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = stringResource(R.string.log_out),
                )
            }
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
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
                onClick = { navigateToSubmission() },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.submission))
            }
            OutlinedButton(
                onClick = { navigateToExportExamList() },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.export_exams))
            }
        }

    }

}


@Composable
@Preview(showBackground = true)
fun MNavigationComponentPreview() {
    NavigationComponent()
}

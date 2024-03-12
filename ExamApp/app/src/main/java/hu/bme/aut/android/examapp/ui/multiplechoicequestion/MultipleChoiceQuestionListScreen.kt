package hu.bme.aut.android.examapp.ui.multiplechoicequestion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionListViewModel

@Composable
fun MultipleChoiceQuestionListScreen(
    modifier: Modifier = Modifier,
    addNewMultipleChoiceQuestion: () -> Unit = {},
    navigateToMultipleChoiceQuestionDetails: (Int) -> Unit,
    viewModel: MultipleChoiceQuestionListViewModel = viewModel(factory = AppViewModelProvider.Factory)
  ){
    val multipleChoiceQuestionListUiState by viewModel.multipleChoiceQuestionListUiState.collectAsState()
    Scaffold(
        topBar = {
            Column {
                Text("Multiple choice Question list")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.question))
                    Text(stringResource(R.string.answer))
                    Text(stringResource(R.string.topic))
                }
            }

             },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewMultipleChoiceQuestion() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(multipleChoiceQuestionListUiState.multipleChoiceQuestionList){
                Row(
                    modifier = Modifier
                        .clickable { navigateToMultipleChoiceQuestionDetails(it.id) }
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(it.question)
                    Column {
                        it.answers.forEach { answer ->
                            Text(answer)
                        }
                    }
                    Text(it.topic)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultipleChoiceQuestionListScreenPreview() {
    MultipleChoiceQuestionListScreen(
        addNewMultipleChoiceQuestion = {},
        navigateToMultipleChoiceQuestionDetails = {}
    )
}
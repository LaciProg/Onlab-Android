package hu.bme.aut.android.examapp.ui.truefalsequestion

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.ui.components.DropDownList
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetails
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEntryScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionUiState
import kotlinx.coroutines.launch

@Composable
fun NewTrueFalseQuestionScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TrueFalseQuestionEntryViewModel = hiltViewModel()//viewModel(factory = AppViewModelProvider.Factory)
) {
    when (viewModel.trueFalseEntryScreenUiState) {
        TrueFalseQuestionEntryScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        TrueFalseQuestionEntryScreenUiState.Success -> NewTrueFalseQuestionScreenUiState(viewModel, navigateBack)
        TrueFalseQuestionEntryScreenUiState.Error -> Text(text = TrueFalseQuestionEntryScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
    }
}

@Composable
private fun NewTrueFalseQuestionScreenUiState(
    viewModel: TrueFalseQuestionEntryViewModel,
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {}
    ) { innerPadding ->
        TrueFalseQuestionEntryBody(
            trueFalseQuestionUiState = viewModel.trueFalseQuestionUiState,
            onTrueFalseQuestionValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.saveTrueFalseQuestion()) {
                        navigateBack()
                    } else {
                        Toast.makeText(
                            context,
                            "Question already exists",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun TrueFalseQuestionEntryBody(
    trueFalseQuestionUiState: TrueFalseQuestionUiState,
    onTrueFalseQuestionValueChange: (TrueFalseQuestionDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TrueFalseQuestionInputForm(
            trueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails,
            onValueChange = onTrueFalseQuestionValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = trueFalseQuestionUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun TrueFalseQuestionInputForm(
    trueFalseQuestionDetails: TrueFalseQuestionDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TrueFalseQuestionDetails) -> Unit = {},
    enabled: Boolean = true,
    topicListViewModel: TopicListViewModel = hiltViewModel(),//viewModel(factory = AppViewModelProvider.Factory),
    pointListViewModel: PointListViewModel = hiltViewModel()//viewModel(factory = AppViewModelProvider.Factory)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        DropDownList(
            name = stringResource(R.string.topic_name_req),
            items = topicListViewModel.topicListUiState.topicList.map{it.topic}.filterNot{ it == trueFalseQuestionDetails.topicName },
            onChoose = { topicName -> onValueChange(trueFalseQuestionDetails.copy(topicName = topicName, topic = if(topicName.isNotBlank()) topicListViewModel.topicListUiState.topicList.filter { it.topic == topicName }.map{it.id}.first() else "")) },
            default = trueFalseQuestionDetails.topicName,
            modifier = Modifier.fillMaxWidth(),
        )
        DropDownList(
            name = stringResource(R.string.point_req),
            items = pointListViewModel.pointListUiState.pointList.map{it.point}.filterNot{ it == trueFalseQuestionDetails.pointName },
            onChoose = { pointName -> onValueChange(trueFalseQuestionDetails.copy(pointName = pointName, point = if(pointName.isNotBlank()) pointListViewModel.pointListUiState.pointList.filter { it.point ==  pointName}.map{it.id}.first() else "")) },
            default = trueFalseQuestionDetails.pointName,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = trueFalseQuestionDetails.question,
            onValueChange = { onValueChange(trueFalseQuestionDetails.copy(question = it)) },
            label = { Text(stringResource(R.string.question_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Column {
            Text(stringResource(R.string.correct_answer_req))
            TextButton( onClick = { onValueChange(trueFalseQuestionDetails.copy(correctAnswer = true, isAnswerChosen = true)) })
            {
                Text(
                    text = stringResource(R.string.true_action),
                    color = if(!trueFalseQuestionDetails.isAnswerChosen) MaterialTheme.colorScheme.onSurface
                        else if (trueFalseQuestionDetails.correctAnswer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
            TextButton(onClick = { onValueChange(trueFalseQuestionDetails.copy(correctAnswer = false,  isAnswerChosen = true)) }) {
                Text(
                    text = stringResource(R.string.false_action),
                    color = if(!trueFalseQuestionDetails.isAnswerChosen) MaterialTheme.colorScheme.onSurface
                        else if (!trueFalseQuestionDetails.correctAnswer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}
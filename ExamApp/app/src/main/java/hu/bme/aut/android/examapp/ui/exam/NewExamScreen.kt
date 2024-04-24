package hu.bme.aut.android.examapp.ui.exam

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.components.DropDownList
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamDetails
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEntryScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import kotlinx.coroutines.launch

@Composable
fun NewExamScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ExamEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (viewModel.examEntryScreenUiState) {
        ExamEntryScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        ExamEntryScreenUiState.Success ->  NewExamScreenUiState(viewModel, navigateBack)
        ExamEntryScreenUiState.Error -> Text(text = ExamEntryScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
    }
}

@Composable
private fun NewExamScreenUiState(
    viewModel: ExamEntryViewModel,
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = { }
    ) { innerPadding ->
        ExamEntryBody(
            examUiState = viewModel.examUiState,
            onExamValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.saveExam()) {
                        navigateBack()
                    } else {
                        Toast.makeText(
                            context,
                            "Exam with this name already exists",
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
fun ExamEntryBody(
    examUiState: ExamUiState,
    onExamValueChange: (ExamDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ExamInputForm(
            examDetails = examUiState.examDetails,
            onValueChange = onExamValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = examUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun ExamInputForm(
    examDetails: ExamDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ExamDetails) -> Unit = {},
    enabled: Boolean = true,
    entryViewModel: ExamEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    topicListViewModel: TopicListViewModel = viewModel(factory = AppViewModelProvider.Factory),

    ) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = examDetails.name,
            onValueChange = { onValueChange(examDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.exam_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        DropDownList(
            name = stringResource(R.string.exam),
            items = topicListViewModel.topicListUiState.topicList.map{it.topic}.filterNot{ it == examDetails.topicName },
            onChoose = {topic ->
                coroutineScope.launch{
                    onValueChange(examDetails.copy(topicId = entryViewModel.getTopicIdByTopic(topic)))
                }
            },
            default = examDetails.topicName,
            modifier = Modifier.fillMaxWidth(),
        )
        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}
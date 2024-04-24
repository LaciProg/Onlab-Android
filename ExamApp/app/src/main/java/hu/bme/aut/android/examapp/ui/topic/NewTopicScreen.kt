package hu.bme.aut.android.examapp.ui.topic

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.components.DropDownList
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetails
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEntryScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicUiState
import kotlinx.coroutines.launch

@Composable
fun NewTopic(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TopicEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when(viewModel.topicScreenUiState){
        TopicEntryScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        TopicEntryScreenUiState.Success -> NewTopicScreenUiState(viewModel, navigateBack)
        TopicEntryScreenUiState.Error -> Text(text = TopicEntryScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
    }

}

@Composable
private fun NewTopicScreenUiState(
    viewModel: TopicEntryViewModel,
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = { }
    ) { innerPadding ->
        TopicEntryBody(
            topicUiState = viewModel.topicUiState,
            onTopicValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.saveTopic()) {
                        navigateBack()
                    } else {
                        Toast.makeText(
                            context,
                            "Topic with this name already exists",
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
fun TopicEntryBody(
    topicUiState: TopicUiState,
    onTopicValueChange: (TopicDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TopicInputForm(
            topicDetails = topicUiState.topicDetails,
            onValueChange = onTopicValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = topicUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun TopicInputForm(
    topicDetails: TopicDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TopicDetails) -> Unit = {},
    default: String = "",
    enabled: Boolean = true,
    listViewModel: TopicListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    entryViewModel: TopicEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = topicDetails.topic,
            onValueChange = { onValueChange(topicDetails.copy(topic = it)) },
            label = { Text(stringResource(R.string.topic_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = topicDetails.description,
            onValueChange = { onValueChange(topicDetails.copy(description = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text(stringResource(R.string.topic_description_req)) },
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
            name = stringResource(R.string.topic),
            items = listViewModel.topicListUiState.topicList.map { it.topic } .filterNot{ it == topicDetails.topic },
            onChoose = {parent ->
                coroutineScope.launch{
                    Log.d("NewTopicScreen", "parent: $parent")
                    onValueChange(topicDetails.copy(parent = entryViewModel.getTopicIdByTopic(parent)))
                }
            },
            default = topicDetails.parentTopicName,
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
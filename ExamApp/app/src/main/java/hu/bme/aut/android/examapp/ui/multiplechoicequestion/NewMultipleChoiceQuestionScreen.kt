package hu.bme.aut.android.examapp.ui.multiplechoicequestion

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.ui.components.DropDownList
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetails
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEntryScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionUiState
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import kotlinx.coroutines.launch

@Composable
fun NewMultipleChoiceQuestionScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: MultipleChoiceQuestionEntryViewModel = hiltViewModel()//viewModel(factory = AppViewModelProvider.Factory)
) {
    when(viewModel.multipleChoiceQuestionScreenUiState) {
        MultipleChoiceQuestionEntryScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
        MultipleChoiceQuestionEntryScreenUiState.Success -> NewMultipleChoiceQuestionScreenUiState(viewModel, navigateBack)
        MultipleChoiceQuestionEntryScreenUiState.Error ->  Text(text = MultipleChoiceQuestionEntryScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
    }
}

@Composable
private fun NewMultipleChoiceQuestionScreenUiState(
    viewModel: MultipleChoiceQuestionEntryViewModel,
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {}
    ) { innerPadding ->
        MultipleChoiceQuestionEntryBody(
            multipleChoiceQuestionUiState = viewModel.multipleChoiceQuestionUiState,
            onMultipleChoiceQuestionValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    if (viewModel.saveMultipleChoiceQuestion()) {
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
fun MultipleChoiceQuestionEntryBody(
    multipleChoiceQuestionUiState: MultipleChoiceQuestionUiState,
    onMultipleChoiceQuestionValueChange: (MultipleChoiceQuestionDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        MultipleChoiceQuestionInputForm(
            multipleChoiceQuestionDetails = multipleChoiceQuestionUiState.multipleChoiceQuestionDetails,
            onValueChange = onMultipleChoiceQuestionValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = multipleChoiceQuestionUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun MultipleChoiceQuestionInputForm(
    multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails,
    modifier: Modifier = Modifier,
    onValueChange: (MultipleChoiceQuestionDetails) -> Unit = {},
    enabled: Boolean = true,
    topicListViewModel: TopicListViewModel = hiltViewModel(),
    pointListViewModel: PointListViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        DropDownList(
            name = stringResource(R.string.topic_name_req),
            items = topicListViewModel.topicListUiState.topicList.map{it.topic}.filterNot{ it == multipleChoiceQuestionDetails.topicName },
            onChoose = { topicName -> onValueChange(multipleChoiceQuestionDetails.copy(topicName = topicName, topic = if(topicName.isNotBlank()) topicListViewModel.topicListUiState.topicList.filter { it.topic == topicName }.map{it.id}.first() else "")) },
            default = multipleChoiceQuestionDetails.topicName,
            modifier = Modifier.fillMaxWidth(),
        )
        DropDownList(
            name = stringResource(R.string.point_req),
            items = pointListViewModel.pointListUiState.pointList.map{it.point}.filterNot{ it == multipleChoiceQuestionDetails.pointName },
            onChoose = { pointName -> onValueChange(multipleChoiceQuestionDetails.copy(pointName = pointName, point = if(pointName.isNotBlank()) pointListViewModel.pointListUiState.pointList.filter { it.point ==  pointName}.map{it.id}.first() else "")) },
            default = multipleChoiceQuestionDetails.pointName,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = multipleChoiceQuestionDetails.question,
            onValueChange = { onValueChange(multipleChoiceQuestionDetails.copy(question = it)) },
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


        val listSize = remember{ mutableIntStateOf(multipleChoiceQuestionDetails.answers.size) }
        //TODO Kókány lista
        Column {
            Text(stringResource(R.string.correct_answer_req))
            listSize.intValue = multipleChoiceQuestionDetails.answers.size
            var text by remember { mutableStateOf("")}
            for(index in 0 until listSize.intValue){

                //TODO ha kitörlöd nem fut a recomposeition
                Log.d("asd", "text: $text ${multipleChoiceQuestionDetails.isAnswerChosen}")

                Row{
                    TextButton(onClick = {
                        if(multipleChoiceQuestionDetails.correctAnswersList.contains(multipleChoiceQuestionDetails.answers[index]) && multipleChoiceQuestionDetails.answers[index].isNotEmpty() ){
                            multipleChoiceQuestionDetails.correctAnswersList.remove(multipleChoiceQuestionDetails.answers[index])
                            //Log.d("remove", "remove: ${multipleChoiceQuestionDetails.correctAnswersList}")
                            if(multipleChoiceQuestionDetails.correctAnswersList.isEmpty()){
                                onValueChange(multipleChoiceQuestionDetails.copy(isAnswerChosen = false))
                            }
                        }
                        else {
                            if (multipleChoiceQuestionDetails.isAnswerChosen && multipleChoiceQuestionDetails.answers[index].isNotEmpty()) {
                                multipleChoiceQuestionDetails.correctAnswersList.add(
                                    multipleChoiceQuestionDetails.answers[index]
                                )
                            } else {
                               if(multipleChoiceQuestionDetails.correctAnswersList.size == 1 && multipleChoiceQuestionDetails.correctAnswersList[0].isEmpty())multipleChoiceQuestionDetails.correctAnswersList.clear()
                               if(multipleChoiceQuestionDetails.answers[index].isNotEmpty())multipleChoiceQuestionDetails.correctAnswersList.add(multipleChoiceQuestionDetails.answers[index])
                               onValueChange(multipleChoiceQuestionDetails.copy(isAnswerChosen = true))
                            }
                            //Log.d("correctAnswer", "correctAnswer: ${multipleChoiceQuestionDetails.correctAnswersList}")
                        }
                        text = multipleChoiceQuestionDetails.correctAnswersList.toString()
                    }) {
                        Text(
                            text = ('A'.code+index).toChar().toString(),
                            color = if(!multipleChoiceQuestionDetails.isAnswerChosen) MaterialTheme.colorScheme.onSurface
                            else if (multipleChoiceQuestionDetails.correctAnswersList.contains(multipleChoiceQuestionDetails.answers[index])) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        if(multipleChoiceQuestionDetails.correctAnswersList.contains(multipleChoiceQuestionDetails.answers[index])){
                            multipleChoiceQuestionDetails.correctAnswersList.remove(multipleChoiceQuestionDetails.answers[index])
                            if(multipleChoiceQuestionDetails.correctAnswersList.isEmpty()){
                                onValueChange(multipleChoiceQuestionDetails.copy(isAnswerChosen = false))
                            }
                        }
                        multipleChoiceQuestionDetails.answers.removeAt(index)
                        listSize.intValue = multipleChoiceQuestionDetails.answers.size
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                    OutlinedTextField(
                        value =  multipleChoiceQuestionDetails.answers[index],
                        onValueChange = {
                            text = it
                            if(multipleChoiceQuestionDetails.correctAnswersList.contains(multipleChoiceQuestionDetails.answers[index]) && multipleChoiceQuestionDetails.answers[index].isNotEmpty()){
                                //Log.d("correctAnswer", "a")
                                multipleChoiceQuestionDetails.correctAnswersList[multipleChoiceQuestionDetails.correctAnswersList.indexOf(multipleChoiceQuestionDetails.answers[index])] = text
                            }
                            multipleChoiceQuestionDetails.answers[index] = it
                            },
                        label = { Text(stringResource(R.string.question_answer_req)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        singleLine = true
                    )
                }
            }

            Button(onClick = {
                multipleChoiceQuestionDetails.answers.add("")
                listSize.intValue = multipleChoiceQuestionDetails.answers.size
            }) {
                Text("Add question")
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
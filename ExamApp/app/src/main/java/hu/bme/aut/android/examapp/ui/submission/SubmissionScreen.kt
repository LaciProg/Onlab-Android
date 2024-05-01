package hu.bme.aut.android.examapp.ui.submission

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.Question
import hu.bme.aut.android.examapp.api.dto.StatisticsDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.theme.Green
import hu.bme.aut.android.examapp.ui.theme.PaleDogwood
import hu.bme.aut.android.examapp.ui.theme.Purple40
import hu.bme.aut.android.examapp.ui.viewmodel.submission.Answers
import hu.bme.aut.android.examapp.ui.viewmodel.submission.SubmissionResultScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.submission.SubmissionScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.submission.SubmissionViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type

@Composable
fun SubmissionScreen (
    navigateBack: () -> Unit = {},
    viewModel: SubmissionViewModel = hiltViewModel()
){
    when(viewModel.submissionScreenUiState){
        is SubmissionScreenUiState.Loading ->  CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is SubmissionScreenUiState.Error -> Text(text = SubmissionScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
        is SubmissionScreenUiState.Success -> SubmissionScreenContent(viewModel)

    }


    if(viewModel.statisticsDto != null){
        when(viewModel.statisticsDialogUiState){
            is SubmissionResultScreenUiState.Loading ->  StatisticDialogContent(viewModel =  viewModel, navigateBack =  navigateBack, text = { CircularProgressIndicator(modifier = Modifier.fillMaxSize())})
            is SubmissionResultScreenUiState.Error -> StatisticDialogContent(viewModel =  viewModel, navigateBack =  navigateBack, text = { Text(SubmissionResultScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })})
            is SubmissionResultScreenUiState.Success -> StatisticDialogContent(viewModel =  viewModel, navigateBack =  navigateBack)
        }
    }


}

@Composable
fun SubmissionScreenContent(
    viewModel: SubmissionViewModel
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Submission Screen")
                Text(text = viewModel.uiState.examDetails.name)

            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        for ((index, question) in viewModel.uiState.examDetails.questionList.withIndex()) {
            QuestionCard(question, viewModel.answers, index)
        }

        Button(onClick = {
            viewModel.statisticsDto = StatisticsDto(0.0,0.0)
            viewModel.statisticsDialogUiState = SubmissionResultScreenUiState.Loading
            viewModel.submitAnswers(answers = viewModel.answers.answers.joinToString("-"))
        }) {
            Text(stringResource(R.string.submit))
        }
    }
}

@Composable
fun StatisticDialogContent(
    navigateBack: () -> Unit = {},
    text: @Composable (() -> Unit)? = null,
    viewModel: SubmissionViewModel = hiltViewModel()
){
    StatisticsDialog(
        viewModel.statisticsDto!!,
        onDismiss = {
            viewModel.statisticsDto = null
        },
        onDismissRequest = {
            viewModel.statisticsDto = null
        },
        navigateBack = { navigateBack() },
        text = text
    )
}

@Composable
fun QuestionCard(
    question: Question,
    answers: Answers,
    index: Int,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = contentColor
    )
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = AlertDialogDefaults.shape,
        colors = CardDefaults.cardColors(
            containerColor = if(question.typeOrdinal == Type.trueFalseQuestion.ordinal) PaleDogwood else Green,
            contentColor = Purple40
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column()
            {
                Text(
                    text = when (question) {
                        is TrueFalseQuestionDto -> question.question
                        is MultipleChoiceQuestionDto -> question.question
                    },
                    modifier = Modifier.width(150.dp)
                )
                if(question is MultipleChoiceQuestionDto){
                    for ((answerNumber, answer) in question.answers.withIndex()) {
                        Text(
                            text = "${answerNumber}. $answer",
                            modifier = Modifier.width(150.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            var answer by rememberSaveable {
                mutableStateOf("")
            }
            OutlinedTextField(
                value = answer,
                onValueChange = {
                    answer = it
                    answers.answers[index] = it
                },
                label = { Text(stringResource(
                    when (question) {
                        is TrueFalseQuestionDto -> R.string.true_or_false
                        is MultipleChoiceQuestionDto -> R.string.answer_numbers
                    }
                )) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true,
                shape = AlertDialogDefaults.shape,
                keyboardOptions = when (question) {
                    is TrueFalseQuestionDto -> KeyboardOptions(keyboardType = KeyboardType.Text)
                    is MultipleChoiceQuestionDto -> KeyboardOptions(keyboardType = KeyboardType.Number)
                }
            )
        }
    }
}

@Composable
fun StatisticsDialog(
    statisticsDto: StatisticsDto,
    navigateBack: () -> Unit = {},
    onDismissRequest: () -> Unit,
    text: @Composable (() -> Unit)? = null,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text("Statistics") },
        text = { text?.let { it() } ?:
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.correct_answers, statisticsDto.earnedPoints))
                Text(stringResource(R.string.percentage, statisticsDto.percentage * 100))
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss(); navigateBack() }) {
                Text(stringResource(R.string.nice))
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.try_again))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SubmissionScreenPreview(){
    SubmissionScreen(
    )
}

@Preview(showBackground = true)
@Composable
fun QuestionCardPreview(){
    StatisticsDialog(StatisticsDto(420.0, 0.69), {}, {}, {}, {})
}
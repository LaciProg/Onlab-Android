package hu.bme.aut.android.examapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.examapp.data.MultipleChoiceData
import hu.bme.aut.android.examapp.data.TrueFalseData
import kotlin.random.Random

@Composable
fun MultipleChoiceQuestion(question: MultipleChoiceData = MultipleChoiceData("Default question", previewAnswerList, 0)) {
    //val answersRandomized = answers.shuffled()
    MultipleChoiceQuestionRow(question = question.question, answers = question.answers.toMutableList(), isEditable = false)

    /*Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(question)
        var questionNumber by remember { mutableIntStateOf (65) }
        for (answer in answers) {
            Row(verticalAlignment = Alignment.CenterVertically){
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = questionNumber.toChar().toString()
                    )
                    Text(text = answer)
                }
            }
            questionNumber++
        }
    }*/
}

@Composable
fun EditMultipleChoiceQuestion(
    question: MultipleChoiceData = MultipleChoiceData("Default question", previewAnswerList, 0),
    onQuestionChanged: (MultipleChoiceData) -> Unit = {},
) {
    var questionName by remember { mutableStateOf(question.question) }
    val answers = remember { question.answers.toMutableList() }
    var correctAnswer by remember { mutableIntStateOf(question.correctAnswer) }
    Column {
        MultipleChoiceQuestionRow(question = questionName,
            answers = answers,
            isEditable = true,
            onQuestionChanged = { questionName = it },
            onAnswerChange = { answer: String, index: Int ->
                answers[index] = answer
            },
            onCorrectAnswerChange = { correct:Int ->
                correctAnswer = correct
                Log.d("EditMultipleChoiceQuestion", "correctAnswer: $correctAnswer")}
        )
        Button(onClick = { onQuestionChanged(MultipleChoiceData(questionName, answers, correctAnswer)) }) {
            Text("Save")
        }
    }
}

@Composable
fun MultipleChoiceQuestionRow(
    question: String = "Default question",
    answers: MutableList<String> = previewAnswerList,
    isEditable: Boolean = false,
    onQuestionChanged: (String) -> Unit = {},
    onAnswerChange: (answer: String, index: Int) -> Unit = { _, _ -> },
    onCorrectAnswerChange: (Int) -> Unit = {},
) {
    if (isEditable) {
        Editable(
            question = question,
            answers = answers,
            onQuestionChanged = onQuestionChanged,
            onAnswerChange = onAnswerChange,
            onCorrectAnswerChange = onCorrectAnswerChange
        )
    } else {
        ReadOnly(
            question = question,
            answers = answers
        )
    }
}

@Composable
private fun Editable(
    question: String = "Default question",
    answers: MutableList<String> = previewAnswerList,
    onQuestionChanged: (String) -> Unit = {},
    onAnswerChange: (answer: String, index: Int) -> Unit = { _, _ -> },
    onCorrectAnswerChange: (Int) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxWidth()){
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = question,
            onValueChange = { onQuestionChanged(it) }
        )
        for ((index, answer) in answers.withIndex()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1f),
                    onClick = {
                        Log.d("MultipleChoiceQuestion", "index: $index")
                        onCorrectAnswerChange(index)
                    }
                ) {
                    Text(text = (index+'A'.code).toChar().toString())
                }
                Log.d("MultipleChoiceQuestion", "questionNumber: $index")
                TextField(
                    modifier = Modifier.weight(8f),
                    value = answer,
                    onValueChange = { onAnswerChange(it, index) }
                )
            }
        }
    }
}

@Composable
private fun ReadOnly(
    question: String,
    answers: MutableList<String>,
){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(question)
        for ((index, answer) in answers.withIndex()) {
            Row(verticalAlignment = Alignment.CenterVertically){
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        modifier = Modifier.padding(end = 16.dp),
                        text = (index+'A'.code).toChar().toString()
                    )
                    Text(text = answer)
                }
            }
        }
    }
}

val previewAnswerList = mutableListOf("Answer 1", "Answer 2", "Answer 3", "Answer 4")
@Composable
@Preview(showBackground = true)
fun MultipleChoiceQuestionPreview() {
    MultipleChoiceQuestion()
}

@Composable
@Preview(showBackground = true)
fun EditMultipleChoiceQuestionPreview() {
    EditMultipleChoiceQuestion()
}
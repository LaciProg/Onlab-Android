package hu.bme.aut.android.examapp.ui.components

import android.text.Editable
import android.widget.EditText
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.examapp.data.TrueFalseData

@Composable
fun TrueFalseQuestion(question: String = "Default question") {
    TrueFalseQuestionRow(question = question)
    /*Row(verticalAlignment = Alignment.CenterVertically){
        Text(text = question)
        Spacer(Modifier.width(16.dp))
        TextButton(onClick = { /*TODO*/ }) {
            Text("True")
        }
        Spacer(Modifier.width(16.dp))
        TextButton(onClick = { /*TODO*/ }) {
            Text("False")
        }
    }*/
}

@Composable
fun EditTrueFalseQuestion(questionData: TrueFalseData = TrueFalseData("Default", false), onQuestionChanged: (TrueFalseData) -> Unit = {}) {
    var question by remember { mutableStateOf(questionData.question) }
    var answer by remember { mutableStateOf(questionData.answer) }
    Column {

        TrueFalseQuestionRow(question = question, isEditable = true, onQuestionChanged = { question = it }, onAnswerChange =  { answer = it })
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            TextField(
                modifier = Modifier.weight(4f),
                value = question,
                onValueChange = { question = it }
            )
            Spacer(Modifier.width(16.dp))
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { answer = true }
            ) {
                Text("True")
            }
            Spacer(Modifier.width(16.dp))
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { answer = false}
            ) {
                Text("False")
            }
        }*/
        Button(onClick = { onQuestionChanged(TrueFalseData(question, answer)) }) {
            Text("Save")
        }
    }
}

@Composable
fun TrueFalseQuestionRow(question: String = "Default question",
                         isEditable: Boolean = false,
                         onQuestionChanged: (String) -> Unit = {},
                         onAnswerChange: (Boolean) -> Unit = {}
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ){
        if(isEditable){
            TextField(
                modifier = Modifier.weight(4f),
                value = question,
                onValueChange = { onQuestionChanged(it) }
            )
        } else {
            Text(
                modifier = Modifier.weight(4f),
                text = question
            )
        }
        /*TextField(
            modifier = Modifier.weight(4f),
            value = question,
            onValueChange = { onTextChange(it) }
        )*/
        Spacer(Modifier.width(16.dp))
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = { onAnswerChange(true) }
        ) {
            Text("True")
        }
        Spacer(Modifier.width(16.dp))
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = { onAnswerChange(false) }
        ) {
            Text("False")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TrueFalseQuestionPreview() {
    TrueFalseQuestion()
}

@Composable
@Preview(showBackground = true)
fun EditTrueFalseQuestionPreview() {
    EditTrueFalseQuestion()
}
package hu.bme.aut.android.examapp.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.examapp.data.TrueFalseData

@Composable
fun ExportedTrueFalseQuestion(number: Int, question: String, point: Double, color: Color = Color.Black)
{
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(text = "$number. $question", color = color)
            Text(text = "Point: $point\\", modifier = Modifier.padding(end = 50.dp), color = color)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text("True", color = color)
            Spacer(modifier = Modifier.width(100.dp))
            Text("False", color = color)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ExportedTrueFalseQuestionPreview() {
    ExportedTrueFalseQuestion(1, "Default question", 2.0 /*PointDto(1, 1.0, Type.trueFalseQuestion.name, 1.0, 1.0 )*/)
}



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
fun EditTrueFalseQuestion(questionData: TrueFalseData = TrueFalseData("Default", false, "Compose", "Plus/Minus/2"), onQuestionChanged: (TrueFalseData) -> Unit = {}) {
    var question by remember { mutableStateOf(questionData.question) }
    var answer by remember { mutableStateOf(questionData.answer) }
    var topic by remember { mutableStateOf("") }
    var point by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            PointTopicBar(
                onChooseTopic = {
                    Log.d("MultiQuestionTopic", it)
                    topic = it
                },
                onChoosePoint = {
                    Log.d("MultiQuestionPoint", it)
                    point = it
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TrueFalseQuestionRow(
                question = question,
                isEditable = true,
                onQuestionChanged = { question = it },
                onAnswerChange = { answer = it })
            Button(onClick = { onQuestionChanged(TrueFalseData(question, answer, topic, point)) }) {
                Text("Save")
            }
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
package hu.bme.aut.android.examapp.ui.exam

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import hu.bme.aut.android.examapp.data.room.dto.Question
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.pdf.PDFExamView
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.components.ExportedMultipleChoiceQuestion
import hu.bme.aut.android.examapp.ui.components.ExportedTrueFalseQuestion
import hu.bme.aut.android.examapp.ui.exam.JetCaptureView.jetCaptureView
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamDetailsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object JetCaptureView{
    var jetCaptureView: MutableState<PDFExamView>? = null
}


@Composable
fun ExportExamDetailsScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    examViewModel: ExamDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val  examUiState = examViewModel.uiState.collectAsState()
    val pointList = examViewModel.pointList.collectAsState().value
    //var jetCaptureView: MutableState<PDFExamView>? = null
    Scaffold(
        topBar = {
            TopBar(
                examName = examUiState.value.examDetails.name,
                modifier = modifier
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton({
                Text(text = "Capture ProfileView", color = Color.White)},
                onClick = { jetCaptureView?.value?.capture(jetCaptureView?.value as PDFExamView) },
                shape = RoundedCornerShape(18.dp),
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Export pdf"
                    )
                }
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) { padding ->

        ExportExamDetailsBodyView(
            examName = examUiState.value.examDetails.name,
            questions =  examUiState.value.examDetails.questionList,
            pointList = pointList,
            modifier = modifier.padding(padding)
        )
        /*
        ExportExamDetailsBody(
            navigateBack = navigateBack,
            examName = examUiState.value.examDetails.name,
            questions =  examUiState.value.examDetails.questionList,
            pointList = examViewModel.pointList.collectAsState().value,
            modifier = modifier.padding(padding)
        )*/

    }

}

@Composable
private fun ExportExamDetailsBodyView(
    examName: String,
    questions: List<Question>,
    pointList: List<PointDto>,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    Log.d("ExportExamDetailsBodyView", "examName: $examName, questions: $questions, pointList: $pointList")
    if(examName.isNotEmpty() && questions.isNotEmpty() && pointList.isNotEmpty()) {
        jetCaptureView = remember {
            mutableStateOf(
                PDFExamView(
                    context = context,
                    examName = examName,
                    questions = questions,
                    pointList = pointList,
                    modifier = modifier
                )
            )
        }
        AndroidView(/*modifier = Modifier.wrapContentSize(),*/
            factory = {
                PDFExamView(it).apply {
                    post {
                        jetCaptureView?.value = this
                    }
                }
            }
        )
    }
}

@Composable
fun TopBar(
    examName: String,
    modifier: Modifier = Modifier
) {

}

@Composable
fun ExportExamDetailsBody(
    examName: String,
    questions: List<Question>,
    pointList: List<PointDto>,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    Card(modifier = modifier.fillMaxWidth()){
        Column(
            modifier = modifier.verticalScroll(rememberScrollState()).horizontalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,

        ){
            Header(modifier, examName)
            Spacer(modifier = modifier.size(16.dp))
            Points(questions, pointList, modifier)
            Text(text = stringResource(id = R.string.questions))
            Spacer(modifier = modifier.size(16.dp))
            for ((index, question) in questions.withIndex()) {
                when (question) {
                    is TrueFalseQuestionDto -> {
                        ExportedTrueFalseQuestion(
                            number = index + 1,
                            question = question.question,
                            point = pointList.find { it.id == question.topic }?.point ?: 0.0
                        )
                    }
                    is MultipleChoiceQuestionDto -> {
                        ExportedMultipleChoiceQuestion(
                            number = index + 1,
                            question = question.question,
                            point = pointList.find { it.id == question.topic }?.point ?: 0.0,
                            answers = question.answers.split("¤"),
                        )
                    }
                    else -> throw IllegalArgumentException("Invalid type")
                }
            }
        }
    }
}

@Composable
private fun Points(
    questions: List<Question>,
    pointList: List<PointDto>,
    modifier: Modifier = Modifier
){
    val examPointList = getExamPointList(questions, pointList)
    val maxPoint = examPointList.sum()
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        PointTable(modifier = modifier)
        Spacer(modifier = modifier.size(16.dp))
        Column(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))){
            OutlinedTextField(
                value = stringResource(id = R.string.total_points)+"$maxPoint",
                onValueChange = {},
                readOnly = true,
                modifier = modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = stringResource(id = R.string.reached_points),
                onValueChange = {},
                readOnly = true,
                modifier = modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = stringResource(id = R.string.percent),
                onValueChange = {},
                readOnly = true,
                modifier = modifier.fillMaxWidth()
            )
        }
    }

}

@Composable
private fun PointTable(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium))) {
        OutlinedTextField(
            value = stringResource(id = R.string.grade_boundaries),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.size(height = 50.dp, width = 250.dp)
        )
        GradeBoundariesTableLine(percent = R.string.one_percent, grade = R.string.one_grade)
        GradeBoundariesTableLine(percent = R.string.two_percent, grade = R.string.two_grade)
        GradeBoundariesTableLine(percent = R.string.three_percent, grade = R.string.three_grade)
        GradeBoundariesTableLine(percent = R.string.four_percent, grade = R.string.four_grade)
        GradeBoundariesTableLine(percent = R.string.five_percent, grade = R.string.five_grade)
    }

}

@Composable
private fun GradeBoundariesTableLine(@StringRes percent: Int, @StringRes grade: Int) {
    Row(Modifier.size(height = 50.dp, width = 250.dp)) {
        OutlinedTextField(
            value = stringResource(id = percent),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.weight(1.5f)
        )
        OutlinedTextField(
            value = stringResource(id = grade),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.weight(3f)
        )
    }
}


private fun getExamPointList (
    questions: List<Question>,
    pointList: List<PointDto>
): List<Double> {
    val points: MutableList<Double> = mutableListOf()
    for (question in questions) {
        when (question) {
            is TrueFalseQuestionDto -> {
                points.add(pointList.find { it.id == question.topic }?.point ?: 0.0)
            }

            is MultipleChoiceQuestionDto -> {
                points.add(pointList.find { it.id == question.topic }?.point ?: 0.0)
            }

            else -> throw IllegalArgumentException("Invalid type")
        }
    }
    return points
}

@Composable
private fun Header(modifier: Modifier, examName: String) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        Text(text = examName)
    }
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = stringResource(id = R.string.name),
            onValueChange = {},
            readOnly = true,
            modifier = modifier
                .padding(top = 20.dp)
                .weight(1f)
        )
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(3f),
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.padding(8.dp))
        BasicTextField(
            value = stringResource(id = R.string.neptun_code),
            onValueChange = {},
            readOnly = true,
            modifier = modifier
                .padding(top = 20.dp)
                .weight(1f)
        )
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f),
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent
            )
        )
    }
}
/*
@Composable
@Preview(showBackground = true)
fun TopBarPreview() {
    TopBar(examName = "Exam name")
}*/

@Composable
@Preview(showBackground = true)
fun ExportExamDetailsBodyPreview() {
    ExportExamDetailsBody(examName = "Exam name", navigateBack = {}, questions = listOf(), pointList = listOf())
}

@Composable
@Preview(showBackground = true)
fun PointTablePreview() {
    PointTable()
}

@Composable
@Preview(showBackground = true)
fun PointsPreview() {
    Points(questions = listOf(), pointList = listOf())
}

package hu.bme.aut.android.examapp.pdf

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import hu.bme.aut.android.examapp.data.room.dto.Question
import hu.bme.aut.android.examapp.ui.exam.ExportExamDetailsBody

class PDFExamView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    private var examName: String = "AAAAAAAAAlma"
    private var questions: List<Question> = emptyList()
    private var pointList: List<PointDto> = emptyList()
    private var modifier: Modifier = Modifier
    constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
        examName: String, questions: List<Question>, pointList: List<PointDto>, modifier: Modifier
    ) : this(context, attrs, defStyleAttr){
        this.examName =  examName
        this.questions = questions
        this.pointList = pointList
        this.modifier = modifier
        Log.d("PDFExamView", "examName: $examName, questions: $questions, pointList: $pointList")
    }


    //side effect
    @Composable
    override fun Content() {
        var localExamName by remember { mutableStateOf("")  }
        localExamName = examName
        // This is a ComposableUI function
        //if(::examName.isInitialized && ::questions.isInitialized && ::pointList.isInitialized){
            Log.d("PDFExamViewContent", "examName: $localExamName, questions: $questions, pointList: $pointList")
            ExportExamDetailsBody(examName = localExamName, questions = questions, pointList = pointList)
        //}
        //else return
        //ExportExamDetailsBody(examName = examName, questions = questions, pointList = pointList)
    }

    fun capture(view: PDFExamView) {
        val bitmap = ImageUtils.generateShareImage(view)
        ShareUtils.shareImageToOthers(context,"test", bitmap)
    }

}


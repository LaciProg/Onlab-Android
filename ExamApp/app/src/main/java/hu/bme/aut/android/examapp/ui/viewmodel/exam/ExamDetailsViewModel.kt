package hu.bme.aut.android.examapp.ui.viewmodel.exam

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.ExamDto
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.api.dto.Question
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
//import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
//import hu.bme.aut.android.examapp.data.room.dto.PointDto
//import hu.bme.aut.android.examapp.data.room.dto.Question
//import hu.bme.aut.android.examapp.data.room.dto.TopicDto
//import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.ExamDetailsDestination
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetailsScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetailsUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.toMultipleChoiceQuestionDetails
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface ExamDetailsScreenUiState {
    data class Success(val exam: ExamDto) : ExamDetailsScreenUiState
    object Error : ExamDetailsScreenUiState{var errorMessage: String = ""}
    object Loading : ExamDetailsScreenUiState
}

class ExamDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val examRepository: ExamRepository,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    val examId: String = checkNotNull(savedStateHandle[ExamDetailsDestination.examIdArg])

    var examDetailsScreenUiState: ExamDetailsScreenUiState by mutableStateOf(
        ExamDetailsScreenUiState.Loading)
    var uiState by mutableStateOf(ExamDetailsUiState())

    var pointList: List<PointDto> = listOf()
    var topicList: List<TopicDto> = listOf()
    var trueFalseList: List<TrueFalseQuestionDto> = listOf()
    var multipleChoiceList: List<MultipleChoiceQuestionDto> = listOf()
    init {
        getExam(examId)
        viewModelScope.launch {
            trueFalseList = ExamAppApi.retrofitService.getAllTrueFalse()
            multipleChoiceList = ExamAppApi.retrofitService.getAllMultipleChoice()
            pointList = ExamAppApi.retrofitService.getAllPoint()
            topicList = ExamAppApi.retrofitService.getAllTopic()
        }
    }


    fun getExam(topicId: String){
        examDetailsScreenUiState = ExamDetailsScreenUiState.Loading
        viewModelScope.launch {
            examDetailsScreenUiState = try{
                val result = ExamAppApi.retrofitService.getExam(topicId)
                uiState = ExamDetailsUiState(result.toExamDetails(
                    topicName =
                    if (result.topicId == "null") ""
                    else ExamAppApi.retrofitService.getTopic(result.topicId).topic,
                    questionList = if(result.questionList == "") listOf() else result.questionList.split("#").map { it.toQuestion() }
                ))
                ExamDetailsScreenUiState.Success(result)
            } catch (e: IOException) {
                ExamDetailsScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> ExamDetailsScreenUiState.Error.errorMessage = "Bad request"
                    401 -> ExamDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> ExamDetailsScreenUiState.Error.errorMessage = "Content not found"
                    500 -> ExamDetailsScreenUiState.Error.errorMessage = "Server error"
                    else -> ExamDetailsScreenUiState.Error
                }
                ExamDetailsScreenUiState.Error
            }
        }
    }


    /**
     * Holds the item details ui state. The data is retrieved from [ExamRepository] and mapped to
     * the UI state.
     */
    //val uiState: StateFlow<ExamDetailsUiState> =
    //    examRepository.getExamById(examId.toInt())
    //        .filterNotNull()
    //        .map { examDto ->
    //            ExamDetailsUiState(examDetails =  examDto.toExamDetails(
    //                topicName = topicRepository.getTopicById(examDto.topicId).filterNotNull().map { it.topic }.first(),
    //                questionList = if(examDto.questionList != "") examDto.questionList.split("¤").map { it.toQuestion() } else listOf()
    //            ))
    //        }.stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = ExamDetailsUiState()
    //        )


    /*val topicList: StateFlow<List<TopicDto>> = topicRepository.getAllTopics().filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )

    val pointList: StateFlow<List<PointDto>> = pointRepository.getAllPoints().filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )

    val trueFalseList: StateFlow<List<TrueFalseQuestionDto>> = trueFalseQuestionRepository.getAllTrueFalseQuestions().filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )

    val multipleChoiceList: StateFlow<List<MultipleChoiceQuestionDto>> = multipleChoiceQuestionRepository.getAllMultipleChoiceQuestions().filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )*/

    private suspend fun String.toQuestion(): Question {
        val question = this.split("~")
        val type = question[0].toInt()
        val questionId = question[1]
        return when(type){
            Type.trueFalseQuestion.ordinal -> toTrueFalseQuestion(questionId)
            Type.multipleChoiceQuestion.ordinal -> toMultipleChoiceQuestion(questionId)
            else -> throw IllegalArgumentException("Invalid type")
        }

    }

    suspend fun saveQuestionOrdering(list: List<Question>) {
        ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = list).toExam())
        //examRepository.updateExam(uiState.value.examDetails.copy(questionList = list).toExam())
    }

    private suspend fun toTrueFalseQuestion(id: String) : TrueFalseQuestionDto {
        return ExamAppApi.retrofitService.getTrueFalse(id.toString())
        //return trueFalseQuestionRepository.getTrueFalseQuestionById(id).filterNotNull().first()
    }

    private suspend fun toMultipleChoiceQuestion(id: String) : MultipleChoiceQuestionDto {
        return ExamAppApi.retrofitService.getMultipleChoice(id.toString())
        //return multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(id).filterNotNull().first()
    }

    suspend fun saveQuestion(ordinal: Int, question: String) {
        examDetailsScreenUiState = ExamDetailsScreenUiState.Loading

        val questionDto: Question = when(ordinal){
            Type.trueFalseQuestion.ordinal -> trueFalseList.first { question == it.question }
            Type.multipleChoiceQuestion.ordinal ->  multipleChoiceList.first { question == it.question }
            else -> throw IllegalArgumentException("Invalid type")
        }

        val newList =  uiState.examDetails.questionList.toMutableList()
        newList.add(questionDto)
        viewModelScope.launch{
            ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = newList).toExam())
            getExam(examId)
            examDetailsScreenUiState = ExamDetailsScreenUiState.Success(ExamAppApi.retrofitService.getExam(examId))
        }
        //ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = newList).toExam())
        //examRepository.updateExam(uiState.value.examDetails.copy(questionList = newList).toExam())
    }

    suspend fun removeQuestion(question: Question) {
        examDetailsScreenUiState = ExamDetailsScreenUiState.Loading
        val newList =  uiState.examDetails.questionList.toMutableList()

        when(question){
            is TrueFalseQuestionDto -> {
                val remove = trueFalseList.first { it.uuid == question.uuid }
                Log.d("ExamDetailsViewModel", "true: $remove")
                newList.remove(remove)
            }
            is MultipleChoiceQuestionDto -> {
                val remove = multipleChoiceList.first { it.uuid == question.uuid }
                Log.d("ExamDetailsViewModel", "multi: $remove")
                newList.remove(remove)
            }
            else -> throw IllegalArgumentException("Invalid type")
        }
        viewModelScope.launch {
            ExamAppApi.retrofitService.updateExam(uiState.examDetails.copy(questionList = newList).toExam())
            getExam(examId)
            examDetailsScreenUiState = ExamDetailsScreenUiState.Success(ExamAppApi.retrofitService.getExam(examId))
        }
        //examRepository.updateExam(uiState.value.examDetails.copy(questionList = newList).toExam())

    }

    /**
     * Deletes the item from the [ExamRepository]'s data source.
     */
    suspend fun deleteExam() {
        ExamAppApi.retrofitService.deleteExam(examId)
        //examRepository.deleteExam(uiState.value.examDetails.toExam())
    }

    /*suspend fun getExamById(id: Int): String {
        return examRepository.getExamById(id).filterNotNull().map { it.name }.first()
    }

    private suspend fun getPointByPointId(id: Int): PointDto {
        return pointRepository.getPointById(id).filterNotNull().first()
    }

    suspend fun getPoints(examId: Int): ExamDetails {
        val examDto = examRepository.getExamById(examId).filterNotNull().first()
        val examDetails =examDto.toExamDetails(
            topicRepository.getTopicById(examRepository.getExamById(examId).map { it.topicId }.first())
                .map{it.topic}.first(),
            examDto.questionList.split("¤").map { it.toQuestion() }
            )
        return examDetails
        /*
        return examDetails.questionList.map {
            when (it) {
                is TrueFalseQuestionDto -> {
                    getPointByPointId(it.point)
                }

                is MultipleChoiceQuestionDto -> {
                    getPointByPointId(it.point)
                }
                else -> throw IllegalArgumentException("Unknown question type")
            }
        }.toList()*/
    }*/



    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ExamDetailsScreen
 */
data class ExamDetailsUiState(
    val examDetails: ExamDetails = ExamDetails()
)
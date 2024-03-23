package hu.bme.aut.android.examapp.ui.viewmodel.exam

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import hu.bme.aut.android.examapp.data.room.dto.Question
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.ui.ExamDetailsDestination
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.typeOf

class ExamDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val examRepository: ExamRepository,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val examId: String = checkNotNull(savedStateHandle[ExamDetailsDestination.examIdArg.toString()])

    /**
     * Holds the item details ui state. The data is retrieved from [ExamRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ExamDetailsUiState> =
        examRepository.getExamById(examId.toInt())
            .filterNotNull()
            .map { examDto ->
                ExamDetailsUiState(examDetails =  examDto.toExamDetails(
                    topicName = topicRepository.getTopicById(examDto.topicId).filterNotNull().map { it.topic }.first(),
                    questionList = if(examDto.questionList != "") examDto.questionList.split("¤").map { it.toQuestion() } else listOf()
                ))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExamDetailsUiState()
            )


    val topicList: StateFlow<List<TopicDto>> = topicRepository.getAllTopics().filterNotNull().stateIn(
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
    )

    private suspend fun String.toQuestion(): Question {
        val question = this.split("-")
        val type = question[0].toInt()
        val questionId = question[1].toInt()
        return when(type){
            Type.trueFalseQuestion.ordinal -> toTrueFalseQuestion(questionId)
            Type.multipleChoiceQuestion.ordinal -> toMultipleChoiceQuestion(questionId)
            else -> throw IllegalArgumentException("Invalid type")
        }

    }

    suspend fun saveQuestionOrdering(list: List<Question>) {
        examRepository.updateExam(uiState.value.examDetails.copy(questionList = list).toExam())
    }

    private suspend fun toTrueFalseQuestion(id: Int) : TrueFalseQuestionDto {
        return trueFalseQuestionRepository.getTrueFalseQuestionById(id).filterNotNull().first()
    }

    private suspend fun toMultipleChoiceQuestion(id: Int) : MultipleChoiceQuestionDto {
        return multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(id).filterNotNull().first()
    }

    suspend fun saveQuestion(ordinal: Int, question: String) {
        val questionDto: Question = when(ordinal){
            Type.trueFalseQuestion.ordinal -> trueFalseList.first().first { question == it.question }
            Type.multipleChoiceQuestion.ordinal ->  multipleChoiceList.first().first { question == it.question }
            else -> throw IllegalArgumentException("Invalid type")
        }

        val newList =  uiState.value.examDetails.questionList.toMutableList()
        newList.add(questionDto)
        examRepository.updateExam(uiState.value.examDetails.copy(questionList = newList).toExam())
    }

    suspend fun removeQuestion(question: Question) {
        val newList =  uiState.value.examDetails.questionList.toMutableList()

        when(question){
            is TrueFalseQuestionDto -> {
                val remove = trueFalseList.first().first { it.id == question.id }
                Log.d("ExamDetailsViewModel", "true: $remove")
                newList.remove(remove)
            }
            is MultipleChoiceQuestionDto -> {
                val remove = multipleChoiceList.first().first { it.id == question.id }
                Log.d("ExamDetailsViewModel", "multi: $remove")
                newList.remove(remove)
            }
            else -> throw IllegalArgumentException("Invalid type")
        }
        examRepository.updateExam(uiState.value.examDetails.copy(questionList = newList).toExam())

    }

    /**
     * Deletes the item from the [ExamRepository]'s data source.
     */
    suspend fun deleteExam() {
        examRepository.deleteExam(uiState.value.examDetails.toExam())
    }

    suspend fun getExamById(id: Int): String {
        return examRepository.getExamById(id).filterNotNull().map { it.name }.first()
    }

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
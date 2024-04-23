package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionListUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionRowUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface MultipleChoiceQuestionListScreenUiState {
    data class Success(val questions: List<NameDto>) : MultipleChoiceQuestionListScreenUiState
    object Error : MultipleChoiceQuestionListScreenUiState
    object Loading : MultipleChoiceQuestionListScreenUiState
}

class MultipleChoiceQuestionListViewModel(
    multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    topicRepository: TopicRepository
) : ViewModel() {

    var multipleChoiceQuestionListScreenUiState: MultipleChoiceQuestionListScreenUiState by mutableStateOf(
        MultipleChoiceQuestionListScreenUiState.Loading)
    var multipleChoiceQuestionListUiState: MultipleChoiceQuestionListUiState by mutableStateOf(
        MultipleChoiceQuestionListUiState()
    )


    init {
        getAllMultipleChoiceQuestionList()
    }

    fun getAllMultipleChoiceQuestionList(){
        multipleChoiceQuestionListScreenUiState = MultipleChoiceQuestionListScreenUiState.Loading
        viewModelScope.launch {
            //try{
            val result = ExamAppApi.retrofitService.getAllMultipleChoiceName()
            multipleChoiceQuestionListScreenUiState =  MultipleChoiceQuestionListScreenUiState.Success(result)
            multipleChoiceQuestionListUiState = MultipleChoiceQuestionListUiState(
                multipleChoiceQuestionList = result.map { nameDto ->
                    MultipleChoiceQuestionRowUiState(
                        id = nameDto.uuid,
                        question = nameDto.name,
                    )
                }
            )
            //} catch (e: IOException) {
            //    MultipleChoiceQuestionListScreenUiState.Error
            //} /*catch (e: HttpException) {
            MultipleChoiceQuestionListScreenUiState.Error
            //}
        }
    }

    /**
     * Holds home ui state. The list of items are retrieved from [MultipleChoiceQuestionRepository] and mapped to
     * [MultipleChoiceQuestionListUiState]
     */
    //val multipleChoiceQuestionListUiState: StateFlow<MultipleChoiceQuestionListUiState> =
    //    multipleChoiceQuestionRepository.getAllMultipleChoiceQuestions().map { MultipleChoiceQuestionListUiState(
    //        multipleChoiceQuestionList = it.map { multipleChoiceQuestionDto ->
    //            MultipleChoiceQuestionRowUiState(
    //                question =multipleChoiceQuestionDto.question,
    //                answers = multipleChoiceQuestionDto.correctAnswersList.split("¤"),
    //                topic = topicRepository.getTopicById(multipleChoiceQuestionDto.topic).map {topic -> topic.topic }.first(),
    //                id = multipleChoiceQuestionDto.id
    //            )
    //        }
    //    ) }
    //        .stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = MultipleChoiceQuestionListUiState()
    //        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicListScreen
 */
data class MultipleChoiceQuestionListUiState(val multipleChoiceQuestionList: List<MultipleChoiceQuestionRowUiState> = listOf(
    MultipleChoiceQuestionRowUiState("",  ""),
))

data class MultipleChoiceQuestionRowUiState(
    val question: String = "",
    //val answers: List<String> = listOf(""),
    //val topic: String = "",
    val id: String = ""
)
package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicRowUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface TrueFalseQuestionListScreenUiState {
    data class Success(val questions: List<NameDto>) : TrueFalseQuestionListScreenUiState
    object Error : TrueFalseQuestionListScreenUiState{var errorMessage: String = ""}
    object Loading : TrueFalseQuestionListScreenUiState
}

class TrueFalseQuestionListViewModel(
    trueFalseQuestionRepository: TrueFalseQuestionRepository,
    topicRepository: TopicRepository
) : ViewModel() {

    var trueFalseQuestionListScreenUiState: TrueFalseQuestionListScreenUiState by mutableStateOf(TrueFalseQuestionListScreenUiState.Loading)
    var trueFalseQuestionListUiState: TrueFalseQuestionListUiState by mutableStateOf(TrueFalseQuestionListUiState())


    init {
        getAllTrueFalseQuestionList()
    }

    fun getAllTrueFalseQuestionList(){
        trueFalseQuestionListScreenUiState = TrueFalseQuestionListScreenUiState.Loading
        viewModelScope.launch {
            trueFalseQuestionListScreenUiState = try{
                val result = ExamAppApi.retrofitService.getAllTrueFalseName()
                trueFalseQuestionListUiState = TrueFalseQuestionListUiState(
                    trueFalseQuestionList = result.map { nameDto ->
                        TrueFalseQuestionRowUiState(
                            id = nameDto.uuid,
                            question = nameDto.name,
                        )
                    }
                )
                TrueFalseQuestionListScreenUiState.Success(result)
            } catch (e: IOException) {
                TrueFalseQuestionListScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TrueFalseQuestionListScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TrueFalseQuestionListScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TrueFalseQuestionListScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TrueFalseQuestionListScreenUiState.Error.errorMessage = "Server error"
                    else -> TrueFalseQuestionListScreenUiState.Error
                }
                TrueFalseQuestionListScreenUiState.Error
            }
        }
    }

    /**
     * Holds home ui state. The list of items are retrieved from [TrueFalseQuestionRepository] and mapped to
     * [TrueFalseQuestionListUiState]
     */
    //val trueFalseQuestionListUiState: StateFlow<TrueFalseQuestionListUiState> =
    //    trueFalseQuestionRepository.getAllTrueFalseQuestions().map { TrueFalseQuestionListUiState(
    //        trueFalseQuestionList = it.map { trueFalseQuestionDto ->
    //            TrueFalseQuestionRowUiState(
    //                question = trueFalseQuestionDto.question,
    //                answer = trueFalseQuestionDto.correctAnswer,
    //                topic = topicRepository.getTopicById(trueFalseQuestionDto.topic).map {topic -> topic.topic }.first(),
    //                id = trueFalseQuestionDto.id
    //            )
    //        }
    //    ) }
    //        .stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = TrueFalseQuestionListUiState()
    //        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicListScreen
 */
data class TrueFalseQuestionListUiState(val trueFalseQuestionList: List<TrueFalseQuestionRowUiState> = listOf(
    TrueFalseQuestionRowUiState("",""),
))

data class TrueFalseQuestionRowUiState(
    val question: String = "",
    //val answer: Boolean = false,
    //val topic: String = "",
    val id: String = ""
)
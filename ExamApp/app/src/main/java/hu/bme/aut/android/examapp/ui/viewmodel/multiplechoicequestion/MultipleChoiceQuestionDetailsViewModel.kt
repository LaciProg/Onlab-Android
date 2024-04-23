package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.ui.MultipleChoiceQuestionDetailsDestination
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.toTrueFalseQuestionDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface MultipleChoiceQuestionDetailsScreenUiState {
    data class Success(val question: MultipleChoiceQuestionDto) : MultipleChoiceQuestionDetailsScreenUiState
    object Error : MultipleChoiceQuestionDetailsScreenUiState
    object Loading : MultipleChoiceQuestionDetailsScreenUiState
}

class MultipleChoiceQuestionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    val multipleChoiceQuestionId: String = checkNotNull(savedStateHandle[MultipleChoiceQuestionDetailsDestination.multipleChoiceQuestionIdArg])
    var multipleChoiceDetailsScreenUiState: MultipleChoiceQuestionDetailsScreenUiState by mutableStateOf(
        MultipleChoiceQuestionDetailsScreenUiState.Loading)
    var uiState by mutableStateOf(MultipleChoiceQuestionDetailsUiState())
    init {
        getQuestion(multipleChoiceQuestionId)
    }

    fun getQuestion(topicId: String){
        multipleChoiceDetailsScreenUiState = MultipleChoiceQuestionDetailsScreenUiState.Loading
        viewModelScope.launch {
            //try{
            val result = ExamAppApi.retrofitService.getMultipleChoice(topicId)
            multipleChoiceDetailsScreenUiState =  MultipleChoiceQuestionDetailsScreenUiState.Success(result)
            uiState = MultipleChoiceQuestionDetailsUiState(result.toMultipleChoiceQuestionDetails(
                topicName =
                if (result.topic == "null") "" //TODO check this
                else ExamAppApi.retrofitService.getTopic(result.topic).topic,
                pointName =
                if (result.point == "null") "" //TODO check this
                else ExamAppApi.retrofitService.getPoint(result.point).type,
            ))
            //} catch (e: IOException) {
            //    MultipleChoiceQuestionDetailsScreenUiState.Error
            //} /*catch (e: HttpException) {
            MultipleChoiceQuestionDetailsScreenUiState.Error
            //}
        }
    }
    /**
     * Holds the item details ui state. The data is retrieved from [MultipleChoiceQuestionRepository] and mapped to
     * the UI state.
     */
    //val uiState: StateFlow<MultipleChoiceQuestionDetailsUiState> =
    //    multipleChoiceQuestionRepository.getMultipleChoiceQuestionById(multipleChoiceQuestionId.toInt())
    //        .filterNotNull()
    //        .map { multipleChoiceQuestionDto ->
    //            MultipleChoiceQuestionDetailsUiState(multipleChoiceQuestionDetails =  multipleChoiceQuestionDto.toMultipleChoiceQuestionDetails(
    //                topicName = if(multipleChoiceQuestionDto.topic != -1)
    //                    topicRepository.getTopicById(multipleChoiceQuestionDto.topic).map{it.topic}.first()
    //                else "",
    //                pointName = if(multipleChoiceQuestionDto.point != -1)
    //                pointRepository.getPointById(multipleChoiceQuestionDto.point).map { it.type }.first()
    //                else ""
    //            ))
    //        }.stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = MultipleChoiceQuestionDetailsUiState()
    //        )

    /**
     * Deletes the item from the [MultipleChoiceQuestionRepository]'s data source.
     */
    suspend fun deleteMultipleChoiceQuestion() {
        ExamAppApi.retrofitService.deleteMultipleChoice(multipleChoiceQuestionId)
        //multipleChoiceQuestionRepository.deleteMultipleChoiceQuestion(uiState.value.multipleChoiceQuestionDetails.toMultipleChoiceQuestion())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class MultipleChoiceQuestionDetailsUiState(
    val multipleChoiceQuestionDetails: MultipleChoiceQuestionDetails = MultipleChoiceQuestionDetails()
)
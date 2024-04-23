package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.TrueFalseQuestionDetailsDestination
import kotlinx.coroutines.launch

sealed interface TrueFalseQuestionDetailsScreenUiState {
    data class Success(val question: TrueFalseQuestionDto) : TrueFalseQuestionDetailsScreenUiState
    object Error : TrueFalseQuestionDetailsScreenUiState
    object Loading : TrueFalseQuestionDetailsScreenUiState
}

class TrueFalseQuestionDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    val trueFalseQuestionId: String = checkNotNull(savedStateHandle[TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg.toString()])

    var trueFalseDetailsScreenUiState: TrueFalseQuestionDetailsScreenUiState by mutableStateOf(TrueFalseQuestionDetailsScreenUiState.Loading)
    var uiState by mutableStateOf(TrueFalseQuestionDetailsUiState())
    init {
        getQuestion(trueFalseQuestionId)
    }

    fun getQuestion(topicId: String){
        trueFalseDetailsScreenUiState = TrueFalseQuestionDetailsScreenUiState.Loading
        viewModelScope.launch {
            //try{
            val result = ExamAppApi.retrofitService.getTrueFalse(topicId)
            trueFalseDetailsScreenUiState =  TrueFalseQuestionDetailsScreenUiState.Success(result)
            uiState = TrueFalseQuestionDetailsUiState(result.toTrueFalseQuestionDetails(
                topicName =
                    if (result.topic == "null") "" //TODO check this
                    else ExamAppApi.retrofitService.getTopic(result.topic).topic,
                pointName =
                    if (result.point == "null") "" //TODO check this
                    else ExamAppApi.retrofitService.getPoint(result.point).type,
            ))
            //} catch (e: IOException) {
            //    TrueFalseQuestionDetailsScreenUiState.Error
            //} /*catch (e: HttpException) {
            TrueFalseQuestionDetailsScreenUiState.Error
            //}
        }
    }
    /**
     * Holds the item details ui state. The data is retrieved from [TrueFalseQuestionRepository] and mapped to
     * the UI state.
     */
    //val uiState: StateFlow<TrueFalseQuestionDetailsUiState> =
    //    trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt())
    //        .filterNotNull()
    //        .map { trueFalseQuestionDto ->
    //            TrueFalseQuestionDetailsUiState(trueFalseQuestionDetails =  trueFalseQuestionDto.toTrueFalseQuestionDetails(
    //                topicName = if(trueFalseQuestionDto.topic != -1)
    //                    topicRepository.getTopicById(trueFalseQuestionDto.topic).map{it.topic}.first()
    //                else "",
    //                pointName = if(trueFalseQuestionDto.point != -1)
    //                pointRepository.getPointById(trueFalseQuestionDto.point).map { it.type }.first()
    //                else ""
    //            ))
    //        }.stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = TrueFalseQuestionDetailsUiState()
    //        )

    /**
     * Deletes the item from the [TrueFalseQuestionRepository]'s data source.
     */
    suspend fun deleteTrueFalseQuestion() {
        ExamAppApi.retrofitService.deleteTrueFalse(trueFalseQuestionId)
        //trueFalseQuestionRepository.deleteTrueFalseQuestion(uiState.value.trueFalseQuestionDetails.toTrueFalseQuestion())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class TrueFalseQuestionDetailsUiState(
    val trueFalseQuestionDetails: TrueFalseQuestionDetails = TrueFalseQuestionDetails()
)
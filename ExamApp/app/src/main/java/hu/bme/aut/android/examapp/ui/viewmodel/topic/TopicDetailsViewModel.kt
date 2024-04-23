package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.PointDto
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.TopicDetailsDestination
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointDetailsScreenUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface TopicDetailsScreenUiState {
    data class Success(val point: TopicDto) : TopicDetailsScreenUiState
    object Error : TopicDetailsScreenUiState
    object Loading : TopicDetailsScreenUiState
}

class TopicDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val topicRepository: TopicRepository,
) : ViewModel() {

    val topicId: String = checkNotNull(savedStateHandle[TopicDetailsDestination.topicIdArg])

    var topicDetailsScreenUiState: TopicDetailsScreenUiState by mutableStateOf(TopicDetailsScreenUiState.Loading)
    var uiState by mutableStateOf(TopicDetailsUiState())
    init {
        getTopic(topicId)
    }

    fun getTopic(topicId: String){
        topicDetailsScreenUiState = TopicDetailsScreenUiState.Loading
        viewModelScope.launch {
            //try{
            val result = ExamAppApi.retrofitService.getTopic(topicId)
            topicDetailsScreenUiState =  TopicDetailsScreenUiState.Success(result)
            uiState = TopicDetailsUiState(result.toTopicDetails(
                parentName =
                if (result.parentTopic == "null") "" //TODO check this
                else
                    ExamAppApi.retrofitService.getTopic(result.parentTopic).topic
            ))
            //} catch (e: IOException) {
            //    PointDetailsScreenUiState.Error
            //} /*catch (e: HttpException) {
            PointDetailsScreenUiState.Error
            //}
        }
    }

    /**
     * Holds the item details ui state. The data is retrieved from [TopicRepository] and mapped to
     * the UI state.
     */
    //val uiState: StateFlow<TopicDetailsUiState> =
    //    topicRepository.getTopicById(topicId.toInt())
    //        .filterNotNull()
    //        .map { topicDto ->
    //            TopicDetailsUiState(topicDetails =  topicDto.toTopicDetails(
    //                parentName = if(topicDto.parentTopic !=-1) topicRepository.getTopicById(topicDto.parentTopic).map{it.topic}.first()
    //                            else ""
    //            ))
    //        }.stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = TopicDetailsUiState()
    //        )



    /**
     * Deletes the item from the [TopicRepository]'s data source.
     */
    suspend fun deleteTopic() {
        viewModelScope.launch {
            ExamAppApi.retrofitService.deleteTopic(topicId)
        }
        //topicRepository.deleteTopic(uiState.value.topicDetails.toTopic())
    }

    //suspend fun getTopicById(id: Int): String {
    //    return ExamAppApi.retrofitService.getTopic(id.toString()).topic
    //    //return topicRepository.getTopicById(id).filterNotNull().map { it.topic }.first()
    //}

    companion object {
        //var topicDetailsScreenEffected = false
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicDetailsScreen
 */
data class TopicDetailsUiState(
    val topicDetails: TopicDetails = TopicDetails()
)
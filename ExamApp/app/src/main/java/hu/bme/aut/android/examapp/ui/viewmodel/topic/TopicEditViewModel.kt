package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.ui.TopicDetailsDestination
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface TopicEditScreenUiState {
    data class Success(val topic: TopicDto) : TopicEditScreenUiState
    data object Error : TopicEditScreenUiState{var errorMessage: String = ""}
    data object Loading : TopicEditScreenUiState
}

class TopicEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val topicRepository: TopicRepository
) : ViewModel() {

    private lateinit var originalTopic: String

    var topicUiState by mutableStateOf(TopicUiState())
        private set

    private val topicId: String = checkNotNull(savedStateHandle[TopicDetailsDestination.topicIdArg])

    var topicEditScreenUiState: TopicEditScreenUiState by mutableStateOf(TopicEditScreenUiState.Loading)

    init {
        getTopic(topicId)
    }

    fun getTopic(topicId: String){
        topicEditScreenUiState = TopicEditScreenUiState.Loading
        viewModelScope.launch {
            topicEditScreenUiState = try{
                val result = ExamAppApi.retrofitService.getTopic(topicId)
                topicUiState = result.toTopicUiState(true,
                    parentName =
                    if (result.parentTopic == "null") ""
                    else
                        ExamAppApi.retrofitService.getTopic(result.parentTopic).topic
                )
                originalTopic = topicUiState.topicDetails.topic
                TopicEditScreenUiState.Success(result)
            } catch (e: IOException) {
                TopicEditScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TopicEditScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TopicEditScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TopicEditScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TopicEditScreenUiState.Error.errorMessage = "Server error"
                    else -> TopicEditScreenUiState.Error
                }
                TopicEditScreenUiState.Error
            }
        }
    }

    suspend fun updateTopic() : Boolean{
        return if (validateInput(topicUiState.topicDetails) && validateUniqueTopic(topicUiState.topicDetails)) {
            viewModelScope.launch {
                ExamAppApi.retrofitService.updateTopic(topicUiState.topicDetails.toTopic())
            }
            true
        }
        else {
            topicUiState = topicUiState.copy(isEntryValid = false)
            false
        }
    }

    fun updateUiState(topicDetails: TopicDetails) {
        topicUiState =
            TopicUiState(topicDetails = topicDetails, isEntryValid = validateInput(topicDetails))
    }

    private fun validateInput(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return with(uiState) {
            topic.isNotBlank() && description.isNotBlank() && !topic.contains("/") && !description.contains("/")
        }
    }

    private suspend fun validateUniqueTopic(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllTopicName().map{it.name}.contains(uiState.topic) || originalTopic == uiState.topic
    }
}




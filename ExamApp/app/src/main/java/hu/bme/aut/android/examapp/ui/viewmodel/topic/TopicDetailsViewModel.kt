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

sealed interface TopicDetailsScreenUiState {
    data class Success(val point: TopicDto) : TopicDetailsScreenUiState
    data object Error : TopicDetailsScreenUiState{var errorMessage: String = ""}
    data object Loading : TopicDetailsScreenUiState
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
            topicDetailsScreenUiState = try{
                val result = ExamAppApi.retrofitService.getTopic(topicId)
                uiState = TopicDetailsUiState(result.toTopicDetails(
                    parentName =
                    if (result.parentTopic == "null") ""
                    else
                        ExamAppApi.retrofitService.getTopic(result.parentTopic).topic
                ))
            TopicDetailsScreenUiState.Success(result)
            } catch (e: IOException) {
                TopicDetailsScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TopicDetailsScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TopicDetailsScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TopicDetailsScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TopicDetailsScreenUiState.Error.errorMessage = "Server error"
                    else -> TopicDetailsScreenUiState.Error
                }
                TopicDetailsScreenUiState.Error
            }
        }
    }

    suspend fun deleteTopic() {
        viewModelScope.launch {
            ExamAppApi.retrofitService.deleteTopic(topicId)
        }
    }

}

data class TopicDetailsUiState(
    val topicDetails: TopicDetails = TopicDetails()
)
package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.NameDto
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface TopicListScreenUiState {
    data class Success(val topics: List<NameDto>) : TopicListScreenUiState
    data object Error : TopicListScreenUiState{var errorMessage: String = ""}
    data object Loading : TopicListScreenUiState
}

class TopicListViewModel : ViewModel() {

    var topicListScreenUiState: TopicListScreenUiState by mutableStateOf(TopicListScreenUiState.Loading)
    var topicListUiState: TopicListUiState by mutableStateOf(TopicListUiState())
    init {
        getAllTopicList()
    }

    fun getAllTopicList(){
        topicListScreenUiState = TopicListScreenUiState.Loading
        viewModelScope.launch {
            topicListScreenUiState = try{
                val result = ExamAppApi.retrofitService.getAllTopicName()
                topicListUiState = TopicListUiState(
                    topicList = result.map { nameDto ->
                        TopicRowUiState(
                            topic = nameDto.name,
                            id = nameDto.uuid
                        )
                    }
                )
                TopicListScreenUiState.Success(result)
            } catch (e: IOException) {
                TopicListScreenUiState.Error.errorMessage = "Network error"
                TopicListScreenUiState.Error
            } catch (e: HttpException) {
                when(e.code()){
                    400 -> TopicListScreenUiState.Error.errorMessage = "Bad request"
                    401 -> TopicListScreenUiState.Error.errorMessage = "Unauthorized try logging in again or open the home screen"
                    404 -> TopicListScreenUiState.Error.errorMessage = "Content not found"
                    500 -> TopicListScreenUiState.Error.errorMessage = "Server error"
                    else -> TopicListScreenUiState.Error
                }
                TopicListScreenUiState.Error
            }
        }
    }

}

data class TopicListUiState(val topicList: List<TopicRowUiState> = listOf(TopicRowUiState()))

data class TopicRowUiState(
    val topic: String = "",
    val id: String = ""
)
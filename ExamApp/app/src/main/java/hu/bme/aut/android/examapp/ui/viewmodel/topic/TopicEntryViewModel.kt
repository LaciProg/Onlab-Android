package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TopicEntryViewModel(private val topicRepository: TopicRepository) : ViewModel(){

    var topicUiState by mutableStateOf(TopicUiState())
        private set

    fun updateUiState(topicDetails: TopicDetails) {
        topicUiState =
            TopicUiState(topicDetails = topicDetails, isEntryValid = validateInput(topicDetails))
    }

    suspend fun saveTopic() : Boolean {
        return if (validateInput() && validateUniqueTopic()) {
            topicRepository.insertTopic(topicUiState.topicDetails.toTopic())
            true
        } else {
            topicUiState = topicUiState.copy(isEntryValid = false)
            false
        }
    }

    /*val parentTopicUiState: StateFlow<String> =
        topicRepository.getTopicsByParentId().map {
            it
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TopicListViewModel.TIMEOUT_MILLIS),
                initialValue = TopicListUiState()
            )*/

    suspend fun getTopicById(id: Int): String {
        return topicRepository.getTopicById(id).filterNotNull().first().topic
    }

    suspend fun getTopicIdByTopic(topic: String): Int {
        return topicRepository.getTopicByTopic(topic).filterNotNull().first().id
    }

    private fun validateInput(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return with(uiState) {
            topic.isNotBlank() && description.isNotBlank() && !topic.contains("/") && !description.contains("/")
        }
    }

    private suspend fun validateUniqueTopic(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return !topicRepository.getAllTopicName().filterNotNull().first().contains(uiState.topic)
    }

}

data class TopicUiState(
    val topicDetails: TopicDetails = TopicDetails(),
    val isEntryValid: Boolean = false
)

data class TopicDetails(
    val id: Int = 0,
    val topic: String = "",
    val parent: Int = -1,
    val description: String = "",
    val parentTopicName : String = ""
)

fun TopicDetails.toTopic(): TopicDto = TopicDto(
    id = id,
    topic = topic,
    parentTopic = parent,
    description = description,
)

fun TopicDto.toTopicUiState(isEntryValid: Boolean = false, parentName: String): TopicUiState = TopicUiState(
    topicDetails = this.toTopicDetails(parentName),
    isEntryValid = isEntryValid
)

fun TopicDto.toTopicDetails(parentName: String): TopicDetails = TopicDetails(
    id = id,
    topic = topic,
    parent = parentTopic,
    description = description,
    parentTopicName = parentName
)
package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TopicEntryViewModel(private val topicRepository: TopicRepository) : ViewModel(){

    var topicUiState by mutableStateOf(TopicUiState())
        private set

    fun updateUiState(topicDetails: TopicDetails) {
        topicUiState =
            TopicUiState(topicDetails = topicDetails, isEntryValid = validateInput(topicDetails))
    }

    suspend fun saveTopic() : Boolean {
        return if (validateInput() && validateUniqueTopic()) {
            viewModelScope.launch {
                ExamAppApi.retrofitService.postTopic(topicUiState.topicDetails.toTopic())
            }
            //topicRepository.insertTopic(topicUiState.topicDetails.toTopic())
            true
        } else {
            topicUiState = topicUiState.copy(isEntryValid = false)
            false
        }
    }

    //suspend fun getTopicById(id: Int): String {
    //    return topicRepository.getTopicById(id).filterNotNull().first().topic
    //}

    suspend fun getTopicIdByTopic(topic: String): String {
        return ExamAppApi.retrofitService.getTopicByTopic(topic)?.uuid ?: ""
        //return topicRepository.getTopicByTopic(topic).filterNotNull().first().id
    }

    private fun validateInput(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return with(uiState) {
            topic.isNotBlank() && description.isNotBlank() && !topic.contains("/") && !description.contains("/")
        }
    }

    private suspend fun validateUniqueTopic(uiState: TopicDetails = topicUiState.topicDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllTopicName().map{it.name}.contains(uiState.topic)
        //return !topicRepository.getAllTopicName().filterNotNull().first().contains(uiState.topic)
    }

}

data class TopicUiState(
    val topicDetails: TopicDetails = TopicDetails(),
    val isEntryValid: Boolean = false
)

data class TopicDetails(
    val id: String = "",
    val topic: String = "",
    val parent: String = "",
    val description: String = "",
    val parentTopicName : String = ""
)

fun TopicDetails.toTopic(): TopicDto = TopicDto(
    uuid = id,
    topic = topic,
    parentTopic = if(parent == "null") "" else parent,
    description = description,
)

fun TopicDto.toTopicUiState(isEntryValid: Boolean = false, parentName: String): TopicUiState = TopicUiState(
    topicDetails = this.toTopicDetails(parentName),
    isEntryValid = isEntryValid
)

fun TopicDto.toTopicDetails(parentName: String): TopicDetails = TopicDetails(
    id = uuid,
    topic = topic,
    parent = parentTopic,
    description = description,
    parentTopicName = parentName
)
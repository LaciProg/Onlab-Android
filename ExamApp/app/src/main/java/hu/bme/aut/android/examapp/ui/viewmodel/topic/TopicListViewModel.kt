package hu.bme.aut.android.examapp.ui.viewmodel.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TopicListViewModel(topicRepository: TopicRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [TopicRepository] and mapped to
     * [TopicListUiState]
     */
    val topicListUiState: StateFlow<TopicListUiState> =
        topicRepository.getAllTopics().map { TopicListUiState(
            topicList = it.map { topicDto ->
                TopicRowUiState(
                    topic = topicDto.topic,
                    id = topicDto.id
                )
            }
        ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TopicListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicListScreen
 */
data class TopicListUiState(val topicList: List<TopicRowUiState> = listOf(TopicRowUiState()))

data class TopicRowUiState(
    val topic: String = "",
    val id: Int = 0
)
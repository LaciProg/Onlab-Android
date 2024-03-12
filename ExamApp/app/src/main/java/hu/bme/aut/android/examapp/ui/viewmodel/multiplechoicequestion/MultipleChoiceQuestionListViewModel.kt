package hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MultipleChoiceQuestionListViewModel(
    multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository,
    topicRepository: TopicRepository
) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [MultipleChoiceQuestionRepository] and mapped to
     * [MultipleChoiceQuestionListUiState]
     */
    val multipleChoiceQuestionListUiState: StateFlow<MultipleChoiceQuestionListUiState> =
        multipleChoiceQuestionRepository.getAllMultipleChoiceQuestions().map { MultipleChoiceQuestionListUiState(
            multipleChoiceQuestionList = it.map { multipleChoiceQuestionDto ->
                MultipleChoiceQuestionRowUiState(
                    question =multipleChoiceQuestionDto.question,
                    answers = multipleChoiceQuestionDto.correctAnswersList.split("¤"),
                    topic = topicRepository.getTopicById(multipleChoiceQuestionDto.topic).map {topic -> topic.topic }.first(),
                    id = multipleChoiceQuestionDto.id
                )
            }
        ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = MultipleChoiceQuestionListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicListScreen
 */
data class MultipleChoiceQuestionListUiState(val multipleChoiceQuestionList: List<MultipleChoiceQuestionRowUiState> = listOf(
    MultipleChoiceQuestionRowUiState("", listOf(""), "", 0),
))

data class MultipleChoiceQuestionRowUiState(
    val question: String = "",
    val answers: List<String> = listOf(""),
    val topic: String = "",
    val id: Int = 0
)
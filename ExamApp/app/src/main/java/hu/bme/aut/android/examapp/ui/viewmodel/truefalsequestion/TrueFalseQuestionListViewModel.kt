package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TrueFalseQuestionListViewModel(
    trueFalseQuestionRepository: TrueFalseQuestionRepository,
    topicRepository: TopicRepository
) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [TrueFalseQuestionRepository] and mapped to
     * [TrueFalseQuestionListUiState]
     */
    val trueFalseQuestionListUiState: StateFlow<TrueFalseQuestionListUiState> =
        trueFalseQuestionRepository.getAllTrueFalseQuestions().map { TrueFalseQuestionListUiState(
            trueFalseQuestionList = it.map { trueFalseQuestionDto ->
                TrueFalseQuestionRowUiState(
                    question = trueFalseQuestionDto.question,
                    answer = trueFalseQuestionDto.correctAnswer,
                    topic = topicRepository.getTopicById(trueFalseQuestionDto.topic).map {topic -> topic.topic }.first(),
                    id = trueFalseQuestionDto.id
                )
            }
        ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TrueFalseQuestionListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for TopicListScreen
 */
data class TrueFalseQuestionListUiState(val trueFalseQuestionList: List<TrueFalseQuestionRowUiState> = listOf(
    TrueFalseQuestionRowUiState("", false, "", 0),
))

data class TrueFalseQuestionRowUiState(
    val question: String = "",
    val answer: Boolean = false,
    val topic: String = "",
    val id: Int = 0
)
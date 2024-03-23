package hu.bme.aut.android.examapp.ui.viewmodel.exam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExamListViewModel(examRepository: ExamRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of items are retrieved from [ExamRepository] and mapped to
     * [ExamListUiState]
     */
    val examListUiState: StateFlow<ExamListUiState> =
        examRepository.getAllExams().map { ExamListUiState(
            examList = it.map { examDto ->
                ExamRowUiState(
                    exam = examDto.name,
                    id = examDto.id
                )
            }
        ) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ExamListUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ExamListScreen
 */
data class ExamListUiState(val examList: List<ExamRowUiState> = listOf(ExamRowUiState()))

data class ExamRowUiState(
    val exam: String = "",
    val id: Int = 0
)
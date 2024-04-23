package hu.bme.aut.android.examapp.ui.viewmodel.exam

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionListUiState
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionRowUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ExamListScreenUiState {
    data class Success(val exams: List<NameDto>) : ExamListScreenUiState
    object Error : ExamListScreenUiState
    object Loading : ExamListScreenUiState
}

class ExamListViewModel(examRepository: ExamRepository) : ViewModel() {

    var examListScreenUiState: ExamListScreenUiState by mutableStateOf(
        ExamListScreenUiState.Loading)
    var examListUiState: ExamListUiState by mutableStateOf(
        ExamListUiState()
    )


    init {
        getAllExamList()
    }

    fun getAllExamList(){
        examListScreenUiState = ExamListScreenUiState.Loading
        viewModelScope.launch {
            //try{
            val result = ExamAppApi.retrofitService.getAllExamName()
            examListScreenUiState =  ExamListScreenUiState.Success(result)
            examListUiState = ExamListUiState(
                examList = result.map { nameDto ->
                    ExamRowUiState(
                        id = nameDto.uuid,
                        exam = nameDto.name,
                    )
                }
            )
            //} catch (e: IOException) {
            //    ExamListScreenUiState.Error
            //} /*catch (e: HttpException) {
            ExamListScreenUiState.Error
            //}
        }
    }

    /**
     * Holds home ui state. The list of items are retrieved from [ExamRepository] and mapped to
     * [ExamListUiState]
     */
    //val examListUiState: StateFlow<ExamListUiState> =
    //    examRepository.getAllExams().map { ExamListUiState(
    //        examList = it.map { examDto ->
    //            ExamRowUiState(
    //                exam = examDto.name,
    //                id = examDto.id
    //            )
    //        }
    //    ) }
    //        .stateIn(
    //            scope = viewModelScope,
    //            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
    //            initialValue = ExamListUiState()
    //        )

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
    val id: String = ""
)
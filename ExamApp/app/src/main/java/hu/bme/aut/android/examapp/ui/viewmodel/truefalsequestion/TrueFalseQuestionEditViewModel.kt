package hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.TopicDto
import hu.bme.aut.android.examapp.api.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.TrueFalseQuestionDetailsDestination
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.toTopicUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed interface TrueFalseQuestionEditScreenUiState {
    data class Success(val question: TrueFalseQuestionDto) : TrueFalseQuestionEditScreenUiState
    object Error : TrueFalseQuestionEditScreenUiState
    object Loading : TrueFalseQuestionEditScreenUiState
}

class TrueFalseQuestionEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val trueFalseQuestionRepository: TrueFalseQuestionRepository,
    private val topicRepository: TopicRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private lateinit var originalQuestion: String
    /**
     * Holds current topic ui state
     */
    var trueFalseQuestionUiState by mutableStateOf(TrueFalseQuestionUiState())
        private set

    private val trueFalseQuestionId: String = checkNotNull(savedStateHandle[TrueFalseQuestionDetailsDestination.trueFalseQuestionIdArg])

    var trueFalseEditScreenUiState: TrueFalseQuestionEditScreenUiState by mutableStateOf(TrueFalseQuestionEditScreenUiState.Loading)

    init {
        getTrueFalseQuestion(trueFalseQuestionId)
    }

    fun getTrueFalseQuestion(topicId: String){
        trueFalseEditScreenUiState = TrueFalseQuestionEditScreenUiState.Loading
        viewModelScope.launch {
            //try{
            val result = ExamAppApi.retrofitService.getTrueFalse(topicId)
            trueFalseEditScreenUiState =  TrueFalseQuestionEditScreenUiState.Success(result)
            trueFalseQuestionUiState = result.toTrueFalseQuestionUiState(isEntryValid = true,
                topicName =
                    if (result.topic == "null") "" //TODO check this
                    else ExamAppApi.retrofitService.getTopic(result.topic).topic,
                pointName =
                    if (result.point == "null") "" //TODO check this
                    else ExamAppApi.retrofitService.getPoint(result.point).type,
                isAnswerChosen = true
            )
            originalQuestion = trueFalseQuestionUiState.trueFalseQuestionDetails.question
            //} catch (e: IOException) {
            //    TrueFalseQuestionEditScreenUiState.Error
            //} /*catch (e: HttpException) {
            TrueFalseQuestionEditScreenUiState.Error
            //}
        }
    }

    //init {
    //    viewModelScope.launch {
    //        trueFalseQuestionUiState = trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt())
    //            .filterNotNull()
    //            .first()
    //            .toTrueFalseQuestionUiState(
    //                isEntryValid = true,
    //                topicName = topicRepository.getTopicById(trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt()).map { it.topic }.first()).map { it.topic }.first(),
    //                pointName = pointRepository.getPointById(trueFalseQuestionRepository.getTrueFalseQuestionById(trueFalseQuestionId.toInt()).map{ it.point }.first()).map{ it.type }.first(),
    //                isAnswerChosen = true
    //            )
    //        originalQuestion = trueFalseQuestionUiState.trueFalseQuestionDetails.question
    //    }
    //}

    /**
     * Update the topic in the [TrueFalseQuestionRepository]'s data source
     */
    suspend fun updateTrueFalseQuestion() : Boolean{
        return if (validateInput(trueFalseQuestionUiState.trueFalseQuestionDetails) && validateUniqueTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails)) {
            ExamAppApi.retrofitService.updateTrueFalse(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
            //trueFalseQuestionRepository.updateTrueFalseQuestion(trueFalseQuestionUiState.trueFalseQuestionDetails.toTrueFalseQuestion())
            true
        }
        else {
            trueFalseQuestionUiState = trueFalseQuestionUiState.copy(isEntryValid = false)
            false
        }
    }

    /**
     * Updates the [trueFalseQuestionUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(trueFalseQuestionDetails: TrueFalseQuestionDetails) {
        trueFalseQuestionUiState =
            TrueFalseQuestionUiState(trueFalseQuestionDetails = trueFalseQuestionDetails, isEntryValid = validateInput(trueFalseQuestionDetails))
    }

    private fun validateInput(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return with(uiState) {
            question.isNotBlank() && isAnswerChosen && point!= "" && topic != "" && !question.contains("/")
        }
    }

    private suspend fun validateUniqueTrueFalseQuestion(uiState: TrueFalseQuestionDetails = trueFalseQuestionUiState.trueFalseQuestionDetails): Boolean {
        return !ExamAppApi.retrofitService.getAllTrueFalse().map{it.question}.contains(uiState.question) || originalQuestion == uiState.question
        //return !trueFalseQuestionRepository.getAllTrueFalseQuestionQuestion().filterNotNull().first().contains(uiState.question) || originalQuestion == uiState.question
    }
}




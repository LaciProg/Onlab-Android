package hu.bme.aut.android.examapp.ui.multiplechoicequestion

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.truefalsequestion.TrueFalseQuestionEditResultScreen
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEditScreenUiState
import kotlinx.coroutines.launch

@Composable
fun MultipleChoiceQuestionEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MultipleChoiceQuestionEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (viewModel.multipleChoiceEditScreenUiState) {
        is MultipleChoiceQuestionEditScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is MultipleChoiceQuestionEditScreenUiState.Success -> MultipleChoiceQuestionEditResultScreen(
            navigateBack = navigateBack,
            viewModel = viewModel,
            modifier = modifier
        )
        is MultipleChoiceQuestionEditScreenUiState.Error -> androidx.compose.material3.Text(text = "Error...")
    }

    //val coroutineScope = rememberCoroutineScope()
    //val context = LocalContext.current
    //MultipleChoiceQuestionEntryBody(
    //    multipleChoiceQuestionUiState = viewModel.multipleChoiceQuestionUiState,
    //    onMultipleChoiceQuestionValueChange = viewModel::updateUiState,
    //    onSaveClick = {
    //        coroutineScope.launch {
    //            if(viewModel.updateMultipleChoiceQuestion()){
    //                navigateBack()
    //            }
    //            else{
    //                Toast.makeText(
    //                    context,
    //                    "Question with this name already exists",
    //                    Toast.LENGTH_LONG
    //                ).show()
    //            }
    //        }
    //    },
    //    modifier = modifier
    //)

}

@Composable
fun MultipleChoiceQuestionEditResultScreen(
    navigateBack: () -> Unit,
    viewModel: MultipleChoiceQuestionEditViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    MultipleChoiceQuestionEntryBody(
        multipleChoiceQuestionUiState = viewModel.multipleChoiceQuestionUiState,
        onMultipleChoiceQuestionValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                if(viewModel.updateMultipleChoiceQuestion()){
                    navigateBack()
                }
                else{
                    Toast.makeText(
                        context,
                        "Question with this name already exists",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        },
        modifier = modifier
    )
}
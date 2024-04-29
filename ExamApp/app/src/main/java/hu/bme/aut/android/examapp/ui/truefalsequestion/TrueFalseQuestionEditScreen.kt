package hu.bme.aut.android.examapp.ui.truefalsequestion

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEditViewModel
import kotlinx.coroutines.launch

@Composable
fun TrueFalseQuestionEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrueFalseQuestionEditViewModel = hiltViewModel()//viewModel(factory = AppViewModelProvider.Factory)
) {
    when (viewModel.trueFalseEditScreenUiState) {
        is TrueFalseQuestionEditScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is TrueFalseQuestionEditScreenUiState.Success -> TrueFalseQuestionEditResultScreen(
            navigateBack = navigateBack,
            viewModel = viewModel,
            modifier = modifier
        )
        is TrueFalseQuestionEditScreenUiState.Error -> Text(text = TrueFalseQuestionEditScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
    }
}

@Composable
fun TrueFalseQuestionEditResultScreen(
    navigateBack: () -> Unit,
    viewModel: TrueFalseQuestionEditViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    TrueFalseQuestionEntryBody(
        trueFalseQuestionUiState = viewModel.trueFalseQuestionUiState,
        onTrueFalseQuestionValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                if(viewModel.updateTrueFalseQuestion()){
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
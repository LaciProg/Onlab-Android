package hu.bme.aut.android.examapp.ui.multiplechoicequestion

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEditViewModel
import kotlinx.coroutines.launch

@Composable
fun MultipleChoiceQuestionEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MultipleChoiceQuestionEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    MultipleChoiceQuestionEntryBody(
        multipleChoiceQuestionUiState = viewModel.multipleChoiceQuestionUiState,
        onMultipleChoiceQuestionValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.updateMultipleChoiceQuestion()
                navigateBack()
            }
        },
        modifier = modifier
    )

}
package hu.bme.aut.android.examapp.ui.exam

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEditViewModel
import kotlinx.coroutines.launch

@Composable
fun ExamEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExamEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    ExamEntryBody(
        examUiState = viewModel.examUiState,
        onExamValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                if(viewModel.updateExam()){
                    navigateBack()
                }
                else{
                    Toast.makeText(
                        context,
                        "Exam with this name already exists",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        },
        modifier = modifier
    )

}
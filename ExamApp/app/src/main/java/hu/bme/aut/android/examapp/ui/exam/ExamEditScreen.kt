package hu.bme.aut.android.examapp.ui.exam

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEditViewModel
import kotlinx.coroutines.launch

@Composable
fun ExamEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExamEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when(viewModel.examEditScreenUiState){
        is ExamEditScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is ExamEditScreenUiState.Success -> ExamEditResultScreen(
            navigateBack = navigateBack,
            viewModel = viewModel,
            modifier = modifier
        )
        is ExamEditScreenUiState.Error -> androidx.compose.material3.Text(text = "Error...")
    }

    //val coroutineScope = rememberCoroutineScope()
    //val context = LocalContext.current
    //ExamEntryBody(
    //    examUiState = viewModel.examUiState,
    //    onExamValueChange = viewModel::updateUiState,
    //    onSaveClick = {
    //        coroutineScope.launch {
    //            if(viewModel.updateExam()){
    //                navigateBack()
    //            }
    //            else{
    //                Toast.makeText(
    //                    context,
    //                    "Exam with this name already exists",
    //                    Toast.LENGTH_LONG
    //                ).show()
    //            }
    //        }
    //    },
    //    modifier = modifier
    //)

}

@Composable
fun ExamEditResultScreen(
    navigateBack: () -> Unit,
    viewModel: ExamEditViewModel,
    modifier: Modifier = Modifier
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
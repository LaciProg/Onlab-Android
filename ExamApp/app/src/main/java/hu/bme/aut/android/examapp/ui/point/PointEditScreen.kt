package hu.bme.aut.android.examapp.ui.point

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEditViewModel
import kotlinx.coroutines.launch

@Composable
fun PointEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PointEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (viewModel.pointEditScreenUiState) {
        is PointEditScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is PointEditScreenUiState.Success -> PointEditResultScreen(
            navigateBack = navigateBack,
            viewModel = viewModel,
            modifier = modifier
        )
        is PointEditScreenUiState.Error -> Text(text = PointEditScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " })
    }

}

@Composable
fun PointEditResultScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PointEditViewModel
){
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    PointEntryBody(
        pointUiState = viewModel.pointUiState,
        onPointValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                if(viewModel.updatePoint()) { navigateBack() }
                else{
                    Toast.makeText(
                        context,
                        "Point with this name already exists",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        },
        modifier = modifier
    )
}

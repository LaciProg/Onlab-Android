package hu.bme.aut.android.examapp.ui.topic

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.point.PointEditResultScreen
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetailsScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditViewModel
import kotlinx.coroutines.launch

@Composable
fun TopicEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    when (viewModel.topicEditScreenUiState) {
        is TopicEditScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is TopicEditScreenUiState.Success -> TopicEditResultScreen(
            navigateBack = navigateBack,
            viewModel = viewModel,
            modifier = modifier
        )
        is TopicEditScreenUiState.Error -> androidx.compose.material3.Text(text = "Error...")
    }
    //val coroutineScope = rememberCoroutineScope()
    //val context = LocalContext.current
    //TopicEntryBody(
    //    topicUiState = viewModel.topicUiState,
    //    onTopicValueChange = viewModel::updateUiState,
    //    onSaveClick = {
    //        coroutineScope.launch {
    //            if(viewModel.updateTopic()){
    //                navigateBack()
    //            }
    //            else{
    //                Toast.makeText(
    //                    context,
    //                    "Topic with this name already exists",
    //                    Toast.LENGTH_LONG
    //                ).show()
    //            }
    //        }
    //    },
    //    modifier = modifier
    //)

}

@Composable
fun TopicEditResultScreen(
    navigateBack: () -> Unit,
    viewModel: TopicEditViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    TopicEntryBody(
        topicUiState = viewModel.topicUiState,
        onTopicValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                if(viewModel.updateTopic()){
                    navigateBack()
                }
                else{
                    Toast.makeText(
                        context,
                        "Topic with this name already exists",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        },
        modifier = modifier
    )
}
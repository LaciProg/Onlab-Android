package hu.bme.aut.android.examapp.ui.topic

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.TopicDetails
import hu.bme.aut.android.examapp.ui.viewmodel.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.TopicEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.TopicUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun TopicDetails(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    TopicEntryBody(
        topicUiState = viewModel.topicUiState,
        onTopicValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.updateTopic()
                navigateBack()
            }
        },
        modifier = modifier
    )

}
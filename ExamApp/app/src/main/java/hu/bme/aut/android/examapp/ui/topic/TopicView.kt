package hu.bme.aut.android.examapp.ui.topic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.ExamDestination
import hu.bme.aut.android.examapp.ui.ExamTabRow
import hu.bme.aut.android.examapp.ui.NewTopic
import hu.bme.aut.android.examapp.ui.TopicDetails
import hu.bme.aut.android.examapp.ui.viewmodel.TopicListViewModel
import kotlinx.coroutines.flow.count

@Composable
fun TopicView(
    addNewTopic: () -> Unit = {},
    navigateToTopicDetails: (String) -> Unit,
    //navigateToItemEntry: () -> Unit,
    //navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicListViewModel = viewModel(factory = AppViewModelProvider.Factory)
  ){
    val topicUiState by viewModel.topicListUiState.collectAsState()
    Scaffold(
        topBar = { Text("Topic list") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewTopic() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        LazyColumn(contentPadding = padding) {
            items(topicUiState.itemList){
                TextButton(onClick = { navigateToTopicDetails(it) }) {
                    Text(it)
                }
            }
        }
    }
}
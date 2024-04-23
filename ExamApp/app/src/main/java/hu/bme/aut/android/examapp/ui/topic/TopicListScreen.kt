package hu.bme.aut.android.examapp.ui.topic

import androidx.annotation.XmlRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.point.PointListResultScreen
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel

@Composable
fun TopicListScreen(
    modifier: Modifier = Modifier,
    addNewTopic: () -> Unit = {},
    navigateToTopicDetails: (String) -> Unit,
    viewModel: TopicListViewModel = viewModel(factory = AppViewModelProvider.Factory)
  ){
    when(viewModel.topicListScreenUiState){
        is TopicListScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is TopicListScreenUiState.Success -> TopicListResultScreen(
            topics =  (viewModel.topicListScreenUiState as TopicListScreenUiState.Success).topics,
            addNewPoint = addNewTopic,
            navigateToPointDetails = navigateToTopicDetails,
            viewModel = viewModel,
        )
        is TopicListScreenUiState.Error -> Text(text = "Error...")
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllTopicList()
    }

    /*val topicUiState by viewModel.topicListUiState.collectAsState()
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
            items(topicUiState.topicList){
                TextButton(onClick = { navigateToTopicDetails(it.id) }) {
                    Text(it.topic)
                }
            }
        }
    }*/
}

@Composable
fun TopicListResultScreen(
    topics: List<NameDto>,
    addNewPoint: () -> Unit,
    navigateToPointDetails: (String) -> Unit,
    viewModel: TopicListViewModel
){



    Scaffold(
        topBar = {  },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewPoint() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        LazyColumn(contentPadding = padding) {
            items(topics){
                TextButton(onClick = { navigateToPointDetails(it.uuid) }) {
                    Text(it.name)
                }
            }
        }
    }
}
package hu.bme.aut.android.examapp.ui.point

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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.examapp.api.dto.NameDto
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListScreenUiState
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListViewModel

@Composable
fun PointListScreen(
    modifier: Modifier = Modifier,
    addNewPoint: () -> Unit = {},
    navigateToPointDetails: (String) -> Unit,
    viewModel: PointListViewModel = viewModel(factory = AppViewModelProvider.Factory)
  ){
    when(viewModel.pointListScreenUiState){
        is PointListScreenUiState.Loading -> CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        is PointListScreenUiState.Success -> PointListResultScreen(
            points =  (viewModel.pointListScreenUiState as PointListScreenUiState.Success).points,
            addNewPoint = addNewPoint,
            navigateToPointDetails = navigateToPointDetails,
            viewModel = viewModel
            )
        is PointListScreenUiState.Error -> Text(text = PointListScreenUiState.Error.errorMessage.ifBlank { "Unexpected error " }, modifier = Modifier.fillMaxSize())
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllPointList()
    }

}

@Composable
fun PointListResultScreen(
    points: List<NameDto>,
    addNewPoint: () -> Unit = {},
    navigateToPointDetails: (String) -> Unit,
    viewModel: PointListViewModel
) {
    Scaffold(
        topBar = { },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewPoint() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        LazyColumn(contentPadding = padding) {
            items(points){
                TextButton(onClick = { navigateToPointDetails(it.uuid) }) {
                    Text(it.name)
                }
            }
        }
    }
}
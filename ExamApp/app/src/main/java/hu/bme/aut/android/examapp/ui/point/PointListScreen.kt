package hu.bme.aut.android.examapp.ui.point

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
import hu.bme.aut.android.examapp.ui.AppViewModelProvider
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListViewModel

@Composable
fun PointListScreen(
    modifier: Modifier = Modifier,
    addNewPoint: () -> Unit = {},
    navigateToPointDetails: (Int) -> Unit,
    //navigateToItemEntry: () -> Unit,
    //navigateToItemUpdate: (Int) -> Unit,
    viewModel: PointListViewModel = viewModel(factory = AppViewModelProvider.Factory)
  ){
    val pointUiState by viewModel.pointListUiState.collectAsState()
    Scaffold(
        topBar = { Text("Point list") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewPoint() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        LazyColumn(contentPadding = padding) {
            items(pointUiState.pointList){
                TextButton(onClick = { navigateToPointDetails(it.id) }) {
                    Text(it.point)
                }
            }
        }
    }
}
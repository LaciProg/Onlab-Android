package hu.bme.aut.android.examapp.ui.exam

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
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamListViewModel

@Composable
fun ExamListScreen(
    modifier: Modifier = Modifier,
    addNewExam: () -> Unit = {},
    navigateToExamDetails: (Int) -> Unit,
    viewModel: ExamListViewModel = viewModel(factory = AppViewModelProvider.Factory)
  ){
    val examUiState by viewModel.examListUiState.collectAsState()
    Scaffold(
        topBar = { Text("Exam list") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addNewExam() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        LazyColumn(contentPadding = padding) {
            items(examUiState.examList){
                TextButton(onClick = { navigateToExamDetails(it.id) }) {
                    Text(it.exam)
                }
            }
        }
    }
}
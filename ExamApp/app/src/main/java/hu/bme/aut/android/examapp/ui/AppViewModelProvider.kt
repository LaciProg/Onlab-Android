package hu.bme.aut.android.examapp.ui

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.examapp.ExamApplication
import hu.bme.aut.android.examapp.ui.viewmodel.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.TopicEntryViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import hu.bme.aut.android.examapp.ui.viewmodel.TopicListViewModel


object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TopicEditViewModel(
                this.createSavedStateHandle(),
                examApplication().container.topicRepository
            )
        }

        initializer {
            TopicEntryViewModel(examApplication().container.topicRepository)
        }

        initializer {
            TopicListViewModel(examApplication().container.topicRepository)
        }
    }
}

fun CreationExtras.examApplication(): ExamApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExamApplication)
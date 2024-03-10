package hu.bme.aut.android.examapp.ui

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.examapp.ExamApplication
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEntryViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TypeRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.type.TypeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        /**
         * Initializes the TypeViewModels with the [TypeRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TypeViewModel(examApplication().container.typeRepository)
        }

        /**
         * Initializes the TopicViewModels with the [TopicRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TopicListViewModel(examApplication().container.topicRepository)
        }

        initializer {
            TopicEntryViewModel(examApplication().container.topicRepository)
        }

        initializer {
            TopicDetailsViewModel(
                this.createSavedStateHandle(),
                examApplication().container.topicRepository
            )
        }

        initializer {
            TopicEditViewModel(
                this.createSavedStateHandle(),
                examApplication().container.topicRepository
            )
        }


        /**
         * Initializes the PointViewModels with the [PointRepository] from the [ExamApplication]'s container.
         */
        initializer {
            PointListViewModel(examApplication().container.pointRepository)
        }

        initializer {
            PointEntryViewModel(examApplication().container.pointRepository)
        }

        initializer {
            PointDetailsViewModel(
                this.createSavedStateHandle(),
                examApplication().container.pointRepository
            )
        }

        initializer {
            PointEditViewModel(
                this.createSavedStateHandle(),
                examApplication().container.pointRepository
            )
        }

        /**
         * Initializes the TrueFalseQuestionViewModels with the [TrueFalseQuestionRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TrueFalseQuestionListViewModel(
                trueFalseQuestionRepository =  examApplication().container.trueFalseQuestionRepository,
                topicRepository =  examApplication().container.topicRepository
            )
        }

        initializer {
            TrueFalseQuestionDetailsViewModel(
                this.createSavedStateHandle(),
                examApplication().container.trueFalseQuestionRepository,
                examApplication().container.topicRepository,
                examApplication().container.pointRepository

            )
        }

        initializer {
            TrueFalseQuestionEntryViewModel(
                examApplication().container.trueFalseQuestionRepository
            )
        }
    }
}

fun CreationExtras.examApplication(): ExamApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExamApplication)
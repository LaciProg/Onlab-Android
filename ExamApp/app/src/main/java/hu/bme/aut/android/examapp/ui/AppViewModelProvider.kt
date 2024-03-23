package hu.bme.aut.android.examapp.ui

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.examapp.ExamApplication
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEntryViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.InitializerViewModelFactoryBuilder
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TypeRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.exam.ExamListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.multiplechoicequestion.MultipleChoiceQuestionListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.point.PointListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.type.TypeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        typeViewModel()

        topicViewModel()

        pointViewModel()

        trueFalseViewModel()

        multipleChoiceViewModel()

        examViewModel()
    }


    private fun InitializerViewModelFactoryBuilder.typeViewModel() {
        /**
         * Initializes the TypeViewModels with the [TypeRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TypeViewModel(examApplication().container.typeRepository)
        }
    }

    private fun InitializerViewModelFactoryBuilder.topicViewModel() {

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
    }

    private fun InitializerViewModelFactoryBuilder.pointViewModel() {
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
    }

    private fun InitializerViewModelFactoryBuilder.trueFalseViewModel() {
        /**
         * Initializes the TrueFalseQuestionViewModels with the [TrueFalseQuestionRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TrueFalseQuestionListViewModel(
                trueFalseQuestionRepository = examApplication().container.trueFalseQuestionRepository,
                topicRepository = examApplication().container.topicRepository
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
            TrueFalseQuestionEditViewModel(
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

    private fun InitializerViewModelFactoryBuilder.multipleChoiceViewModel() {
        initializer {
            MultipleChoiceQuestionListViewModel(
                examApplication().container.multipleChoiceQuestionRepository,
                examApplication().container.topicRepository
            )
        }

        initializer {
            MultipleChoiceQuestionDetailsViewModel(
                this.createSavedStateHandle(),
                examApplication().container.multipleChoiceQuestionRepository,
                examApplication().container.topicRepository,
                examApplication().container.pointRepository
            )
        }

        initializer {
            MultipleChoiceQuestionEditViewModel(
                this.createSavedStateHandle(),
                examApplication().container.multipleChoiceQuestionRepository,
                examApplication().container.topicRepository,
                examApplication().container.pointRepository
            )
        }

        initializer {
            MultipleChoiceQuestionEntryViewModel(
                examApplication().container.multipleChoiceQuestionRepository
            )
        }
    }

    private fun InitializerViewModelFactoryBuilder.examViewModel() {
        initializer {
            ExamListViewModel(
                examApplication().container.examRepository
            )
        }

        initializer {
            ExamDetailsViewModel(
                this.createSavedStateHandle(),
                examApplication().container.examRepository,
                examApplication().container.trueFalseQuestionRepository,
                examApplication().container.multipleChoiceQuestionRepository,
                examApplication().container.topicRepository,
                examApplication().container.pointRepository
            )
        }

        initializer {
            ExamEditViewModel(
                this.createSavedStateHandle(),
                examApplication().container.topicRepository,
                examApplication().container.examRepository,
                examApplication().container.trueFalseQuestionRepository,
                examApplication().container.multipleChoiceQuestionRepository
            )
        }

        initializer {
            ExamEntryViewModel(
                examApplication().container.examRepository,
                examApplication().container.topicRepository
            )
        }
    }

}

fun CreationExtras.examApplication(): ExamApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExamApplication)
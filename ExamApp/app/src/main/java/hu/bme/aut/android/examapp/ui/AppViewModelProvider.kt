package hu.bme.aut.android.examapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.InitializerViewModelFactoryBuilder
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.examapp.ExamApplication
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.ui.viewmodel.MainScreenViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.auth.LoginUserViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.auth.RegisterUserViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.auth.usecases.IsEmailValidUseCase
import hu.bme.aut.android.examapp.ui.viewmodel.auth.usecases.PasswordsMatchUseCase
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
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.topic.TopicListViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionDetailsViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEditViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionEntryViewModel
import hu.bme.aut.android.examapp.ui.viewmodel.truefalsequestion.TrueFalseQuestionListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        topicViewModel()

        pointViewModel()

        trueFalseViewModel()

        multipleChoiceViewModel()

        examViewModel()

        authViewModels()

        initializer { MainScreenViewModel(ExamApplication.authService) }
    }


    private fun InitializerViewModelFactoryBuilder.topicViewModel() {

        /**
         * Initializes the TopicViewModels with the [TopicRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TopicListViewModel()
        }

        initializer {
            TopicEntryViewModel()
        }

        initializer {
            TopicDetailsViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            TopicEditViewModel(
                this.createSavedStateHandle()
            )
        }
    }

    private fun InitializerViewModelFactoryBuilder.pointViewModel() {
        /**
         * Initializes the PointViewModels with the [PointRepository] from the [ExamApplication]'s container.
         */
        initializer {
            PointListViewModel()
        }

        initializer {
            PointEntryViewModel()
        }

        initializer {
            PointDetailsViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            PointEditViewModel(
                this.createSavedStateHandle()
            )
        }
    }

    private fun InitializerViewModelFactoryBuilder.trueFalseViewModel() {
        /**
         * Initializes the TrueFalseQuestionViewModels with the [TrueFalseQuestionRepository] from the [ExamApplication]'s container.
         */
        initializer {
            TrueFalseQuestionListViewModel()
        }

        initializer {
            TrueFalseQuestionDetailsViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            TrueFalseQuestionEditViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            TrueFalseQuestionEntryViewModel()
        }
    }

    private fun InitializerViewModelFactoryBuilder.multipleChoiceViewModel() {
        initializer {
            MultipleChoiceQuestionListViewModel()
        }

        initializer {
            MultipleChoiceQuestionDetailsViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            MultipleChoiceQuestionEditViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            MultipleChoiceQuestionEntryViewModel()
        }
    }

    private fun InitializerViewModelFactoryBuilder.examViewModel() {
        initializer {
            ExamListViewModel()
        }

        initializer {
            ExamDetailsViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            ExamEditViewModel(
                this.createSavedStateHandle()
            )
        }

        initializer {
            ExamEntryViewModel()
        }
    }

    private fun InitializerViewModelFactoryBuilder.authViewModels() {

        initializer {
            val authService = ExamApplication.authService
            val isEmailValidUseCase = IsEmailValidUseCase()
            LoginUserViewModel(
                authService,
                isEmailValidUseCase,
                examApplication().container.userRepository
            )
        }

        initializer {
            val authService = ExamApplication.authService
            val isEmailValidUseCase = IsEmailValidUseCase()
            val passwordsMatchUseCase = PasswordsMatchUseCase()
            RegisterUserViewModel(
                authService,
                isEmailValidUseCase,
                passwordsMatchUseCase
            )
        }
    }

}

fun CreationExtras.examApplication(): ExamApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExamApplication)
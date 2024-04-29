package hu.bme.aut.android.examapp.ui

/*
object AppViewModelProvider {
    val Factory = viewModelFactory {

        //topicViewModel()

        //pointViewModel()

        //trueFalseViewModel()

        //multipleChoiceViewModel()

        //examViewModel()

        //authViewModels()

        //initializer { MainScreenViewModel(ExamApplication.authService) }
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

        /*initializer {
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
        }*/
    }

}*/
/*
fun CreationExtras.examApplication(): ExamApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ExamApplication)*/
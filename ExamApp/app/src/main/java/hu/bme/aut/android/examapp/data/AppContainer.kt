package hu.bme.aut.android.examapp.data

/*
interface AppContainer {
    val examRepository: ExamRepository
    val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository
    val pointRepository: PointRepository
    val topicRepository: TopicRepository
    val trueFalseQuestionRepository: TrueFalseQuestionRepository
    val typeRepository: TypeRepository
    //val userRepository: UserRepository
}
class AppDataContainer(private val context: Context) : AppContainer {
    override val typeRepository: TypeRepository by lazy { OfflineTypeRepository(DatabaseModule.getInstance(context).typeDao()) }
    override val examRepository: ExamRepository by lazy { OfflineExamRepository(DatabaseModule.getInstance(context).examDao()) }
    override val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository by lazy { OfflineMultipleChoiceQuestionRepository(DatabaseModule.getInstance(context).multipleChoiceQuestionDao()) }
    override val pointRepository: PointRepository by lazy { OfflinePointRepository(DatabaseModule.getInstance(context).pointDao()) }
    override val topicRepository: TopicRepository by lazy { OfflineTopicRepository(DatabaseModule.getInstance(context).topicDao()) }
    override val trueFalseQuestionRepository: TrueFalseQuestionRepository by lazy { OfflineTrueFalseQuestionRepository(DatabaseModule.getInstance(context).trueFalseQuestionDao()) }
    //override val userRepository: UserRepository by lazy { OfflineUserRepository(DatabaseModule.getInstance(context).userDao()) }
}*/
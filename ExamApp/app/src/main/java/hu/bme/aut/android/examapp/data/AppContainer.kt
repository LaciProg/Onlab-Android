package hu.bme.aut.android.examapp.data

import android.content.Context
import hu.bme.aut.android.examapp.data.repositories.inrefaces.ExamRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.MultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.PointRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TopicRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TrueFalseQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.TypeRepository
import hu.bme.aut.android.examapp.data.repositories.inrefaces.UserRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineExamRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineMultipleChoiceQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflinePointRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineTopicRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineTrueFalseQuestionRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineTypeRepository
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineUserRepository
import hu.bme.aut.android.examapp.data.room.ExamDatabase

interface AppContainer {
    val examRepository: ExamRepository
    val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository
    val pointRepository: PointRepository
    val topicRepository: TopicRepository
    val trueFalseQuestionRepository: TrueFalseQuestionRepository
    val typeRepository: TypeRepository
    val userRepository: UserRepository
}
class AppDataContainer(private val context: Context) : AppContainer {
    override val typeRepository: TypeRepository by lazy { OfflineTypeRepository(ExamDatabase.getInstance(context).typeDao()) }
    override val examRepository: ExamRepository by lazy { OfflineExamRepository(ExamDatabase.getInstance(context).examDao()) }
    override val multipleChoiceQuestionRepository: MultipleChoiceQuestionRepository by lazy { OfflineMultipleChoiceQuestionRepository(ExamDatabase.getInstance(context).multipleChoiceQuestionDao()) }
    override val pointRepository: PointRepository by lazy { OfflinePointRepository(ExamDatabase.getInstance(context).pointDao()) }
    override val topicRepository: TopicRepository by lazy { OfflineTopicRepository(ExamDatabase.getInstance(context).topicDao()) }
    override val trueFalseQuestionRepository: TrueFalseQuestionRepository by lazy { OfflineTrueFalseQuestionRepository(ExamDatabase.getInstance(context).trueFalseQuestionDao()) }
    override val userRepository: UserRepository by lazy { OfflineUserRepository(ExamDatabase.getInstance(context).userDao()) }
}
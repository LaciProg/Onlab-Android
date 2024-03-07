package hu.bme.aut.android.examapp.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hu.bme.aut.android.examapp.data.room.dao.ExamDao
import hu.bme.aut.android.examapp.data.room.dao.MultipleChoiceQuestionDao
import hu.bme.aut.android.examapp.data.room.dao.PointDao
import hu.bme.aut.android.examapp.data.room.dao.TopicDao
import hu.bme.aut.android.examapp.data.room.dao.TrueFalseQuestionDao
import hu.bme.aut.android.examapp.data.room.dao.TypeDao
import hu.bme.aut.android.examapp.data.room.dto.ExamDto
import hu.bme.aut.android.examapp.data.room.dto.MultipleChoiceQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.PointDto
import hu.bme.aut.android.examapp.data.room.dto.TopicDto
import hu.bme.aut.android.examapp.data.room.dto.TrueFalseQuestionDto
import hu.bme.aut.android.examapp.data.room.dto.TypeDto

@Database(entities = [
        ExamDto::class,
        TrueFalseQuestionDto::class,
        MultipleChoiceQuestionDto::class,
        TopicDto::class,
        TypeDto::class,
        PointDto::class
    ],
    version = 4,
    exportSchema = false)
abstract class ExamDatabase : RoomDatabase() {
    abstract fun examDao(): ExamDao
    abstract fun trueFalseQuestionDao(): TrueFalseQuestionDao
    abstract fun multipleChoiceQuestionDao(): MultipleChoiceQuestionDao
    abstract fun topicDao(): TopicDao
    abstract fun typeDao(): TypeDao
    abstract fun pointDao(): PointDao

    companion object {
        @Volatile
        private var INSTANCE: ExamDatabase? = null
        fun getInstance(context: Context): ExamDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ExamDatabase::class.java, "ExamDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
package hu.bme.aut.android.examapp.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.examapp.data.room.ExamDatabase
import hu.bme.aut.android.examapp.data.room.dao.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule  {
    @Provides
    @Singleton
    fun getInstance(
        @ApplicationContext context: Context
    ): ExamDatabase = Room.databaseBuilder(
        context,
        ExamDatabase::class.java,
        "ExamDatabase"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserDao(
        db: ExamDatabase
    ): UserDao = db.userDao

}
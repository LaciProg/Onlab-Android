package hu.bme.aut.android.examapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.examapp.data.room.dto.ExamDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExam(exam: ExamDto)

    @Update
    suspend fun updateExam(exam: ExamDto)

    @Delete
    suspend fun deleteExam(exam: ExamDto)

    @Query("SELECT * FROM exam")
    fun getAllExams(): Flow<List<ExamDto>>

    @Query("SELECT * FROM exam WHERE id = :id")
    fun getExamById(id: Int): Flow<ExamDto>

    @Query("SELECT * FROM exam WHERE name = :name")
    fun getExamByName(name: String): Flow<ExamDto>

    @Query("SELECT name FROM exam ORDER BY name ASC")
    fun getAllExamName(): Flow<List<String>>

    @Query("SELECT * FROM exam WHERE topicId = :topicId")
    fun getExamsByTopic(topicId: Int): Flow<List<ExamDto>>

    @Query("SELECT name FROM exam WHERE topicId = :topicId")
    fun getExamNamesByTopic(topicId: Int): Flow<List<String>>

}
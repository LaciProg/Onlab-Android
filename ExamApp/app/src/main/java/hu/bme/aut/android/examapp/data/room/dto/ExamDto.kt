package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exam",
    foreignKeys = [
        androidx.room.ForeignKey(
        entity = TopicDto::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("topicId"),
        onDelete = androidx.room.ForeignKey.SET_DEFAULT
    )],
    indices = [androidx.room.Index(value = ["topicId"])]
)
data class ExamDto (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "questionList") var questionList: String,
    @ColumnInfo(name = "topicId") var topicId: Int
)
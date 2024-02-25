package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "trueFalseQuestion",
    foreignKeys = [
        ForeignKey(
            entity = TopicDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("topicId"),
            onDelete = ForeignKey.SET_DEFAULT
    ),
        ForeignKey(
            entity = PointDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pointId"),
            onDelete = ForeignKey.SET_DEFAULT
    ),
        ForeignKey(
            entity = TypeDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("typeId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["topicId", "pointId", "typeId"])]
)
data class TrueFalseQuestionDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name =  "id") var id: Int,
    @ColumnInfo(name =  "question")var question: String,
    @ColumnInfo(name =  "correctAnswer")var correctAnswer: Boolean,
    @ColumnInfo(name = "pointId") var pointId: Int,
    @ColumnInfo(name = "topicId") var topicId: Int,
    @ColumnInfo(name = "typeId") var typeId: Int
)

package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "multipleChoiceQuestion",
    foreignKeys = [
        ForeignKey(
            entity = TopicDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("topicFk"),
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = PointDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pointFk"),
            onDelete = ForeignKey.SET_DEFAULT
        ),
        ForeignKey(
            entity = TypeDto::class,
            parentColumns = arrayOf("type"),
            childColumns = arrayOf("typeFk"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["topicFk", "pointFk", "typeFk"])]
)
data class MultipleChoiceQuestionDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name =  "id") var id: Int,
    @ColumnInfo(name =  "question")var question: String,
    @ColumnInfo(name =  "answers")var answers: String,
    @ColumnInfo(name =  "correctAnswer")var correctAnswer: Int,
    @ColumnInfo(name = "pointFk") var point: Int,
    @ColumnInfo(name = "topicFk") var topic: Int,
    @ColumnInfo(name = "typeFk") var type: String
)

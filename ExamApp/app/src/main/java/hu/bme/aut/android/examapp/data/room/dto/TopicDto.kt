package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic",
    indices = [androidx.room.Index(value = ["topic"], unique = true), androidx.room.Index(value = ["parentTopicFk"])]
)
data class TopicDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name =  "id") var id: Int,
    @ColumnInfo(name =  "topic") var topic: String,
    @ColumnInfo(name =  "description")var description: String,
    @ColumnInfo(name =  "parentTopicFk")var parentTopic: Int
)

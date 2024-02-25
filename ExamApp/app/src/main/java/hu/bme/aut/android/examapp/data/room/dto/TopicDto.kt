package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic",
    foreignKeys = [
        ForeignKey(
            entity = TopicDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parentId"),
            onDelete = ForeignKey.SET_NULL
        )],
    indices = [androidx.room.Index(value = ["parentId"])]
)
data class TopicDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name =  "id") var id: Int,
    @ColumnInfo(name =  "topic")var topic: String,
    @ColumnInfo(name =  "description")var description: String,
    @ColumnInfo(name =  "parentId")var parentId: Int
)

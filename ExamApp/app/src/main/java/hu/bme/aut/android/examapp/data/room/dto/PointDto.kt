package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point")
data class PointDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name =  "id") var id: Int,
    @ColumnInfo(name =  "point")var point: Int,
    @ColumnInfo(name =  "type")var topic: String,
    @ColumnInfo(name =  "goodAnswer")var goodAnswer: Int,
    @ColumnInfo(name =  "badAnswer")var badAnswer: Int,
)
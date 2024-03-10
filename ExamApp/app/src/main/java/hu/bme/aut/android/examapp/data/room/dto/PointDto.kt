package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "point",
    indices = [androidx.room.Index(value = ["type"], unique = true)]
)
data class PointDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name =  "id") var id: Int,
    @ColumnInfo(name =  "point") var point: Double,
    @ColumnInfo(name =  "type") var type: String,
    @ColumnInfo(name =  "goodAnswer") var goodAnswer: Double,
    @ColumnInfo(name =  "badAnswer") var badAnswer: Double,
)
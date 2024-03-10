package hu.bme.aut.android.examapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.examapp.ui.viewmodel.type.Type

@Entity(
    tableName = "type",
    indices = [androidx.room.Index(value = ["type"], unique = true)]
)
data class TypeDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "type") var type: String
)
package hu.bme.aut.android.examapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PointTopicBar(
    onChooseTopic: (String) -> Unit = {},
    onChoosePoint: (String) -> Unit = {}
) {
    Column {
        DropDownList(
            name = "Topic",
            /*TODO database access*/
            items = listOf("Math", "Physics", "Chemistry", "Biology", "History", "Literature", "Geography", "Art", "Music", "Physical Education", "Computer Science", "Foreign Language"),
            onChoose = {onChooseTopic(it)},
            default = ""
        )
        Spacer(modifier = Modifier.height(16.dp))
        DropDownList(
            name = "Point",
            /*TODO database access*/
            items = listOf("Plus/Minus/2", "Plus/4"),
            onChoose = {onChoosePoint(it)},
            default = ""
        )
    }
}
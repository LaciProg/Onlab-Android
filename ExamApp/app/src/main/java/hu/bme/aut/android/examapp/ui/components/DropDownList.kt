package hu.bme.aut.android.examapp.ui.components

import android.util.Log
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.examapp.ui.viewmodel.TopicDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownList(
    modifier: Modifier = Modifier,
    name: String = "Gyümölcsök",
    items: List<String> = listOf("Alma", "Körte", "Banán"),
    default: TopicDetails = TopicDetails(),
    onChoose: (String) -> Unit = {},
) {
    Log.d("DropDownList", "Default.parent: ${default.parent}")
    var isExpanded by remember{ mutableStateOf(false)}
    var item by remember { mutableStateOf("") }
    item = default.parent
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = { newValue ->
            isExpanded = newValue
        }
    ) {
        TextField(
            value = item,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(text = name)
            },
            //colors = ExposedDropdownMenuDefaults.textFieldColors(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            for(i in items) {
                DropdownMenuItem(
                    text = {
                        Text(text = i)
                    },
                    onClick = {
                        item = i
                        isExpanded = false
                        onChoose(i)
                    }
                )
            }
            DropdownMenuItem(
                text = { Text(text = "") },
                onClick = {
                    item = ""
                    isExpanded = false
                    onChoose("")
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DropDownListPreview() {
    DropDownList()
}


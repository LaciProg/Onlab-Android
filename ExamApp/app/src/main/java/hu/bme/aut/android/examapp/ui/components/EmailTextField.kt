package hu.bme.aut.android.examapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@ExperimentalMaterial3Api
@Composable
fun EmailTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    readOnly: Boolean = false,
    isError: Boolean = false,
    onDone: (KeyboardActionScope.() -> Unit)? = null
) {
    val shape = RoundedCornerShape(5.dp)

    TextField(
        value = value.trim(),
        onValueChange = onValueChange,
        label = { Text(text = label) },
        leadingIcon = leadingIcon,
        trailingIcon = if (isError) {
            {
                Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)
            }
        } else {
            {
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        },
        modifier = modifier.width(TextFieldDefaults.MinWidth),
        singleLine = true,
        readOnly = readOnly,
        isError = isError,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = shape
    )
}

/* Credit for: https://gist.github.com/amparhizgar/e377e575e734ca0efe4e0c0afee36099 */
@Composable
fun EmailTextFieldWithDropdownUsage(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    readOnly: Boolean = false,
    isError: Boolean = false,
    onDone: (KeyboardActionScope.() -> Unit)? = null,
    users: List<String>
) {
    var dropDownOptions by remember { mutableStateOf(listOf<String>()) }
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = value)) }
    var dropDownExpanded by remember { mutableStateOf(false) }
    EmailTextFieldWithDropdown(
        value = textFieldValue,
        setValue = { fieldValue -> dropDownExpanded = true
            textFieldValue = fieldValue
            dropDownOptions = users.filter { it.startsWith(fieldValue.text) && it != fieldValue.text }.take(3)
            onValueChange(fieldValue.text)
        },
        onDismissRequest = { dropDownExpanded = false },
        dropDownExpanded = dropDownExpanded,
        list = dropDownOptions,
        label = label,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        enabled = enabled,
        imeAction = imeAction,
        readOnly = readOnly,
        isError = isError,
        onDone = onDone,
        modifier = modifier
    )
}

/* Credit for: https://gist.github.com/amparhizgar/e377e575e734ca0efe4e0c0afee36099 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailTextFieldWithDropdown(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    setValue: (TextFieldValue) -> Unit,
    onDismissRequest: () -> Unit,
    dropDownExpanded: Boolean,
    list: List<String>,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    readOnly: Boolean = false,
    isError: Boolean = false,
    onDone: (KeyboardActionScope.() -> Unit)? = null
) {
    var newDropDownExpanded by remember { mutableStateOf(dropDownExpanded) }
    newDropDownExpanded = dropDownExpanded
    Box(modifier) {
        val shape = RoundedCornerShape(5.dp)

        TextField(
            value = value,
            onValueChange = setValue ,
            label = { Text(text = label) },
            leadingIcon = leadingIcon,
            trailingIcon = if (isError) {
                {
                    Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null)
                }
            } else {
                {
                    if (trailingIcon != null) {
                        trailingIcon()
                    }
                }
            },
            modifier = modifier.width(TextFieldDefaults.MinWidth),
            singleLine = true,
            readOnly = readOnly,
            isError = isError,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = onDone
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            shape = shape,
        )
        DropdownMenu(
            expanded = newDropDownExpanded,
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = onDismissRequest
        ) {
            list.forEach { text ->
                DropdownMenuItem(onClick = {
                    setValue(
                        TextFieldValue(
                            text,
                            TextRange(text.length)
                        )
                    )
                    newDropDownExpanded = false
                }) {
                    Text(text = text)
                }
            }
        }
    }
}
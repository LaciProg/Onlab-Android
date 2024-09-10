package hu.bme.aut.android.examapp.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.examapp.R
import hu.bme.aut.android.examapp.ui.components.EmailTextFieldWithDropdownUsage
import hu.bme.aut.android.examapp.ui.components.PasswordTextField
import hu.bme.aut.android.examapp.ui.viewmodel.auth.LoginUserEvent
import hu.bme.aut.android.examapp.ui.viewmodel.auth.LoginUserViewModel
import hu.bme.aut.android.examapp.util.UiEvent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginUserViewModel = hiltViewModel()//LoginUserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()

    val snackbarHostState = SnackbarHostState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        try{
            viewModel.hasUser(context)
        } catch (e: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = e.localizedMessage ?: "An error occurred"
                )
            }
        }


        viewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Success -> {
                    onSuccess()
                }
                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailTextFieldWithDropdownUsage(
                value = state.email,
                label = stringResource(id = R.string.textfield_label_email),
                onValueChange = { viewModel.onEvent(LoginUserEvent.EmailChanged(it), context) },
                onDone = {},
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(bottom = 10.dp),
                users = users
            )
            PasswordTextField(
                value = state.password,
                label = stringResource(id = R.string.textfield_label_password),
                onValueChange = { viewModel.onEvent(LoginUserEvent.PasswordChanged(it), context) },
                onDone = {},
                modifier = Modifier.padding(bottom = 10.dp),
                isVisible = state.passwordVisibility,
                onVisibilityChanged = { viewModel.onEvent(LoginUserEvent.PasswordVisibilityChanged, context) }
            )
            Button(
                onClick = { viewModel.onEvent(LoginUserEvent.SignIn, context) },
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.button_text_sign_in))
            }
            Button(
                onClick = onRegisterClick,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.button_text_no_account))
            }
        }
    }
}

package hu.bme.aut.android.examapp.ui.viewmodel.auth.usecases

class PasswordsMatchUseCase {

    operator fun invoke(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}
package hu.bme.aut.android.examapp.domain.usecases

class PasswordsMatchUseCase {

    operator fun invoke(password: String, confirmPassword: String): Boolean =
        password == confirmPassword
}
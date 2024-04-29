package hu.bme.aut.android.examapp.domain.usecases

class IsEmailValidUseCase {

    operator fun invoke(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

}
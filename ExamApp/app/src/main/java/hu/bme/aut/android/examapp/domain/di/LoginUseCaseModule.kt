package hu.bme.aut.android.examapp.domain.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.examapp.data.auth.AuthService
import hu.bme.aut.android.examapp.data.auth.FirebaseAuthService
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineUserRepository
import hu.bme.aut.android.examapp.domain.usecases.IsEmailValidUseCase
import hu.bme.aut.android.examapp.domain.usecases.PasswordsMatchUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginUseCaseModule {

    @Provides
    @Singleton
    fun providePasswordsMatchUseCase(): PasswordsMatchUseCase = PasswordsMatchUseCase()

    @Provides
    @Singleton
    fun provideIsEmailValidUseCase(): IsEmailValidUseCase = IsEmailValidUseCase()

    @Provides
    @Singleton
    fun provideAuthService(
        repository: OfflineUserRepository
    ): AuthService = FirebaseAuthService(
        FirebaseAuth.getInstance(),
        repository
    )

    @Provides
    @Singleton
    fun provideAuthUsesCases(
        passwordsMatchUseCase: PasswordsMatchUseCase,
        isEmailValidUseCase: IsEmailValidUseCase,
        authService: AuthService
    ): AuthUseCases = AuthUseCases(
        passwordsMatchUseCase,
        isEmailValidUseCase,
        authService
    )


}

class AuthUseCases(
    val passwordsMatchUseCase: PasswordsMatchUseCase,
    val isEmailValidUseCase: IsEmailValidUseCase,
    val authService: AuthService
)
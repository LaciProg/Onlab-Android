package hu.bme.aut.android.examapp.domain.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.examapp.api.ExamAppApiService
import hu.bme.aut.android.examapp.data.auth.AuthService
import hu.bme.aut.android.examapp.data.auth.FirebaseAuthService
import hu.bme.aut.android.examapp.data.repositories.offline.OfflineUserRepository
import hu.bme.aut.android.examapp.domain.usecases.AuthenticateUseCase
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
    fun provideAuthenticateUsesCase(): AuthenticateUseCase = AuthenticateUseCase()

    @Provides
    @Singleton
    fun provideAuthService(
        repository: OfflineUserRepository,
        retrofitService: ExamAppApiService,
        authenticateUseCase: AuthenticateUseCase
    ): AuthService = FirebaseAuthService(
        FirebaseAuth.getInstance(),
        repository,
        retrofitService,
        authenticateUseCase
    )

    @Provides
    @Singleton
    fun provideAuthUsesCases(
        passwordsMatchUseCase: PasswordsMatchUseCase,
        isEmailValidUseCase: IsEmailValidUseCase,
        authenticateUseCase: AuthenticateUseCase,
        authService: AuthService,
    ): AuthUseCases = AuthUseCases(
        passwordsMatchUseCase,
        isEmailValidUseCase,
        authenticateUseCase,
        authService
    )


}

class AuthUseCases(
    val passwordsMatchUseCase: PasswordsMatchUseCase,
    val isEmailValidUseCase: IsEmailValidUseCase,
    val authenticateUseCase: AuthenticateUseCase,
    val authService: AuthService
)
package hu.bme.aut.android.examapp.domain.usecases

import hu.bme.aut.android.examapp.api.ExamAppApi
import hu.bme.aut.android.examapp.api.dto.UserDto

class AuthenticateUseCase {
    suspend operator fun invoke(userDto: UserDto? = null){
        ExamAppApi.authenticate(userDto)
    }

}
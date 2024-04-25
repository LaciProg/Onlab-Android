package hu.bme.aut.android.examapp

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.examapp.data.AppContainer
import hu.bme.aut.android.examapp.data.AppDataContainer
import hu.bme.aut.android.examapp.data.auth.AuthService
import hu.bme.aut.android.examapp.data.auth.FirebaseAuthService

class ExamApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        authService = FirebaseAuthService(FirebaseAuth.getInstance(), container.userRepository)
    }

    companion object{
        lateinit var authService: AuthService
    }
}
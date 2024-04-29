package hu.bme.aut.android.examapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExamApplication : Application() {

    //@Inject
    //lateinit var userDao: UserDao

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    //lateinit var container: AppContainer

    /*override fun onCreate() {
        super.onCreate()
        //userRepository = OfflineUserRepository(userDao)
        //container = AppDataContainer(this)
        //authService = FirebaseAuthService(FirebaseAuth.getInstance(), userRepository)
    }*/

    /*companion object{
        lateinit var authService: AuthService
        lateinit var userRepository: OfflineUserRepository
    }*/
}
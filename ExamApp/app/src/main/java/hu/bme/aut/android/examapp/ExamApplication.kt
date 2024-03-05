package hu.bme.aut.android.examapp

import android.app.Application
import hu.bme.aut.android.examapp.data.AppContainer
import hu.bme.aut.android.examapp.data.AppDataContainer

class ExamApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
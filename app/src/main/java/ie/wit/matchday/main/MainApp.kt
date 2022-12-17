package ie.wit.matchday.main

import android.app.Application
import ie.wit.matchday.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var loggedInUser: UserModel

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        loggedInUser = UserModel()
        i("Matchday started")
    }


}
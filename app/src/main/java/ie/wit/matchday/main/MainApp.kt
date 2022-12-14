package ie.wit.matchday.main

import android.app.Application
import ie.wit.matchday.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var users: UserStore
    lateinit var loggedInUser: UserModel

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        users = UserJSONStore(applicationContext)
        loggedInUser = UserModel()
        i("Matchday started")
    }


}
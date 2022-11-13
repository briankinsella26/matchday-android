package ie.wit.matchday.main

import android.app.Application
import android.content.Context
import ie.wit.matchday.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var matches: MatchStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        matches = MatchJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        i("Matchday started")
    }


}
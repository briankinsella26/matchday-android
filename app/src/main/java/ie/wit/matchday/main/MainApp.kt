package ie.wit.matchday.main

import android.app.Application
import ie.wit.matchday.models.MatchMemStore
import ie.wit.matchday.models.MatchModel
import ie.wit.matchday.models.MatchStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    lateinit var matches: MatchStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        matches = MatchMemStore()
        i("Matchday started")
    }


}
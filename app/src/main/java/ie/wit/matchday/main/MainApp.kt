package ie.wit.matchday.main

import android.app.Application
import ie.wit.matchday.models.MatchModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application() {

    var matches = ArrayList<MatchModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Matchday started")

        matches.add(MatchModel("Forth Celtic", "home", "1-0"))
    }


}
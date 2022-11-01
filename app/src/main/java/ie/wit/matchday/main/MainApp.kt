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

        matches.add(MatchModel("Forth Celtic", true, 52.1231, 7.14222, 0.5f, "1-0"))
    }


}
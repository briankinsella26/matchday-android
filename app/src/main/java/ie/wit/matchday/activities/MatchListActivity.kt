package ie.wit.matchday.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import ie.wit.matchday.main.MainApp

class MatchListActivity: AppCompatActivity() {
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        app = application as MainApp

    }

}
package ie.wit.matchday.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivityHomepageBinding
import ie.wit.matchday.main.MainApp
import timber.log.Timber
import timber.log.Timber.i

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_homepage)
        i("Matchday started")

    }
}
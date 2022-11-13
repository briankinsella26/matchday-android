package ie.wit.matchday.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ie.wit.matchday.databinding.ActivityHomepageBinding
import ie.wit.matchday.main.MainApp
import timber.log.Timber.i

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        //adding delay temporarily to showcase splashscreen
        var keepSplashOnScreen = true
        val delay = 1000L
        splashScreen.setKeepVisibleCondition() { keepSplashOnScreen }
        Handler(Looper.getMainLooper()).postDelayed({ keepSplashOnScreen = false }, delay)

        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        i("Login activity started")


    }
}
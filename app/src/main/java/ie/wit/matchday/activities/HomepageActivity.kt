package ie.wit.matchday.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ie.wit.matchday.databinding.ActivityHomepageBinding
import ie.wit.matchday.main.MainApp
import timber.log.Timber.i

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        registerRefreshCallback()

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        i("Homepage activity started")

        if(app.loggedInUser.id.isEmpty()) {
            i("No user logged in, redirect to login activity")
            val launcherIntent = Intent(this, LoginActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
        }
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {}
    }
}
package ie.wit.matchday.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivitySignupBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.UserModel
import timber.log.Timber

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        registerRefreshCallback()

        app = application as MainApp
        Timber.i("Signup activity started")

        binding.signupButton.setOnClickListener() {
            user.id = java.util.UUID.randomUUID().toString()
            user.email = binding.signupEmail.text.toString()
            user.password = binding.signupPassword.text.toString()
            user.firstName = binding.signupFirstName.text.toString()
            user.lastName = binding.signupFirstName.text.toString()

            if (user.email.isEmpty() || user.password.isEmpty() || user.firstName.isEmpty() || user.lastName.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.signup_validation_message), Toast.LENGTH_SHORT).show()
                Timber.i("signup failed")
            } else {
                app.users.create(user.copy())
                val launcherIntent = Intent(this, MatchListActivity::class.java)
                app.loggedInUser = user
                refreshIntentLauncher.launch(launcherIntent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_match, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                val launcherIntent = Intent(this, LoginActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {}
    }
}
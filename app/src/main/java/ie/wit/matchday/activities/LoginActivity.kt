package ie.wit.matchday.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivityLoginBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.UserModel
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        registerRefreshCallback()
        app = application as MainApp
        Timber.i("Login activity started")

        binding.login.setOnClickListener() {
            user.email = binding.email.text.toString()
            user.password = binding.password.text.toString()

            if (user.email.isEmpty() || user.password.isEmpty()){
                Toast.makeText(applicationContext, getString(R.string.signup_validation_message), Toast.LENGTH_SHORT).show()
                Timber.i("login failed")
            } else {
                login(it, user.email, user.password)
            }
            setResult(RESULT_OK)
        }

        binding.signupButton.setOnClickListener() {
            val launcherIntent = Intent(app.applicationContext, SignupActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
        }
        Timber.i("list of users ${app.users.findAll()}")
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {}
    }

    private fun login(view: View, email: String, password: String) {
        var allUsers = app.users.findAll()
        var userFound = false
        for (user in allUsers) {
            if (email == user.email && password == user.password) {
                userFound = true
                app.loggedInUser = user
                break
            }
        }
        if (userFound) {
            Timber.i("login successful for user:  ${user.email}")
            val launcherIntent = Intent(app.applicationContext, MatchListActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
        } else {
            Toast.makeText(applicationContext, getString(R.string.login_error_message), Toast.LENGTH_SHORT).show()
            Timber.i("login failed")
        }
    }
}
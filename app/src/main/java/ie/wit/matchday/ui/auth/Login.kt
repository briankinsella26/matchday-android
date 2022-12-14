package ie.wit.matchday.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.wit.matchday.R
import ie.wit.matchday.databinding.LoginBinding
import ie.wit.matchday.activities.Home
import ie.wit.matchday.models.UserModel
import timber.log.Timber

class Login : AppCompatActivity() {

    private lateinit var loginViewModel : LoginViewModel
    private lateinit var loginBinding : LoginBinding
    var user = UserModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        loginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.login.setOnClickListener() {
            user.email = loginBinding.email.text.toString()
            user.password = loginBinding.password.text.toString()

            login(user.email, user.password)
        }

        loginBinding.signupButton.setOnClickListener() {
            startActivity(Intent(this, Signup::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, Home::class.java)) })

        loginViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
            { status -> checkStatus(status) })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Click again to Close App...",Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun login(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) { return }

        loginViewModel.login(email,password)
    }

    private fun checkStatus(error:Boolean) {
            if (error)
                Toast.makeText(this,
                        getString(R.string.login_error_message),
                        Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = loginBinding.email.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginBinding.email.error = "Required."
            valid = false
        } else {
            loginBinding.email.error = null
        }

        val password = loginBinding.password.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginBinding.password.error = "Required."
            valid = false
        } else {
            loginBinding.password.error = null
        }
        return valid
    }
}

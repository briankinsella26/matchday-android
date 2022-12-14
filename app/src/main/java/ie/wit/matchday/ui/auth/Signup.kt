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

import ie.wit.matchday.activities.Home
import ie.wit.matchday.databinding.SignupBinding
import ie.wit.matchday.models.UserModel
import timber.log.Timber

class Signup : AppCompatActivity() {

    private lateinit var signupViewModel : SignupViewModel
    private lateinit var signupBinding : SignupBinding
    var user = UserModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        signupBinding = SignupBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)

        signupBinding.signupButton.setOnClickListener() {
            user.email = signupBinding.signupEmail.text.toString()
            user.password = signupBinding.signupPassword.text.toString()

            signup(user.email, user.password)
        }

        signupBinding.login.setOnClickListener() {
            startActivity(Intent(this, Login::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()

        signupViewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        signupViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser -> if (firebaseUser != null)
            startActivity(Intent(this, Home::class.java)) })

        signupViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
            { status -> checkStatus(status) })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this,"Click again to Close App...",Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun signup(email: String, password: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) { return }

        signupViewModel.register(email,password)
    }

    private fun checkStatus(error:Boolean) {
            if (error)
                Toast.makeText(this,
                        getString(R.string.login_error_message),
                        Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = signupBinding.signupEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            signupBinding.signupEmail.error = "Required."
            valid = false
        } else {
            signupBinding.signupEmail.error = null
        }

        val password = signupBinding.signupPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            signupBinding.signupPassword.error = "Required."
            valid = false
        } else {
            signupBinding.signupPassword.error = null
        }

        return valid
    }
}

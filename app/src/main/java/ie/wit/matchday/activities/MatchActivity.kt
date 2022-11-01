package ie.wit.matchday.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivityMatchBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.MatchModel
import timber.log.Timber.i

class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    var match = MatchModel()
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app: MainApp
    var isUpdate: Boolean = false
    var radioButtonIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        registerRefreshCallback()

        i("Match Activity started...")

        if (intent.hasExtra("match_edit")) {
            if(binding.homeGame.isChecked) {
                radioButtonIndex = 1;
            }
            isUpdate = true
            match = intent.extras?.getParcelable("match_edit")!!
            binding.matchOpponent.setText(match.opponent)
            binding.radioGroup.check(radioButtonIndex)
            binding.btnAdd.text = getString(R.string.button_saveMatch)

        }


        binding.btnAdd.setOnClickListener() {
            match.opponent = binding.matchOpponent.text.toString()
            match.result = binding.result.text.toString()
            match.homeOrAway = homeOrAway(binding.awayGame.isChecked)

            if (match.opponent.isEmpty()) {
                Snackbar.make(
                    it,
                    getString(R.string.opponent_validation_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if ( isUpdate ) {
                    app.matches.update(match.copy())
                } else {
                    app.matches.create(match.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_match, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.matches.delete(match)
                finish()
            }
            R.id.item_cancel -> {
                val launcherIntent = Intent(this, HomepageActivity::class.java)
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

    private fun homeOrAway(isAway: Boolean) : String {
        return if (isAway) {
            "Away"
        } else {
            "Home"
        }

    }
}
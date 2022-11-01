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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        registerRefreshCallback()

        i("Match Activity started...")


        binding.btnAdd.setOnClickListener() {
            match.opponent = binding.matchOpponent.text.toString()
            match.result = binding.result.text.toString()
            match.homeOrAway = homeOrAway(binding.awayGame.isChecked)
            if (match.opponent.isNotEmpty()) {
                app.matches.add(match.copy())
                setResult(RESULT_OK)
                val launcherIntent = Intent(this, MatchListActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            else {
                Snackbar
                    .make(it,"Please Enter an Opponent", Snackbar.LENGTH_LONG)
                    .show()
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
               //todo
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

    private fun homeOrAway(isAway: Boolean = false) : String {
        return if (isAway) {
            "Away"
        } else {
            "Home"
        }

    }
}
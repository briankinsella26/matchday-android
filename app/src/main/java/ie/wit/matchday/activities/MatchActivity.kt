package ie.wit.matchday.activities

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivityMatchBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.Location
import ie.wit.matchday.models.MatchModel
import timber.log.Timber.i
import java.util.*


class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    var match = MatchModel()
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app: MainApp
    private var isUpdate: Boolean = false
    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        registerRefreshCallback()
        registerMapCallback()

        i("Match Activity started...")

        if (intent.hasExtra("match_edit")) {
            match = intent.extras?.getParcelable("match_edit")!!
            isUpdate = true
            binding.matchOpponent.setText(match.opponent)
            binding.result.setText(match.result)
            binding.date.text = match.date
            binding.time.text = match.time
//            if(match.homeOrAway == "Away") {
//                binding.awayGame.isChecked = true
//            } else {
//                binding.homeGame.isChecked = true
//            }
            binding.btnAdd.text = getString(R.string.button_saveMatch)
            binding.locationButton.text = getString(R.string.location_edit)

        }

        val datePicker: MaterialDatePicker<Long> = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText("Select date...")
            .build()

        binding.date.setOnClickListener() {
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            val simple = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = simple.format(it)
            match.date = date
            binding.date.text = match.date

        }

        val timePicker: MaterialTimePicker = MaterialTimePicker
            .Builder()
            .setTitleText("Select time...")
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()

        binding.time.setOnClickListener() {
            timePicker.show(supportFragmentManager, "TIME_PICKER")
        }

        timePicker.addOnPositiveButtonClickListener {
            val hour = String.format("%02d", timePicker.hour)
            val minute = String.format("%02d", timePicker.minute)
            match.time = "${hour}:${minute}"
            binding.time.text = match.time
        }

        binding.btnAdd.setOnClickListener() {
            match.opponent = binding.matchOpponent.text.toString()
            match.result = binding.result.text.toString()
//            match.homeOrAway = homeOrAway(binding.awayGame.isChecked)
            match.userId = app.loggedInUser.id

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

        binding.locationButton.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (match.zoom != 0f) {
                location.lat = match.lat
                location.lng = match.lng
                location.zoom = match.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_match, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.item_cancel -> {
//                finish()
//            }
//        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {}
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            location = result.data!!.extras?.getParcelable("location")!!
                            match.lat = location.lat
                            match.lng = location.lng
                            match.zoom = location.zoom
                            binding.locationButton.text = getString(R.string.location_set)
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun homeOrAway(isAway: Boolean) : String {
        return if (isAway) {
            "Away"
        } else {
            "Home"
        }

    }
}
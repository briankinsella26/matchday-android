package ie.wit.matchday.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivityMatchBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.MatchModel
import timber.log.Timber.i
import java.util.*


class MatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchBinding
    var match = MatchModel()
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app: MainApp
    var isUpdate: Boolean = false
    var radioButtonIndex: Int = 0
    var date: EditText? = null
    var datePickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        registerRefreshCallback()

        i("Match Activity started...")

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
            match.date = date.toString()
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
            match.time = "${timePicker.hour}:${timePicker.minute}"
        }

        if (intent.hasExtra("match_edit")) {
            if(binding.homeGame.isChecked) {
                radioButtonIndex = 1;
            }
            isUpdate = true
            match = intent.extras?.getParcelable("match_edit")!!
            binding.matchOpponent.setText(match.opponent)
            binding.result.setText(match.result)
//            binding.dateAndTime
            binding.radioGroup.check(radioButtonIndex)
            binding.btnAdd.text = getString(R.string.button_saveMatch)

        }


        binding.btnAdd.setOnClickListener() {
            match.opponent = binding.matchOpponent.text.toString()
            match.result = binding.result.text.toString()
//            match.dateAndTime = binding.dateAndTime.text.toString()
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
                finish()
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
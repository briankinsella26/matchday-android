package ie.wit.matchday.ui.detail

import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import ie.wit.matchday.R
import ie.wit.matchday.activities.MapActivity
import ie.wit.matchday.databinding.FragmentMatchDetailBinding
import ie.wit.matchday.models.Location
import ie.wit.matchday.models.MatchModel
import java.util.*


class MatchDetailFragment : Fragment() {

    private lateinit var detailViewModel: MatchDetailViewModel
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private val args by navArgs<MatchDetailFragmentArgs>()
    private var _fragBinding: FragmentMatchDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    var match = MatchModel()
    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentMatchDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(MatchDetailViewModel::class.java)
        detailViewModel.observableMatch.observe(viewLifecycleOwner, Observer {
            match = detailViewModel.observableMatch.value!!
            render()
        })

        registerRefreshCallback()
        registerMapCallback()
        setCancelButtonListener(fragBinding)

        val datePicker: MaterialDatePicker<Long> = MaterialDatePicker
            .Builder
            .datePicker()
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setTitleText("Select date...")
            .build()

        fragBinding.date.setOnClickListener() {
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        datePicker.addOnPositiveButtonClickListener {
            val simple = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = simple.format(it)
            match.date = date
            fragBinding.date.text = match.date

        }

        val timePicker: MaterialTimePicker = MaterialTimePicker
            .Builder()
            .setTitleText("Select time...")
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .build()

        fragBinding.time.setOnClickListener() {
            timePicker.show(parentFragmentManager, "TIME_PICKER")
        }

        timePicker.addOnPositiveButtonClickListener {
            val hour = String.format("%02d", timePicker.hour)
            val minute = String.format("%02d", timePicker.minute)
            match.time = "${hour}:${minute}"
            fragBinding.time.text = match.time
        }

        fragBinding.locationButton.setOnClickListener {
            location.lng = match.lng
            location.lat = match.lat
            location.zoom = match.zoom
            val launcherIntent = Intent(this.context, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)

        }

        fragBinding.btnUpdate.setOnClickListener {
            match.opponent = fragBinding.matchOpponent.text.toString()
            match.result = fragBinding.result.text.toString()
            match.home = fragBinding.home.isChecked
            match.away = fragBinding.away.isChecked

            detailViewModel.updateMatch(match)
            val action = MatchDetailFragmentDirections.actionMatchDetailFragmentToMatchesFragment()
            findNavController().navigate(action)
        }

        return root
    }

    private fun render() {
        fragBinding.matchvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getMatch(args.matchid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
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
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            location = result.data!!.extras?.getParcelable("location")!!
                            match.lat = location.lat
                            match.lng = location.lng
                            match.zoom = location.zoom
                            fragBinding.locationButton.text = getString(R.string.location_updated)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }


    private fun setCancelButtonListener(layout: FragmentMatchDetailBinding) {
        layout.btnCancel.setOnClickListener {
            val action = MatchDetailFragmentDirections.actionMatchDetailFragmentToMatchesFragment()
            findNavController().navigate(action)
        }
    }
}
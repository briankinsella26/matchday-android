package ie.wit.matchday.ui.detail

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import ie.wit.matchday.R
import ie.wit.matchday.activities.MapActivity
import ie.wit.matchday.databinding.FragmentMatchDetailBinding
import ie.wit.matchday.models.Location
import ie.wit.matchday.models.MatchModel
import ie.wit.matchday.ui.auth.LoggedInViewModel
import java.util.*


class MatchDetailFragment : Fragment() {

    private lateinit var detailViewModel: MatchDetailViewModel
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
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
        val homeTeamSpinner: Spinner = fragBinding.homeTeamSpinner
        val awayTeamSpinner: Spinner = fragBinding.awayTeamSpinner
        val homeScoreSpinner: Spinner = fragBinding.homeScore
        val awayScoreSpinner: Spinner = fragBinding.awayScore

        detailViewModel = ViewModelProvider(this).get(MatchDetailViewModel::class.java)
        detailViewModel.observableMatch.observe(viewLifecycleOwner, Observer {
            match = detailViewModel.observableMatch.value!!
            render()
        })

        registerRefreshCallback()
        registerMapCallback()
        setCancelButtonListener(fragBinding)

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.teams_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            homeTeamSpinner.adapter = adapter
        }
        detailViewModel.observableMatch.observe(viewLifecycleOwner, Observer {
            match = detailViewModel.observableMatch.value!!

            homeTeamSpinner.setSelection(
                (homeTeamSpinner.adapter as ArrayAdapter<String?>).getPosition(match.homeTeam))

            awayTeamSpinner.setSelection(
                (awayTeamSpinner.adapter as ArrayAdapter<String?>).getPosition(match.awayTeam))

            homeScoreSpinner.setSelection(
                (homeScoreSpinner.adapter as ArrayAdapter<String?>).getPosition(match.homeScore))

            awayScoreSpinner.setSelection(
                (homeScoreSpinner.adapter as ArrayAdapter<String?>).getPosition(match.awayScore))
        })

        homeTeamSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (parent != null) {
                    match.homeTeam = parent.getItemAtPosition(pos) as String
                }
                if(match.homeTeam == "Select team") match.homeTeam == ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                match.homeTeam = ""
            }
        }


        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.teams_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            awayTeamSpinner.adapter = adapter
        }

        awayTeamSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (parent != null) {
                    match.awayTeam = parent.getItemAtPosition(pos) as String
                }
                if(match.awayTeam == "Select team") match.awayTeam == ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                match.awayTeam = ""
            }
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.score_home_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            homeScoreSpinner.adapter = adapter
        }

        homeScoreSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (parent != null) {
                    match.homeScore = parent.getItemAtPosition(pos) as String
                }
                if(match.homeScore == "Home") match.homeScore == ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                match.homeScore = ""
            }
        }

        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.score_away_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            awayScoreSpinner.adapter = adapter
        }

        awayScoreSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (parent != null) {
                    match.awayScore = parent.getItemAtPosition(pos) as String
                }
                if(match.awayScore == "Home") match.awayScore == ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                match.awayScore = ""
            }
        }

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
            detailViewModel.observableMatch.observe(viewLifecycleOwner, Observer {
                var matchUpdate = detailViewModel.observableMatch.value!!
                match.matchTitle = matchUpdate.homeTeam + "  vs  " + matchUpdate.awayTeam
                match.result = "${matchUpdate.homeScore} - ${matchUpdate.awayScore}"
            })
            match.leagueGame = fragBinding.leagueGame.isChecked
            match.cupGame = fragBinding.cupGame.isChecked
            detailViewModel.updateMatch(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.matchid, match)
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
        detailViewModel.getMatch(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.matchid)
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
package ie.wit.matchday.ui.addMatch

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import ie.wit.matchday.R
import ie.wit.matchday.activities.MapActivity
import ie.wit.matchday.databinding.FragmentAddMatchBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.Location
import ie.wit.matchday.models.MatchModel
import ie.wit.matchday.ui.matches.MatchesViewModel
import timber.log.Timber.i
import java.util.*

class AddMatchFragment : Fragment() {

    private var _fragBinding: FragmentAddMatchBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    lateinit var navController: NavController
    private lateinit var addMatchViewModel: AddMatchViewModel
    lateinit var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location(52.245696, -7.139102, 15f)
    var match = MatchModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentAddMatchBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.menu_addMatch)
	    setupMenu()
        registerMapCallback()
        addMatchViewModel = ViewModelProvider(this).get(AddMatchViewModel::class.java)
        addMatchViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })


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
            val location = Location(52.245696, -7.139102, 15f)
            if (match.zoom != 0f) {
                location.lat = match.lat
                location.lng = match.lng
                location.zoom = match.zoom
            }
            val launcherIntent = Intent(this.context, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)

        }

        fragBinding.btnAdd.setOnClickListener {
            match.opponent = fragBinding.matchOpponent.text.toString()
            match.result = fragBinding.result.text.toString()
            match.home = fragBinding.home.isChecked
            match.away = fragBinding.away.isChecked
            match.userId = app.loggedInUser.id

            if (match.opponent.isEmpty()) {
                Snackbar.make(
                    it,
                    getString(R.string.opponent_validation_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                addMatchViewModel.addMatch(match)
            }
        }

        setButtonListener(fragBinding)
        setCancelButtonListener(fragBinding)

        return root;
    }

 private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_match, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
//                    Uncomment this if you want to immediately return to Matches
//                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.add_match_error),Toast.LENGTH_LONG).show()
        }
    }

    private fun setButtonListener(layout: FragmentAddMatchBinding) {
        layout.btnAdd.setOnClickListener {
            match.opponent = fragBinding.matchOpponent.text.toString()
            match.result = fragBinding.result.text.toString()
            match.home = fragBinding.home.isChecked
            match.away = fragBinding.away.isChecked
            match.userId = app.loggedInUser.id

            if (match.opponent.isEmpty()) {
                Snackbar.make(
                    it,
                    getString(R.string.opponent_validation_message),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                addMatchViewModel.addMatch(match)
                val action = AddMatchFragmentDirections.actionAddMatchFragmentToMatchesFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setCancelButtonListener(layout: FragmentAddMatchBinding) {
        layout.btnCancel.setOnClickListener {
            val action = AddMatchFragmentDirections.actionAddMatchFragmentToMatchesFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        val matchesViewModel = ViewModelProvider(this).get(MatchesViewModel::class.java)
//        matchesViewModel.observableMatchesList.observe(viewLifecycleOwner, Observer {
//                totalMatches = matchesViewModel.observableMatchesList.value!!.sumOf { it.amount }
//        })
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        i("in register callback: ${result.data }")
                        if (result.data != null) {
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("in register callback location: $location")
                            match.lat = location.lat
                            match.lng = location.lng
                            match.zoom = location.zoom
                            fragBinding.locationButton.text = getString(R.string.location_set)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
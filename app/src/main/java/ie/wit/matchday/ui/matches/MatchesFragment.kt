package ie.wit.matchday.ui.matches

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.matchday.R
import ie.wit.matchday.adapters.MatchAdapter
import ie.wit.matchday.adapters.MatchClickListener
import ie.wit.matchday.databinding.FragmentMatchesBinding
import ie.wit.matchday.models.MatchModel
import ie.wit.matchday.ui.auth.LoggedInViewModel

class MatchesFragment : Fragment(), MatchClickListener {

    private var _fragBinding: FragmentMatchesBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val matchesViewModel: MatchesViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMatchesBinding.inflate(inflater, container, false)
        val root = fragBinding.root
	    setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        matchesViewModel.observableMatchesList.observe(viewLifecycleOwner, Observer {
                matches ->
            matches?.let { render(matches as ArrayList<MatchModel>) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = MatchesFragmentDirections.actionMatchesFragmentToAddMatchFragment()
            findNavController().navigate(action)
        }
        return root
    }

   private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_matches, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(matchList: ArrayList<MatchModel>) {
        timber.log.Timber.i("matchlost zzzz: ${matchList}")
        fragBinding.recyclerView.adapter = MatchAdapter(matchList,this)
        if (matchList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.matchesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.matchesNotFound.visibility = View.GONE
        }
    }

    override fun onMatchClick(match: MatchModel) {
        val action = MatchesFragmentDirections.actionMatchesFragmentToMatchDetailFragment(match.uid)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                matchesViewModel.liveFirebaseUser.value = firebaseUser
                matchesViewModel.load()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
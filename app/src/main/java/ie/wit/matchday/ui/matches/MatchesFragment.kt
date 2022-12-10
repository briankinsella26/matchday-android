package ie.wit.matchday.ui.matches

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.matchday.R
import ie.wit.matchday.adapters.MatchAdapter
import ie.wit.matchday.adapters.MatchListener
import ie.wit.matchday.databinding.FragmentMatchesBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.MatchModel

class MatchesFragment : Fragment(), MatchListener {

    lateinit var app: MainApp
    private var _fragBinding: FragmentMatchesBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var matchesViewModel: MatchesViewModel

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
        matchesViewModel = ViewModelProvider(this).get(MatchesViewModel::class.java)
        matchesViewModel.observableMatchesList.observe(viewLifecycleOwner, Observer {
                matches ->
            matches?.let { render(matches) }
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
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_matches, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(matchesList: List<MatchModel>) {
        fragBinding.recyclerView.adapter = MatchAdapter(matchesList,this)
        if (matchesList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.matchesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.matchesNotFound.visibility = View.GONE
        }
    }

    override fun onMatchClick(match: MatchModel) {
        val action = MatchesFragmentDirections.actionMatchesFragmentToMatchDetailFragment(match.id)
        findNavController().navigate(action)
    }

    override fun onResume() {
        super.onResume()
        matchesViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
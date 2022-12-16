package ie.wit.matchday.ui.matches

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import ie.wit.matchday.R
import ie.wit.matchday.adapters.MatchAdapter
import ie.wit.matchday.adapters.MatchClickListener
import ie.wit.matchday.databinding.FragmentMatchesBinding
import ie.wit.matchday.models.MatchModel
import ie.wit.matchday.ui.auth.LoggedInViewModel
import ie.wit.matchday.utils.*

class MatchesFragment : Fragment(), MatchClickListener {

    private var _fragBinding: FragmentMatchesBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val matchesViewModel: MatchesViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

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
        loader = createLoader(requireActivity())
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        showLoader(loader,"Downloading Matches")
        matchesViewModel.observableMatchesList.observe(viewLifecycleOwner, Observer {
                matches ->
            matches?.let { render(matches as ArrayList<MatchModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = MatchesFragmentDirections.actionMatchesFragmentToAddMatchFragment()
            findNavController().navigate(action)
        }

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Match")
                val adapter = fragBinding.recyclerView.adapter as MatchAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                matchesViewModel.delete(matchesViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as MatchModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onMatchClick(viewHolder.itemView.tag as MatchModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }

   private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_matches, menu)

                val item = menu.findItem(R.id.toggleMatches) as MenuItem
                item.setActionView(R.layout.toggle_button)
                val toggleMatches: SwitchMaterial = item.actionView!!.findViewById(R.id.match_type_switch)
                toggleMatches.isChecked = false

                toggleMatches.setOnCheckedChangeListener { _, isChecked ->
                    if (!isChecked) matchesViewModel.load()
                    else matchesViewModel.loadByMatchType("leagueGame")
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(matchList: ArrayList<MatchModel>) {
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

    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Matches")
            matchesViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Matches")
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
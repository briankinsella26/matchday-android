package ie.wit.matchday.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ie.wit.matchday.databinding.FragmentMatchDetailBinding
import ie.wit.matchday.models.MatchModel


class MatchDetailFragment : Fragment() {

    private lateinit var detailViewModel: MatchDetailViewModel
    private val args by navArgs<MatchDetailFragmentArgs>()
    private var _fragBinding: FragmentMatchDetailBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentMatchDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(MatchDetailViewModel::class.java)
        detailViewModel.observableMatch.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render(/*match: MatchModel*/) {
        fragBinding.matchvm = detailViewModel
//        fragBinding.matchOpponent.setText(match.opponent)
//        fragBinding.matchResult.setText(match.opponent)
//        fragBinding.matresult = match.result
//        foundMatch.homeOrAway = match.homeOrAway
//        foundMatch.date = match.date
//        foundMatch.time = match.time
//        foundMatch.lat = match.lat
//        foundMatch.lng = match.lng
//        foundMatch.zoom = match.zoom
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getMatch(args.matchid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
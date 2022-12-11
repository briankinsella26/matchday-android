package ie.wit.matchday.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.wit.matchday.R

class AboutFragment : Fragment() {

    private lateinit var aboutViewModel: AboutViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        aboutViewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_about, container, false)
        aboutViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }
}
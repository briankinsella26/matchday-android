package ie.wit.matchday.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.matchday.models.MatchManager
import ie.wit.matchday.models.MatchModel

class MatchesViewModel : ViewModel() {

    private val matchesList = MutableLiveData<List<MatchModel>>()

    val observableMatchesList: LiveData<List<MatchModel>>
        get() = matchesList

    init {
        load()
    }

    fun load() {
        matchesList.value = MatchManager.findAll()
    }
}
package ie.wit.matchday.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.matchday.models.MatchManager
import ie.wit.matchday.models.MatchModel

class MatchDetailViewModel : ViewModel() {
    private val match = MutableLiveData<MatchModel>()

    val observableMatch: LiveData<MatchModel>
        get() = match

    fun getMatch(id: Long) {
        match.value = MatchManager.findById(id)
    }
}
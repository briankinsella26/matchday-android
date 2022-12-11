package ie.wit.matchday.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.matchday.models.MatchManager
import ie.wit.matchday.models.MatchModel
import timber.log.Timber.i

class MatchDetailViewModel : ViewModel() {
    private val match = MutableLiveData<MatchModel>()

    val observableMatch: LiveData<MatchModel>
        get() = match

    fun getMatch(id: Long) {
        i("match: ${match}")
        match.value = MatchManager.findById(id)
    }

    fun updateMatch(match: MatchModel) {
        MatchManager.update(match)
    }
}
package ie.wit.matchday.ui.addMatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.matchday.models.MatchManager
import ie.wit.matchday.models.MatchModel

class AddMatchViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addMatch(match: MatchModel) {
        status.value = try {
            MatchManager.create(match)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
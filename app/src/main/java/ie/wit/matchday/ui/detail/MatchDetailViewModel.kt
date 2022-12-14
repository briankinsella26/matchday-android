package ie.wit.matchday.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.matchday.firebase.FirebaseDBManager
import ie.wit.matchday.models.MatchModel
import timber.log.Timber

class MatchDetailViewModel : ViewModel() {
    private val match = MutableLiveData<MatchModel>()

    val observableMatch: LiveData<MatchModel>
        get() = match

    fun getMatch(userid:String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, match)
            Timber.i("Detail getMatch() Success : ${
                match.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getMatch() Error : $e.message")
        }
    }

    fun updateMatch(userid:String, id: String, match: MatchModel) {
        try {
            FirebaseDBManager.update(userid, id, match)
            Timber.i("Detail update() Success : $match")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}
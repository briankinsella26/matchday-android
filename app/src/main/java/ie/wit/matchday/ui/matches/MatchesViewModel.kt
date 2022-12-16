package ie.wit.matchday.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.matchday.firebase.FirebaseDBManager
import ie.wit.matchday.models.MatchModel
import timber.log.Timber
import java.lang.Exception

class MatchesViewModel : ViewModel() {

    private val matchList = MutableLiveData<List<MatchModel>>()

    val observableMatchesList: LiveData<List<MatchModel>>
        get() = matchList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, matchList)
            Timber.i("Match List Load Success : ${matchList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Match List Load Error : $e.message")
        }
    }

    fun loadByMatchType(matchType: String) {
        try {
            FirebaseDBManager.findByMatchType(liveFirebaseUser.value?.uid!!, matchList, matchType)
            Timber.i("Match List Load Success : ${matchList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Match List Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Match List Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Match List Delete Error : $e.message")
        }
    }
}
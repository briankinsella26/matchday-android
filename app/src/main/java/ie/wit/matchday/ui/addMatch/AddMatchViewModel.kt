package ie.wit.matchday.ui.addMatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.matchday.firebase.FirebaseDBManager
import ie.wit.matchday.models.MatchModel

class AddMatchViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addMatch(firebaseUser: MutableLiveData<FirebaseUser>,
                 match: MatchModel) {
        status.value = try {
            FirebaseDBManager.create(firebaseUser,match)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
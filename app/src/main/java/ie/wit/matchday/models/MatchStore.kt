package ie.wit.matchday.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface MatchStore {

    fun create(firebaseUser: MutableLiveData<FirebaseUser>, match: MatchModel)
    fun findAll(MatchList:
                MutableLiveData<List<MatchModel>>
    )
    fun findAll(userid:String,
                matchList:
                MutableLiveData<List<MatchModel>>)
    fun findById(userid:String, matchid: String,
                 match: MutableLiveData<MatchModel>)
    fun delete(userid:String, matchid: String)
    fun update(userid:String, matchid: String, match: MatchModel)
}
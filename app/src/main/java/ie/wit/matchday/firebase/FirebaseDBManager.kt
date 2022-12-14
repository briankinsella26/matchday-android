package ie.wit.matchday.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.matchday.models.MatchModel
import ie.wit.matchday.models.MatchStore
import timber.log.Timber

object FirebaseDBManager : MatchStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(matchlist: MutableLiveData<List<MatchModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, matchlist: MutableLiveData<List<MatchModel>>) {

        Timber.i("Getting matches for user : ${userid}")
        Timber.i("Getting matches: ${matchlist}")

        database.child("user-matches").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase match error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<MatchModel>()
                    val children = snapshot.children
                    children.forEach {
                        val match = it.getValue(MatchModel::class.java)
                        localList.add(match!!)
                    }
                    database.child("user-matches").child(userid)
                        .removeEventListener(this)

                    matchlist.value = localList
                }
            })
    }

    override fun findById(userid: String, matchid: String, match: MutableLiveData<MatchModel>) {

        database.child("user-matches").child(userid)
            .child(matchid).get().addOnSuccessListener {
                match.value = it.getValue(MatchModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, match: MatchModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("matches").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        match.uid = key
        val matchValues = match.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/matches/$key"] = matchValues
        childAdd["/user-matches/$uid/$key"] = matchValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, matchid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/matches/$matchid"] = null
        childDelete["/user-matches/$userid/$matchid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, matchid: String, match: MatchModel) {

        val matchValues = match.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["matches/$matchid"] = matchValues
        childUpdate["user-matches/$userid/$matchid"] = matchValues

        database.updateChildren(childUpdate)
    }
}

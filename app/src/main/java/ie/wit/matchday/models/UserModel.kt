package ie.wit.matchday.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var id: String = "",
                      var firstName: String = "",
                      var lastName: String = "",
                      var email: String = "",
                      var password: String = "",
                      var phoneNumber: Long = 0) : Parcelable {
}

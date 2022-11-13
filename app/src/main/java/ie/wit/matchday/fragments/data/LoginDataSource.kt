package ie.wit.matchday.fragments.data

import ie.wit.matchday.fragments.data.model.LoggedInUser
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.UserModel
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    var users = listOf<UserModel>(UserModel(1, "Brian", "Android", "brian@android.com", "test11", 2))

    fun login(email: String, password: String): Result<LoggedInUser> {

        var loggedInUser = UserModel(0, "", "", "", "", 0)
        var userFound = false
        for (user in users) {
            if (email == user.email && password == user.password) {
                loggedInUser = user
                userFound = true
                break
            }
        }
        return if (userFound) {
            Result.Success(
                LoggedInUser(
                    java.util.UUID.randomUUID().toString(),
                    loggedInUser.firstName
                )
            )
        } else {
            Result.Error(IOException("Error logging in"))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

}
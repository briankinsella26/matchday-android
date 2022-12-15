package ie.wit.matchday.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
@Parcelize
data class MatchModel(var uid: String = "",
                      var email: String = "joe@bloggs.com",
                      var homeTeam: String = "",
                      var awayTeam: String = "",
                      var matchTitle: String = "",
                      var leagueGame: Boolean = true,
                      var cupGame: Boolean = false,
                      var homeScore: String = "",
                      var awayScore: String = "",
                      var date: String = "",
                      var time: String = "",
                      var lat: Double = 0.0,
                      var lng: Double = 0.0,
                      var zoom: Float = 0f,
                      var result: String = "") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "homeTeam" to homeTeam,
            "awayTeam" to awayTeam,
            "leagueGame" to leagueGame,
            "matchTitle" to matchTitle,
            "cupGame" to cupGame,
            "homeScore" to homeScore,
            "awayScore" to awayScore,
            "date" to date,
            "time" to time,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom,
            "result" to result
        )
    }
}

@Parcelize
data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f) : Parcelable

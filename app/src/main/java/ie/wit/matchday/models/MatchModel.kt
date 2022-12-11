package ie.wit.matchday.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchModel(var id: Long = 0,
                      var userId: String = "",
                      var opponent: String = "",
                      var home: Boolean = true,
                      var away: Boolean = false,
                      var date: String = "",
                      var time: String = "",
                      var lat: Double = 0.0,
                      var lng: Double = 0.0,
                      var zoom: Float = 0f,
                      var result: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f) : Parcelable

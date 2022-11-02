package ie.wit.matchday.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchModel(var id: Long = 0,
                      var opponent: String = "",
                      var homeOrAway: String = "",
                      var date: String = "",
                      var time: String = "",
//                      var lat: Double = 0.0,
//                      var lng: Double = 0.0,
//                      var zoom: Float = 0f,
                      var result: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f) : Parcelable

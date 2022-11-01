package ie.wit.matchday.models

data class MatchModel(var opponent: String = "",
                      var homeOrAway: String = "home",
//                      var lat: Double = 0.0,
//                      var lng: Double = 0.0,
//                      var zoom: Float = 0f,
                      var result: String = "")

data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f)

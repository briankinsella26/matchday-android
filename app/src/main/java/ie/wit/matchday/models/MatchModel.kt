package ie.wit.matchday.models

data class MatchModel(var opponent: String = "",
                      var homeGame: Boolean = false,
                      var lat: Double = 0.0,
                      var lng: Double = 0.0,
                      var zoom: Float = 0f,
                      var result: String = "")

data class Location(var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f)

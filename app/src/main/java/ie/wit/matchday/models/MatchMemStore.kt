package ie.wit.matchday.models

import timber.log.Timber
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class MatchMemStore: MatchStore {

    val matches = ArrayList<MatchModel>()

    override fun findAll(): List<MatchModel> {
        return matches
    }

    override fun create(match: MatchModel) {
        match.id = getId()
        matches.add(match)
        logAll()
    }

    override fun update(match: MatchModel) {
        var foundMatch: MatchModel? = matches.find { p -> p.id == match.id}
        if (foundMatch != null) {
            foundMatch.opponent = match.opponent
            foundMatch.homeOrAway = match.homeOrAway
            foundMatch.result = match.result
            foundMatch.lat = match.lat
            foundMatch.lng = match.lng
            foundMatch.zoom = match.zoom
            logAll()
        }
    }

    override fun delete(match: MatchModel) {
        matches.remove(match)
    }

    fun logAll() {
        matches.forEach{ i("${it}") }
    }
}
package ie.wit.matchday.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object MatchManager : MatchStore {

    private val matches = ArrayList<MatchModel>()

    override fun findAll(): List<MatchModel> {
        return matches
    }

    override fun findMatchesByUser(user: UserModel): List<MatchModel> {
        TODO("Not yet implemented")
    }

    override fun update(match: MatchModel) {
        TODO("Not yet implemented")
    }

    override fun delete(match: MatchModel) {
        TODO("Not yet implemented")
    }

    override fun findById(id:Long) : MatchModel? {
        val foundMatch: MatchModel? = matches.find { it.id == id }
        return foundMatch
    }

    override fun create(match: MatchModel) {
        match.id = getId()
        matches.add(match)
        logAll()
    }

    fun logAll() {
        Timber.v("** Matches List **")
        matches.forEach { Timber.v("Match: ${it}") }
    }
}
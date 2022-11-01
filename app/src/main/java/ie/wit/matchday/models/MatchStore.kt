package ie.wit.matchday.models

interface MatchStore {

    fun create(match: MatchModel)
    fun findAll(): List<MatchModel>
    fun update(match: MatchModel)
    fun delete(match: MatchModel)
}
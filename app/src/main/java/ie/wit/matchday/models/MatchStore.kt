package ie.wit.matchday.models

interface MatchStore {

    fun create(match: MatchModel)
    fun findAll(): List<MatchModel>
    fun findById(id:Long) : MatchModel?
    fun findMatchesByUser(user: UserModel): List<MatchModel>
    fun update(match: MatchModel)
    fun delete(match: MatchModel)
}
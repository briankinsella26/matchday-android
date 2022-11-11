package ie.wit.matchday.models

interface UserStore {

    fun create(user: UserModel)
    fun findAll(): List<UserModel>
    fun update(user: UserModel)
    fun delete(user: UserModel)
}
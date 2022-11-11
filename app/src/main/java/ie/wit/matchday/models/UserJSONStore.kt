package ie.wit.matchday.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.matchday.helpers.exists
import ie.wit.matchday.helpers.read
import ie.wit.matchday.helpers.write

import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

const val JSON_FILE_USERS = "users.json"
val gsonBuilderUser: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParserUser())
    .create()
val listTypeUser: Type = object: TypeToken<ArrayList<UserModel>>() {}.type

fun generateRandomUserid(): Long {
    return Random().nextLong()
}

class UserJSONStore(private val context: Context): UserStore {

    var users = mutableListOf<UserModel>()

    init {
        if (exists(context, JSON_FILE_USERS)) {
            deserialize()
        }
    }

    override fun findAll(): List<UserModel> {
        logAll()
        return users
    }

    override fun create(user: UserModel) {
        user.id = generateRandomUserid()
        users.add(user)
        serialize()
    }


    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id}
        if (foundUser != null) {
            foundUser.firstName = user.firstName
            foundUser.lastName = user.lastName
            foundUser.email = user.email
            foundUser.password = user.password
            foundUser.phoneNumber = user.phoneNumber
            logAll()
            serialize()
        }
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilderUser.toJson(users, listTypeUser)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = gsonBuilderUser.fromJson(jsonString, listTypeUser)
    }

    private fun logAll() {
        users.forEach { Timber.i("$it") }
    }
}

    class UriParserUser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}

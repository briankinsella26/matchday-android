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

const val JSON_FILE = "matches.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object: TypeToken<ArrayList<MatchModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class MatchJSONStore(private val context: Context): MatchStore {

    var matches = mutableListOf<MatchModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<MatchModel> {
        logAll()
        return matches
    }

    override fun findMatchesByUser(user: UserModel): List<MatchModel> {
        logAll()
        return matches.filter { it.userId == user.id}
    }

    override fun create(match: MatchModel) {
        match.id = generateRandomId()
        matches.add(match)
        serialize()
    }


    override fun update(match: MatchModel) {
        var foundMatch: MatchModel? = matches.find { p -> p.id == match.id}
        if (foundMatch != null) {
            foundMatch.opponent = match.opponent
            foundMatch.result = match.result
            foundMatch.homeOrAway = match.homeOrAway
            foundMatch.date = match.date
            foundMatch.time = match.time
            foundMatch.lat = match.lat
            foundMatch.lng = match.lng
            foundMatch.zoom = match.zoom
            logAll()
            serialize()
        }
    }

    override fun delete(match: MatchModel) {
        matches.remove(match)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(matches, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        matches = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        matches.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
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

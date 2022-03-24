import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

val serializer = Json {
    encodeDefaults = true
    prettyPrint = true
}

@Serializable
data class Message(
    var text: String,
    val id: Int,
    val datePosted: String = LocalDateTime.now().toString(),
    var dateEdited: String = datePosted,
) {
    val logicFields: Map<String, String>
        get() = evaluateLogicFields()

    fun getLogicFieldsAsJson(): String {
        return serializer.encodeToString(logicFields)
    }

    fun asJson(): String {
        return serializer.encodeToString(this)
    }
}

fun List<Message>.asJson(): String {
    return serializer.encodeToString(this)
}
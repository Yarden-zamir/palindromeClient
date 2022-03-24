package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import serializer
import java.time.LocalDateTime



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
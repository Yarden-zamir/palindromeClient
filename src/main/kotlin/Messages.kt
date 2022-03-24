import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import model.serializer

suspend fun updateMessage(args: String): String {
    val id = args.split(" ")[0].toIntOrNull() ?: "Failed to retrieve message, bad ID"
    val text = args.split(" ", limit = 2)[1]
    val oldMessage = try {
        val response: HttpStatement = client.get("$endpoint/messages/$id")
        serializer.decodeFromString<Message>(response.receive())
    } catch (t: Throwable) {
        return "No message by that ID"
    }
    return try {
        val response: HttpStatement = client.put("$endpoint/messages/$id") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            body = listOf("text" to text).formUrlEncode()
        }
        serializer.decodeFromString<Message>(response.receive()).prettyPrint()
    } catch (t: Throwable) {
        "Cannot update message"
    } + " (was$CYAN ${oldMessage.text})"
}

suspend fun deleteMessage(id: String): String {
    val oldMessage: Message = try {
        val response: HttpStatement = client.get("$endpoint/messages/$id")
        serializer.decodeFromString<Message>(response.receive())
    } catch (t: Throwable) {
        return "No message by that ID"
    }
    return try {
        val response: HttpStatement = client.delete("$endpoint/messages/$id")
        "Removed message ${oldMessage.prettyPrint()}"
    } catch (t: Throwable) {
        "Cannot remove message"
    }
}

fun String.arg(): String {
    return this.split("-")[1].trim()
}

suspend fun addMessage(text: String): String {
    return try {
        val response: HttpStatement = client.post("$endpoint/messages") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            body = listOf("text" to text).formUrlEncode()
        }
        serializer.decodeFromString<Message>(response.receive()).prettyPrint()
    } catch (t: Throwable) {
        "Cannot add message"
    }

}

suspend fun getMessage(id: String): String {
    return try {
        val response: HttpStatement = client.get("$endpoint/messages/$id")
        serializer.decodeFromString<Message>(response.receive()).prettyPrint()
    } catch (t: Throwable) {
        "No message by that ID"
    }

}

suspend fun getMessages(): String {
    return try {

        val response: HttpStatement = client.get("$endpoint/messages")
        return serializer.decodeFromString<List<Message>>(response.receive()).prettyPrint()
    } catch (t: Throwable) {
        "No messages ):    add new messages with `add message - {text}`"
    }
}

suspend fun UpdateMessage(text: String): String {
    return ""
}
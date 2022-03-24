import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString

val client = HttpClient(CIO)
var endpoint: String = ""

suspend fun main(args: Array<String>) {
    endpoint = args.getOrNull(0) ?: "https://palindrome-rest-api.herokuapp.com"
    println(help())
    var command = readln()
    while (command != "close") {
        val result = when {
            command.startsWith("Add message -", true)
            -> addMessage(command.arg())
            command.startsWith("Delete message -", true)
            -> deleteMessage(command.arg())
            command.startsWith("Update message -", true)
            -> updateMessage(command.arg())
            command.startsWith("List messages", true)
            -> getMessages()
            command.startsWith("Get message -", true)
            -> getMessage(command.arg())
            else -> {
                "Command not found \n" +
                        help()
            }
        }
        println(result)
        command = readln()
    }
}

fun help(): String {
    return buildString {
        appendLine("---")
        appendLine("What would you like to do?")
        appendLine("---")
        appendLine("Add message {text}")
        appendLine("Delete message {id}")
        appendLine("Update message {id} {text}")
        appendLine("Get message {id}")
        appendLine("List messages")
        appendLine("---")
    }
}

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

suspend fun List<Message>.prettyPrint(): String {
    return return this.map { it.prettyPrint() }.toString()
}

suspend fun Message.prettyPrint(): String {


    val date = if (datePosted == dateEdited) dateEdited else "$dateEdited (posted $datePosted)"
    val palindrome = if (isPalindrome()) "which is a palindrome" else "which is not a palindrome"
    return "\n$CYAN[$id] : $text $RESET $palindrome    -$date"
}

suspend fun Message.isPalindrome(): Boolean {
    return try {
        val response: HttpStatement = client.get("$endpoint/messages/$id/logicfields/palindrome")
        response.receive<String>()
    } catch (t: Throwable) {
        "Failed to check palindromety"
    }.toBooleanStrictOrNull() ?: false
}
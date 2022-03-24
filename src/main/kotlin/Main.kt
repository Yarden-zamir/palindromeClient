import io.ktor.client.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.Json

val serializer = Json {
    encodeDefaults = true
    prettyPrint = true
}
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




package ext

import CYAN
import RESET
import client
import endpoint
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.client.statement.*
import model.Message

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
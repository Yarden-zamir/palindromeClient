import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.client.statement.*

suspend fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
    val client = HttpClient(CIO) {
        install(Logging)
    }
    val response: HttpStatement = client.request("https://https://palindrome-rest-api.herokuapp.com") {
        // Configure request parameters exposed by HttpRequestBuilder
    }
    println(response.execute().content)

}

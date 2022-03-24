package model

import kotlin.reflect.KFunction1

val logicFieldsList = LogicFields()


class LogicFields : ArrayList<KFunction1<Message, String>>() {
    var enabled = true
}

fun Message.evaluateLogicFields(): Map<String, String> {
    val result = mutableMapOf<String, String>()
    if (logicFieldsList.enabled) logicFieldsList.forEach {
        result[it.name.lowercase()] = it.invoke(this)
    }
    return result
}
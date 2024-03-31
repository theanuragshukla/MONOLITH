package com.anurag.monolith.Interpreter

object Helper {
    fun reveseOperator(str: String): String{
        return when (str) {
            ">" -> "<="
            "<" -> ">="
            "=" -> "!="
            ">=" -> "<"
            "<=" -> ">"
            "!=" -> "="
            else -> throw IllegalArgumentException("Invalid operator: ${str}")
        }
    }
    fun randomString(len: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..len)
            .map{ kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}
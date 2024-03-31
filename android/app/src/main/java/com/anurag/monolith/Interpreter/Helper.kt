package com.anurag.monolith.Interpreter

import android.util.Log

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
    fun arrayMapToString(map: Map<String, Array<Number>>):String {
        val sb = StringBuilder("{")

        for ((key, value) in map) {
            sb.append("$key=[${value.joinToString()}], ")
        }
        if (sb.length > 1) {
            sb.delete(sb.length - 2, sb.length)
        }

        sb.append("}")

        return sb.toString()
    }

}
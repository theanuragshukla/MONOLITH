package com.anurag.monolith.Interpreter

import If
import While
import com.anurag.monolith.Interpreter.Helper

object TACHelper {
    fun whileToNotIf(whileCondition: While): If {
        val operator = Helper.reveseOperator(whileCondition.operator)
        return If(whileCondition.operand1, operator, whileCondition.operand2)
    }
    fun ifToNotIf(ifCondition: If): If {
        val operator = Helper.reveseOperator(ifCondition.operator)
        return If(ifCondition.operand1, operator, ifCondition.operand2)
    }
}
package com.anurag.monolith.Interpreter

import Instruction
import Add
import Break
import Continue
import Dec
import Div
import Done
import Else
import Fi
import Flops
import Halt
import If
import Jump
import Mod
import Mov
import Mul
import Sub
import While

object Parser {
    fun parseInstruction(instructionString: String): Instruction {
        val parts = instructionString.trim().split("\\s+".toRegex())
        return when (parts[0]) {
            "ADD" -> Add(parts[1], parts[2], parts[3])
            "DEC" -> Dec(parts[1], parts[2].toDouble())
            "MOV" -> Mov(parts[1], parts[2].toDouble())
            "MODE" -> Flops(parts[1].toBooleanStrict())
            "SUB" -> Sub(parts[1], parts[2], parts[3])
            "MUL" -> Mul(parts[1], parts[2], parts[3])
            "DIV" -> Div(parts[1], parts[2], parts[3])
            "MOD" -> Mod(parts[1], parts[2], parts[3])
            "IF" -> If(parts[1], parts[2], parts[3])
            "ELSE" -> Else
            "FI" -> Fi
            "WHILE" -> While(parts[1], parts[2], parts[3])
            "DONE" -> Done
            "BREAK" -> Break
            "CONTINUE" -> Continue
            "JUMP" -> Jump(parts[1])
            "HALT" -> Halt
            else -> throw IllegalArgumentException("Invalid instruction: ${parts[0]}")
        }
    }
}
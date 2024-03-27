package com.anurag.monolith

import Add
import Dec
import Div
import Fi
import Instruction
import Flops
import If
import Mod
import Mov
import Mul
import Sub
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.anurag.monolith.Interpreter.InterpreterViewModel
import com.anurag.monolith.ui.theme.MONOLITHTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MONOLITHTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Demo()
                }
            }
        }
    }
}

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
        "FI" -> Fi
        else -> throw IllegalArgumentException("Invalid instruction: ${parts[0]}")
    }
}
    @Composable
    fun Demo() {
        val interpreter = remember {
            InterpreterViewModel()
        }
        val programString = """
        MOV a 5
        MOV b 10
        DEC c 0
        IF a > b
        SUB c a b
        FI
        IF b > a
        SUB c b a
    """.trimIndent()

        val instructions = programString.lines().map { parseInstruction(it) }
        for (instruction in instructions) {
            interpreter.execute(instruction)
        }
        println(interpreter.registers)
    }
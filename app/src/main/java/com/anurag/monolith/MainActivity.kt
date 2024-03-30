package com.anurag.monolith

import Add
import Break
import Continue
import Dec
import Div
import Done
import Fi
import Instruction
import Flops
import If
import Jump
import Mod
import Mov
import Mul
import Sub
import While
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

    @Composable
    fun Demo() {
        val interpreter = remember {
            InterpreterViewModel()
        }
        val programString = """
        DEC a 0
        DEC i 0
        WHILE i < 10
        DEC j 0
        WHILE j < 10
        ADD a a 1
        ADD j j 1
        DONE
        ADD i i 1
        DONE
        HALT
    """.trimIndent()

        interpreter.setAlgorithm(programString)
        interpreter.start()
    }
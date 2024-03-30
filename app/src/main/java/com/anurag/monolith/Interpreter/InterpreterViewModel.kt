package com.anurag.monolith.Interpreter

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
import Instruction
import Jump
import Mod
import Mov
import Mul
import Sub
import While
import android.util.Log
import androidx.lifecycle.ViewModel

class InterpreterViewModel : ViewModel() {
    var parsed = listOf<Instruction>()
    var algo = mutableListOf<Instruction>()
    var registers = mutableMapOf<String, Number>()
    var flops: Boolean = false
    var skipStmt: Boolean = false
    var ifCount = 0
    var pc = 0
    val labels = mutableMapOf<String, Int>()
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

    fun setAlgorithm(program: String) {
        labels.clear()
        val instructions = analyseProgram(program.lines())
        parsed = instructions.map { parseInstruction(it) }
        pc = 0
        convertToTAC()
    }

    fun analyseProgram(program: List<String>): List<String> {
        return program.mapIndexed { idx, s ->
            val parts = s.split("::")
            if (parts.size > 1) {
                labels[parts[1].trim()] = idx
            }
            return@mapIndexed parts[0]
        }
    }

    fun convertToTAC() {
        while (pc < parsed.size) {
            parseInstruction(parsed[pc++])
        }
    }

    fun parseInstruction(s: Instruction) {
        when (s) {
            is While -> {
                pc--
                parseWhileLoop()
            }

            is If -> {
                pc--
                parseIfBlock()
            }

            else -> {
                appendAlgo(s)
            }
        }
    }

    fun parseWhileLoop() {
        val startLabel = Helper.randomString(8)
        val endLabel = Helper.randomString(8)
        val whileCondition = parsed[pc++] as? While ?: return
        addLabel(startLabel)
        appendAlgo(whileToNotIf(whileCondition))
        appendAlgo(Jump(endLabel))
        appendAlgo(Fi)

        while (pc < parsed.size) {
            val ins = parsed[pc++]
            when (ins) {
                is Break -> {
                    appendAlgo(Jump(endLabel))
                }

                is Continue -> {
                    appendAlgo(Jump(startLabel))
                }

                is Done -> {
                    appendAlgo(Jump(startLabel))
                    addLabel(endLabel)
                    return
                }

                else -> {
                    parseInstruction(ins)
                }
            }
        }
    }

    fun parseIfBlock() {
        val elseLabel = Helper.randomString(8)
        val endLabel = Helper.randomString(8)
        val IfCondition = parsed[pc++] as? If ?: return
        appendAlgo(ifToNotIf(IfCondition))
        appendAlgo(Jump(elseLabel))
        appendAlgo(Fi)
        while (pc < parsed.size) {
            val ins = parsed[pc++]
            when (ins) {
                is Else -> {
                    appendAlgo(Jump(endLabel))
                    addLabel(elseLabel)
                }

                is Fi -> {
                    if (!labels.containsKey(elseLabel)) addLabel(elseLabel)
                    addLabel(endLabel)
                    return
                }

                else -> {
                    parseInstruction(ins)
                }
            }
        }
    }

    fun execute(instruction: Instruction) {
        Log.d("EXEC", instruction.toString())
        if (skipStmt && instruction !is Fi) return
        when (instruction) {
            is Add -> {
                val op1 = resolveOperand(instruction.operand1)
                val op2 = resolveOperand(instruction.operand2)
                val result = op1.toDouble() + op2.toDouble()
                registers[instruction.destination] = result
            }

            is Dec -> {
                registers[instruction.variableName] = instruction.initialValue.toDouble()
            }

            is Mov -> {
                registers[instruction.destination] =
                    if (flops) instruction.value.toDouble() else instruction.value.toInt()
            }

            is Flops -> {
                updateFlops(instruction.mode)
            }

            is Sub -> {
                val op1 = resolveOperand(instruction.operand1)
                val op2 = resolveOperand(instruction.operand2)
                val result = op1.toDouble() - op2.toDouble()
                registers[instruction.destination] = result
            }

            is Mul -> {
                val op1 = resolveOperand(instruction.operand1)
                val op2 = resolveOperand(instruction.operand2)
                val result = op1.toDouble() * op2.toDouble()
                registers[instruction.destination] = result
            }

            is Div -> {
                val op1 = resolveOperand(instruction.operand1)
                val op2 = resolveOperand(instruction.operand2)
                val result = op1.toDouble() / op2.toDouble()
                registers[instruction.destination] = result
            }

            is Mod -> {
                val op1 = resolveOperand(instruction.operand1)
                val op2 = resolveOperand(instruction.operand2)
                val result = op1.toDouble() % op2.toDouble()
                registers[instruction.destination] = result
            }

            is If -> {
                ifCount++
                val isTrue = evaluateIf(instruction)
                if (!isTrue) {
                    skipStmt = true
                }
            }

            is Fi -> {
                if (ifCount <= 0) {
                    throw Exception("got Unexpected FI statement")
                }
                ifCount--
                skipStmt = false
            }

            is Jump -> {
                val idx = labels.getOrDefault(instruction.destination.trim(), -1)
                if (idx != -1) {
                    pc = idx
                } else {
                    throw Exception("Illegal Jump Instruction.")
                }
            }

            is Halt -> {
                Log.d("HALT", "Program execution finished")
                pc = algo.size
            }

            else -> {}
        }
    }

    fun evaluateIf(stmt: If): Boolean {
        val op1 = resolveOperand(stmt.operand1)
        val op2 = resolveOperand(stmt.operand2)
        return when (stmt.operator) {
            ">" -> op1.toDouble() > op2.toDouble()
            "<" -> op1.toDouble() < op2.toDouble()
            "=" -> op1.toDouble() == op2.toDouble()
            ">=" -> op1.toDouble() >= op2.toDouble()
            "<=" -> op1.toDouble() <= op2.toDouble()
            "!=" -> op1.toDouble() != op2.toDouble()
            else -> throw IllegalArgumentException("Invalid operator: ${stmt.operator}")
        }
    }

    private fun resolveOperand(operand: String): Number {
        return try {
            operand.toDouble()
        } catch (e: NumberFormatException) {
            registers.getOrDefault(operand, 0)
        }
    }

    fun appendAlgo(line: Instruction) {
        Log.d("ALGO", line.toString())
        algo.add(line)
    }

    fun addLabel(label: String) {
        labels[label] = algo.size
    }

    fun updateFlops(value: Boolean) {
        flops = value
    }

    fun whileToNotIf(whileCondition: While): If {
        val operator = when (whileCondition.operator) {
            ">" -> "<="
            "<" -> ">="
            "=" -> "!="
            ">=" -> "<"
            "<=" -> ">"
            "!=" -> "="
            else -> throw IllegalArgumentException("Invalid operator: ${whileCondition.operator}")
        }
        return If(whileCondition.operand1, operator, whileCondition.operand2)
    }
    fun ifToNotIf(ifCondition: If): If {
        val operator = when (ifCondition.operator) {
            ">" -> "<="
            "<" -> ">="
            "=" -> "!="
            ">=" -> "<"
            "<=" -> ">"
            "!=" -> "="
            else -> throw IllegalArgumentException("Invalid operator: ${ifCondition.operator}")
        }
        return If(ifCondition.operand1, operator, ifCondition.operand2)
    }
    fun start() {
        pc = 0
        Log.d("INFO", labels.toString())
        while (pc < algo.size) {
            execute(algo[pc++])
        }
        Log.d("INFO", registers.toString())
    }
}
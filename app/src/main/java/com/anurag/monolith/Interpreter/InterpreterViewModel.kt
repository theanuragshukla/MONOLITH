package com.anurag.monolith.Interpreter

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
import androidx.lifecycle.ViewModel

class InterpreterViewModel : ViewModel() {

    var algo = listOf<Instruction>()
    var registers = mutableMapOf<String, Number>()
    var flops:Boolean = false
    var skipStmt:Boolean = false
    var ifCount = 0
    var pc = 0
    val labels = mutableMapOf<String, Int>()


    fun updateFlops(value:Boolean){
        flops = value
    }

    fun setAlgorithm(program: String){
        labels.clear()
        val instructions = analyseProgram(program.lines())
        algo = instructions.map { parseInstruction(it) }
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
    fun execute(instruction: Instruction) {
        if(skipStmt && instruction !is Fi)return
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
                registers[instruction.destination] = if (flops) instruction.value.toDouble() else instruction.value.toInt()
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
                if(!isTrue){
                    skipStmt = true
                }
            }
            is Fi -> {
                if(ifCount<=0){
                    throw Exception("got Unexpected FI statement")
                }
                ifCount --
                skipStmt = false
            }
            is Jump -> {
                val idx = labels.getOrDefault(instruction.destination.toString().trim(), -1)
                if(idx!=-1){
                    pc = idx
                }else {
                    throw Exception("Illegal Jump Instruction. Lablel $")
                }
            }
            else -> {}
        }
    }

    fun evaluateIf(stmt:If):Boolean{
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
            "WHILE" -> While(parts[1] ,parts[2], parts[3])
            "DONE" -> Done
            "BREAK" -> Break
            "CONTINUE" -> Continue
            "JUMP" -> Jump(parts[1].toInt())
            else -> throw IllegalArgumentException("Invalid instruction: ${parts[0]}")
        }
    }

    fun start() {
        while(pc<algo.size){
            execute(algo[pc++])
        }
    }
}
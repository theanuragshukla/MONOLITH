package com.anurag.monolith.Interpreter

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
import androidx.lifecycle.ViewModel

class InterpreterViewModel : ViewModel() {

    var registers = mutableMapOf<String, Number>()
    var flops:Boolean = false
    var skipStmt:Boolean = false
    var ifCount = 0

    fun updateFlops(value:Boolean){
        flops = value
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
}
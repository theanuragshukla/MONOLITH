sealed class Instruction
data class Add(val destination: String, val operand1: String, val operand2: String) : Instruction()
data class Dec(val variableName: String, val initialValue: Number) : Instruction()
data class Mov(val destination: String, val value: Number) : Instruction()
data class Flops(val mode: Boolean) : Instruction()
data class Sub(val destination: String, val operand1: String, val operand2: String) : Instruction()
data class Mul(val destination: String, val operand1: String, val operand2: String) : Instruction()
data class Div(val destination: String, val operand1: String, val operand2: String) : Instruction()
data class Mod(val destination: String, val operand1: String, val operand2: String) : Instruction()
data class Jump(val destination: String) : Instruction()
data class If(val operand1: String, val operator: String, val operand2: String) : Instruction()
object Else:Instruction()
object Fi:Instruction()
data class While(val operand1: String, val operator: String, val operand2: String) : Instruction()
object Done:Instruction()
object Break:Instruction()
object Continue:Instruction()
object Halt: Instruction()
data class DeclareArray(val arrayName: String, val size: Int) : Instruction()
data class AccessArray(val arrayName: String, val index: String, val destination: String) : Instruction()
data class UpdateArray(val arrayName: String, val index: String, val value: String) : Instruction()

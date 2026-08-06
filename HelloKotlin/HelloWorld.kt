
// Enums
enum class DiaSemana {
    SEGUNDA, 
    TERCA, 
    QUARTA, 
    QUINTA, 
    SEXTA, 
    SABADO, 
    DOMINGO
}

// Generics
class Print<T>{
    constructor(valor: T){
        val tipo = valor!!::class.simpleName
        println("Tipo: ${tipo}")
    }
}


// Annotations
annotation class Info(val descricao: String = "");

@Info(descricao = "Função para somar dois números")
fun soma(a: Int, b: Int): Int{
    return a + b
}

fun main(){
    println("Hello, World!")
    println(Print<Int>(10))
}
// Para executar
// kotlinc HelloWorld.kts -script

println("Hello World")

// Variaveis
var x = 10
println("Minha variavel ${x}")
x = 20
println("Minha variavel ${x}")

//Constantes
val y = 30
// y = 15 // HelloWorld.kts:14:1: error: 'val' cannot be reassigned.
println("Minha constante ${y}")

// Coleções
val listaFixa = listOf("A", "B", "C")
listaFixa.map{ print("${it},") }

val listaMutavel = mutableListOf("A", "B", "C")
listaMutavel.add("D")
listaMutavel.map{ print("${it},") }

// Condicional
val impar = if(x%2 != 0) "Impar" else "Par"
println("\nO numero ${x} é ${impar}")

if(x > 0){
    println("O número é positivo")
}else{
    println("O número é negativo")
}

// Range
for(i in 1..10){
    print("${i},")
}

val numeros = listOf(1,2,3,4,5)

// Funções de alta ordem
val impares = numeros.filter{ it%2 != 0 }
val pares = numeros.filter{ it%2 == 0 }
val quadrados = numeros.map{ it*it }
val soma = numeros.sum()
println("\nImpares: ${impares}")
println("Pares: ${pares}") 
println("Quadrados: ${quadrados}")
println("Soma: ${soma}")

// When
val mes = when(x){
    1 -> "Janeiro"
    2 -> "Fevereiro"
    3 -> "Março"
    4 -> "Abril"
    5 -> "Maio"
    6 -> "Junho"
    7 -> "Julho"
    8 -> "Agosto"
    9 -> "Setembro"
    10 -> "Outubro"
    11 -> "Novembro"
    12 -> "Dezembro"
    else -> "Mês inválido"
}
println("O mês é: ${mes}")

// Classes
class Pessoa{
    val nome: String
    val idade: Int

    init{
        println("Classe Pessoa instanciada")
    }

    constructor(nome: String, idade: Int){
        println("Construtor chamado")
        this.nome = nome
        this.idade = idade
    }
}

var pessoa = Pessoa("João", 30)
println("Nome: ${pessoa.nome}")

// Data Classes
data class Carro(val marca: String, val modelo: String, val ano: Int);

val carro = Carro("Ford", "Ka", 2020)
var novocarro = carro.copy(ano = 2021)
println("Carro: ${carro.marca} ${carro.modelo} ${carro

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

// val dia = DiaSemana.SEGUNDA
// if(dia == DiaSemana.SEGUNDA){
//     println("Hoje é segunda-feira")
// }

// Generics
class Print<T>{
    constructor(valor: T){
        val tipo = valor!!::class.simpleName
        println("Tipo: ${tipo}")
    }
}

var pi = Print<Int>(10);
var ps = Print<String>("Hello, World!");
var pb = Print<Boolean>(true);


// Annotations
annotation class Info(val descricao: String = "");

@Info(descricao = "Função para somar dois números")
fun soma(a: Int, b: Int): Int{
    return a + b
}

// Extension Function
fun String.truncate(maxLength: Int): String {
    return if (this.length <= maxLength) this else take(maxLength - 3) + "..."
}

val texto = "Este é um texto muito longo que precisa ser truncado."
val textoTruncado = texto.truncate(20)
println("Texto truncado: ${textoTruncado}")

// NUll Safety
var variavelQuePodeSerNull: String? = null
variavelQuePodeSerNull = "Agora não é mais null"
println("Variavel: ${variavelQuePodeSerNull}")
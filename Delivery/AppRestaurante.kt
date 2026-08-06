fun cadastro(){

}

fun login(): Restaurante?{
    println("Informe o email:")
    val email = readln().toString()
    // Lógica de login aqui
    return null
}

enum class OpcoesPlanoDeVoo{
    CADASTRO,
    LOGIN,
    SAIR
}

fun main(){
    print("===== APP RESTAURANTE =====")
    println("Informe a opção:")
    println("1. Cadastrar")
    println("2. Login")
    println("0. Sair")
    while(opcao != OpcoesPlanoDeVoo.SAIR){
        println(">>")
        val opcaoUsuario = readln().toInt();
        val opcao = OpcoesPlanoDeVoo.entries.getOrNull(index) 
        when(opcao){
            CADASTRO -> cadastro()
            LOGIN -> login()
            else -> println("Opção inválida!")
        }   
    }
}
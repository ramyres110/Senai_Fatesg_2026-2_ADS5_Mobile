/*
{
  "nome": "Pizzaria do Bairro",
  "email": "contato@pizzariadobairro.com",
  "endereco": "Rua das Flores, 123",
  "menu": [
    {
      "numero_item": 1,
      "descricao": "Pizza Calabresa",
      "preco": 45.00
    },
    {
      "numero_item": 2,
      "descricao": "Refrigerante 2L",
      "preco": 10.00
    }
  ]
}
 */
data class Restaurante(val nome: String, val email: String, val endereco: String, val menu: List<Item>)
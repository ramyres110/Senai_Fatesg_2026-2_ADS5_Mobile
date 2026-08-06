ESPECIFICACAO TECNICA: SISTEMA DE DELIVERY KOTLIN VIA CONSOLE

1. APRESENTACAO DO SISTEMA

O sistema e uma solucao de delivery operada via linha de comando (CLI), composta
por duas aplicacoes independentes:

  1. App Restaurante
  2. App Cliente

A persistencia de dados e a comunicacao entre as aplicacoes ocorrem por meio
de arquivos locais em formato JSON e CSV:

  - restaurante_ID.json : Arquivos individuais com cadastro, e-mail unico e
                          cardapio de cada restaurante.
  - clientes.json       : Arquivo central com dados dos clientes e telefone unico.
  - pedidos.csv         : Arquivo central compartilhado para registro de pedidos,
                          utilizando e-mail e telefone como chaves de filtro.


2. ESTRUTURA DE DADOS

[A] Arquivo JSON do Restaurante (Exemplo: restaurante_1.json)
-------------------------------------------------------------
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

[B] Arquivo JSON de Clientes (clientes.json)
--------------------------------------------
[
  {
    "nome": "Joao Silva",
    "telefone": "62999998888",
    "endereco": "Av. Central, 500"
  }
]

[C] Arquivo CSV de Pedidos (pedidos.csv)
----------------------------------------
Estrutura do Cabecalho:
id_pedido;data_hora;email_restaurante;nome_restaurante;telefone_cliente;nome_cliente;endereco_cliente;numero_item;quantidade;descricao_item;valor_unitario;valor_total_item;status

Codigos de Status do Pedido:
  0 - SOLICITADO
  1 - EM PREPARACAO
  2 - AGUARDANDO ENTREGADOR
  3 - EM TRANSITO
  4 - ENTREGUE


3. REQUISITOS DO APLICATIVO DO RESTAURANTE


[3.1] Acesso e Autenticacao
  - Ao iniciar, exibe as opcoes:
      [1] Entrar como Restaurante Existente
      [2] Novo Cadastro
 
  - Entrar:
      * Solicita o E-mail.
      * Busca o e-mail nos arquivos "restaurante_ID.json".
      * Se encontrado, carrega a sessao; caso contrario, exibe mensagem de erro.

  - Novo Cadastro:
      * Solicita Nome, E-mail e Endereco.
      * Validacao de Unicidade: impede o registro se o E-mail ja estiver cadastrado.
      * Cadastro Inicial do Cardapio: entra em loop para adicao de itens
        solicitando "numero_item", "descricao" e "preco".
      * O loop e encerrado ao enviar entrada vazia no "numero_item" (Enter).
      * Salva o novo arquivo "restaurante_ID.json".

[3.2] Menu Principal do Restaurante
  - Opcao 1: Gerenciar Cardapio
      * [A] Ver Cardapio: Exibe os itens do menu atual.
      * [B] Adicionar Item: Solicita os dados do item e salva no JSON.
      * [C] Remover Item: Solicita o "numero_item" e remove do JSON.

  - Opcao 2: Visualizar Pedidos por Status
      * Le o arquivo "pedidos.csv".
      * Exibe apenas os registros onde "email_restaurante" seja igual ao do restaurante logado.
      * Organiza/filtra os pedidos pelos status de 0 a 4.

  - Opcao 3: Alterar Status do Pedido
      * Solicita o "id_pedido" e o novo codigo de status (0 a 4).
      * Reescreve o arquivo "pedidos.csv" atualizando o status do pedido.


4. REQUISITOS DO APLICATIVO DO CLIENTE

[4.1] Acesso e Autenticacao
  - Ao iniciar, exibe as opcoes:
      [1] Entrar
      [2] Novo Cadastro

  - Entrar:
      * Solicita o Telefone.
      * Consulta "clientes.json". Se encontrado, carrega a sessao com Nome e Endereco.

  - Novo Cadastro:
      * Solicita Nome, Telefone e Endereco.
      * Validacao de Unicidade: impede o registro se o Telefone ja existir em "clientes.json".
      * Grava o novo cliente em "clientes.json".

[4.2] Menu Principal do Cliente
  - Opcao 1: Realizar Novo Pedido
      * Lista os restaurantes cadastrados lendo a pasta com arquivos "restaurante_ID.json".
      * Exibe o cardapio do restaurante selecionado.
      * Selecao de Itens (Loop): Solicita "numero_item" e "quantidade" de forma
        iterativa ate que uma entrada vazia seja informada no "numero_item" (Enter).
      * Confirmacao: Exibe o resumo do pedido com valor total e solicita confirmacao [S/N].
      * Ao confirmar [S]: gera "id_pedido", atribui data/hora atual e grava as linhas
        em "pedidos.csv" contendo "email_restaurante", "telefone_cliente" e status inicial 0.

  - Opcao 2: Ver Pedidos em Andamento
      * Le "pedidos.csv" e exibe as solicitações do "telefone_cliente" logado
        com status menor que 4 (0, 1, 2 ou 3).

  - Opcao 3: Ver Pedidos Finalizados
      * Le "pedidos.csv" e exibe as solicitações do "telefone_cliente" logado
        com status igual a 4 (ENTREGUE).


5. RESULTADO ESPERADO

  * Identificacao Unica: E-mail exclusivo para restaurantes e Telefone exclusivo
    para clientes, garantindo ausencia de duplicidades.
  * Filtragem Precisa: Rastreabilidade completa de pedidos em "pedidos.csv"
    atraves dos campos "email_restaurante" e "telefone_cliente".
  * Gestao Dinamica de Menu: Flexibilidade para consulta, adicao e remocao de
    itens do cardapio no arquivo JSON do restaurante.
  * Operacao Desacoplada: Funcionamento independente entre as aplicacoes CLI,
    sincronizando o fluxo de pedidos por meio do sistema de arquivos locais.



ENTREGA: Enviar link do repositório como resposta!
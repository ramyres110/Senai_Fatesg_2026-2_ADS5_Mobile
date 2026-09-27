# ✈️ Trip Planner BR `v1.0.0`

**Trip Planner BR** é um aplicativo nativo Android para planejamento e gestão de viagens futuras. O app permite cadastrar a cidade de origem do usuário, organizar destinos com integração em tempo real à API do IBGE, planejar passeios em cada localidade e manter uma lista de contatos úteis (guias, hotéis e pousadas).

---

## 📌 Versão 1.0.0

A versão **1.0.0** traz a implementação completa dos fluxos principais do aplicativo:
* **Perfil do Usuário & Origem**: Cadastro e edição de nome, e-mail e cidade de origem (onde reside) com busca dinâmica por UF na API do IBGE.
* **Gestão de Destinos**: Adição de novos destinos com busca de municípios via IBGE, distância (Km), orçamento (R$), data prevista e status.
* **Filtros de Viagem**: Filtro interativo na tela principal por status (*Pendente*, *Em Andamento*, *Realizada*).
* **Detalhes do Destino**: Gestão completa de cada viagem, alteração dinâmica de status, gerenciamento de passeios planejados e contatos associados.

---

## 📱 Funcionalidades Principais

* 🏠 **Área de Pouso (Home)**:
  * Exibição da origem do usuário e saudações personalizadas.
  * Filtro por status da viagem (*Todos*, *Pendentes*, *Em Andamento*, *Realizadas*).
  * Listagem de destinos em cards com atalho para detalhes e criação.

* 👤 **Perfil do Usuário**:
  * Gerenciamento de dados pessoais (Nome, E-mail).
  * Definição e alteração da cidade de origem (Mora/Reside) consumindo a API de municípios do IBGE.

* ➕ **Cadastro de Destino**:
  * Seleção de Estado (UF) e carregamento automático dos municípios via API do IBGE.
  * Informação de orçamento previsto, distância em Km, data prevista, CEP e status.

* 📍 **Detalhes do Destino**:
  * Visualização completa das informações da viagem.
  * Alteração direta do status da viagem.
  * Cadastro e remoção de **Passeios Planejados**.
  * Cadastro e remoção de **Contatos Úteis** (Guias turísticos, hotéis, pousadas).
  * Opção para exclusão do destino.

---

## 🛠️ Tecnologias Utilizadas

* **[Kotlin](https://kotlinlang.org/)**: Linguagem oficial para desenvolvimento Android Nativo.
* **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: UI declarativa e moderna com Material Design 3.
* **[Room 3](https://developer.android.com/training/data-storage/room)**: Persistência local SQLite para entidades `Usuario`, `Origem`, `Destino`, `Passeio` e `Contato`.
* **[Retrofit 2](https://square.github.io/retrofit/) & Gson**: Consumo da API REST de localidades e municípios do IBGE.
* **[Navigation Compose](https://developer.android.com/jetpack/compose/navigation)**: Navegação tipo-segura (`@Serializable`) entre telas.
* **Arquitetura MVVM**: `ViewModel`, `StateFlow` e `Repository Pattern` para separação de responsabilidades.

---

## 🏗️ Estrutura do Projeto

```text
com.ramyres.tripplannerbr/
├── api/                   # Integração Retrofit com a API do IBGE
│   ├── ApiIBGE.kt
│   ├── ApiMunicipiosService.kt
│   └── CidadeDto.kt
├── data/                  # Banco de dados Room, Entidades e DAOs
│   ├── BancoDeDados.kt
│   ├── Converters.kt
│   ├── Usuario.kt / UsuarioDao.kt
│   ├── Origem.kt / OrigemDao.kt
│   ├── Destino.kt / DestinoDao.kt
│   ├── Passeio.kt / PasseioDao.kt
│   └── Contato.kt / ContatoDao.kt
├── model/                 # Repositórios e Enums
│   ├── enumeradores/StatusViagem.kt
│   └── repository/
│       └── ViagemRepository.kt
├── ui/                    # UI Compose, ViewModels e Navegação
│   ├── screen/
│   │   ├── AreaDePousoScreen.kt & AreaDePousoViewModel.kt
│   │   ├── PerfilUsuarioScreen.kt & PerfilUsuarioViewModel.kt
│   │   ├── CadastroDestinoScreen.kt & CadastroDestinoViewModel.kt
│   │   ├── DestinoDetalheScreen.kt & DestinoDetalheViewModel.kt
│   │   ├── ViagemViewModelFactory.kt
│   │   └── Navegacao.kt
│   └── theme/
└── MainActivity.kt        # Entry point da aplicação
```

---

## 🌐 APIs Utilizadas

* **API de Localidades do IBGE**:
  * Obtém os municípios brasileiros por estado/UF.
  * Endpoint base: `https://servicodados.ibge.gov.br/api/v1/localidades/`

---

## 🚀 Como Executar o Projeto

1. **Pré-requisitos**:
   * Android Studio (Ladybug / Meerkat ou superior).
   * JDK 17+.
   * Dispositivo físico ou emulador Android com API 26+ (Android 8.0+).

2. **Execução**:
   * Abra o projeto no **Android Studio**.
   * Aguarde o *Gradle Sync*.
   * Execute o app pressionando `Shift + F10` ou clicando em **Run**.

---

## 📄 Licença

Projeto acadêmico desenvolvido para a disciplina de Desenvolvimento Mobile.

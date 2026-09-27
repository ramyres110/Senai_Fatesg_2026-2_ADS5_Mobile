package com.ramyres.githubmobile.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.ramyres.githubmobile.R
import com.ramyres.githubmobile.ui.theme.GithubMobileTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramyres.githubmobile.model.GithubRepo
import com.ramyres.githubmobile.setup.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GithubMobileScreen() {
    var user: String by remember { mutableStateOf("") }
    var repos: List<GithubRepo> = remember { mutableStateListOf<GithubRepo>() }
    var loading: Boolean by remember { mutableStateOf(false) }

    GithubMobileTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)) {
                        OutlinedTextField(
                            modifier = Modifier.weight(3f),
                            value = user,
                            onValueChange = { it -> user = it },
                            label = { Text("Label") }
                        )
                        OutlinedIconButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp)
                                .fillMaxHeight(),
                            onClick = {
                                loading= true
                                if (user != ""){
                                    fetchUserData(user, { it ->
                                        if (it == null) {
                                            loading = false
                                            repos = emptyList()
                                            return@fetchUserData
                                        }

                                        repos = it!!
                                        user = ""
                                        loading = false
                                    })
                                }
                            },
                        ) {
                            if(!loading)
                                Icon(Icons.Outlined.Search, contentDescription = "Search")
                        }
                    }
                }
                Column(modifier = Modifier.fillMaxWidth().padding(10.dp, 0.dp)) {
                    if (repos.count() == 0) {
                        Text("Nenhum repositório encontrado")
                    } else {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Name",
                                modifier = Modifier.padding(5.dp).weight(3f)
                            )
                            Text(
                                text = "Stars",
                                modifier = Modifier.padding(5.dp).weight(1f)
                            )
                            Text(
                                text = "Watchers",
                                modifier = Modifier.padding(5.dp).weight(1f)
                            )
                            Text(
                                text = "Forks",
                                modifier = Modifier.padding(5.dp).weight(1f)
                            )
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = repos, key = { item -> item.name }) {
                                item ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = item.name,
                                        modifier = Modifier.padding(5.dp).weight(3f)
                                    )
                                    Text(
                                        text = "${item.stargazers_count}",
                                        modifier = Modifier.padding(5.dp).weight(1f)
                                    )
                                    Text(
                                        text = "${item.watchers_count}",
                                        modifier = Modifier.padding(5.dp).weight(1f)
                                    )
                                    Text(
                                        text = "${item.forks}",
                                        modifier = Modifier.padding(5.dp).weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun fetchUserData(user: String, setRepos: (List<GithubRepo>?) -> Unit) {
    // Sempre execute chamadas de rede em um thread de IO
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // 1. Chama o serviço através do cliente configurado
            val response = RetrofitClient.apiService.getUserRepos(user).execute()

            // 2. Verifica se a requisição foi bem-sucedida (HTTP Status 2xx)
            if (response.isSuccessful) {
                val repos: List<GithubRepo>? = response.body()

                // Mudar para Dispatchers.Main para atualizar a UI
                withContext(Dispatchers.Main) {
                    if (repos != null && repos.isNotEmpty()) {
                        setRepos(repos)
                    }
                }
            } else {
                // Erro na resposta do servidor (ex: 404 Not Found)
                setRepos(null)
            }
        } catch (e: Exception) {
            // Erro de conexão (ex: sem internet)
            e.printStackTrace()
        }
    }
}

@Preview
@Composable
fun GithubMobileScreenPreview() {
    GithubMobileScreen()
}
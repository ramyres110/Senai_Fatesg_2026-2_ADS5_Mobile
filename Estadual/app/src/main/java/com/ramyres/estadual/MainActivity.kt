package com.ramyres.estadual

import android.R
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramyres.estadual.ui.theme.EstadualTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EstadualTheme {
                var titulo by remember { mutableStateOf("Estadual") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(titulo) },
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.primary)
                        )
                    }
                ) { innerPadding ->

                    GreetingScreen(
                        modifier = Modifier.padding(innerPadding),
                        onTituloChange = {titulo = it}
                    )
                }
            }
        }
    }
}

class GreetingViewModel: ViewModel() {
    private val _firstName: MutableLiveData<String> = MutableLiveData("")
    private val _lastName: MutableLiveData<String> = MutableLiveData("")
    val firstName: LiveData<String> = _firstName
    val lastName: LiveData<String> = _lastName

    fun onFirstNameChange(newName: String) {
        _firstName.value = newName
    }

    fun onLastNameChange(newName: String) {
        _lastName.value = newName
    }
}

@Composable
fun GreetingScreen(greetingViewModel: GreetingViewModel = GreetingViewModel(),
                   modifier: Modifier = Modifier,
                   onTituloChange: (String) -> Unit = {}){
    //val (value, setValue) = remember { mutableStateOf("") }
    //var name: MutableState<String> = remember { mutableStateOf("") }
    var firstName = greetingViewModel.firstName.observeAsState("")
    var lastName = greetingViewModel.lastName.observeAsState("")
    Greeting(
        firstName = firstName.value,
        lastName = lastName.value,
        modifier = modifier,
        onFirstNameChange = { greetingViewModel.onFirstNameChange(it) },
        onLastNameChange = { greetingViewModel.onLastNameChange(it) },
        onTituloChange = onTituloChange
    )
}

@Composable
fun Greeting(firstName: String,
             lastName: String,
             modifier: Modifier = Modifier,
             onFirstNameChange: (String) -> Unit,
             onLastNameChange: (String) -> Unit,
             onTituloChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        EditText(
            label = "Nome",
            value = firstName,
            onValueChange = { onFirstNameChange(it) })
        EditText(
            label = "Sobrenome", value = lastName,
            onValueChange = { onLastNameChange(it) })
        Button(onClick = { onTituloChange("Bem vindo $firstName $lastName") }) {
            Text("Greetings")
        }
    }
}

@Composable
fun EditText(label: String, value: String, onValueChange: (it: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
    }
}

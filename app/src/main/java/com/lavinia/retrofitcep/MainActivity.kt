package com.lavinia.retrofitcep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lavinia.retrofitcep.model.dados.Endereco
import com.lavinia.retrofitcep.model.dados.RetrofitClient
import com.lavinia.retrofitcep.ui.theme.RetrofitCepTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitCepTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CepScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CepScreen(modifier: Modifier = Modifier) {
    var cep by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Resultado do endereço será exibido aqui") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = cep,
            onValueChange = { cep = it },
            label = { Text("Digite o CEP") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (cep.isNotEmpty()) {
                    coroutineScope.launch {
                        result = buscarEndereco(cep)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Buscar Endereço")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(text = result)
    }
}

suspend fun buscarEndereco(cep: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val endereco = RetrofitClient.enderecoService.findByCep(cep)

            """
            Logradouro: ${endereco.logradouro}
            Bairro: ${endereco.bairro}
            Localidade: ${endereco.localidade}
            UF: ${endereco.uf}
            """.trimIndent()
        } catch (e: Exception) {
            "Erro: ${e.message}"
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CepScreenPreview() {
    RetrofitCepTheme {
        CepScreen()
    }
}
package com.daw.tarea4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.daw.tarea4.ui.theme.Tarea4Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {

    private val countries = mutableStateOf<List<Pais>>(emptyList())
    private var regionSearchQuery = mutableStateOf("")
    private var capitalSearchQuery = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Tarea4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchBar(
                            regionSearchQuery.value,
                            capitalSearchQuery.value
                        ) { newRegionQuery, newCapitalQuery ->
                            regionSearchQuery.value = newRegionQuery
                            capitalSearchQuery.value = newCapitalQuery
                            obtenerPaisesPorRegion(regionSearchQuery.value)
                            obtenerPaisesPorCapital(capitalSearchQuery.value)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ListaPaises(countries.value)
                    }
                }
            }
        }

        obtenerPaises()
        obtenerPaisesPorCapital(capitalSearchQuery.value)
        obtenerPaisesPorRegion(regionSearchQuery.value)
    }

    @Composable
    fun SearchBar(
        regionSearchQuery: String,
        capitalSearchQuery: String,
        onSearchQueryChange: (String, String) -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = regionSearchQuery,
                onValueChange = {
                    val newText = it.replace(Regex("[\n]"), "")
                    onSearchQueryChange(newText, capitalSearchQuery)
                },
                label = { Text("Buscar por región") }
            )
            TextField(
                modifier = Modifier.weight(1f),
                value = capitalSearchQuery,
                onValueChange = {
                    val newText = it.replace(Regex("[\n]"), "")
                    onSearchQueryChange(regionSearchQuery, newText)
                },
                label = { Text("Buscar por capital") }
            )
            Divider(
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp)
            )
        }
    }

    @Composable
    fun ListaPaises(listaPaises: List<Pais>) {
        LazyColumn(
            modifier = Modifier.padding()
        ) {
            items(listaPaises) { pais ->
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberImagePainter(pais.flagUrl),
                        contentDescription = "Bandera de ${pais.capital}",
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Nombre: ")
                                }
                                append(pais.nombre ?: "Desconocido")
                            }
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Capital: ")
                                }
                                append(pais.capital.joinToString() ?: "Desconocido")
                            }
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Región: ")
                                }
                                append(pais.region ?: "Desconocido")
                            }
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("País: ")
                                }
                                append(pais.fifa ?: "Desconocido")
                            }
                        )
                    }
                }
                Divider(color = Color.LightGray)
            }
        }
    }

    private fun obtenerPaises() {
        val llamada: Call<List<Pais>> = InstanciaRetrofit.servicioPaises.obtenerPaises()
        llamada.enqueue(object : Callback<List<Pais>> {
            override fun onResponse(llamada: Call<List<Pais>>, respuesta: Response<List<Pais>>) {
                if (respuesta.isSuccessful) {
                    val listaPaises = respuesta.body()
                    countries.value = listaPaises ?: emptyList()
                    Log.d("Retrofit", "Datos de países recibidos correctamente: $listaPaises")
                } else {
                    Log.e("Retrofit", "Error en la respuesta: ${respuesta.message()}")
                }
            }

            override fun onFailure(llamada: Call<List<Pais>>, t: Throwable) {
                Log.e("Retrofit", "Error al realizar la llamada: ${t.message}")
            }
        })
    }

    private fun obtenerPaisesPorRegion(region: String) {
        val llamada: Call<List<Pais>> = InstanciaRetrofit.servicioPaises.obtenerPaisesPorRegion(region)
        llamada.enqueue(object : Callback<List<Pais>> {
            override fun onResponse(llamada: Call<List<Pais>>, respuesta: Response<List<Pais>>) {
                if (respuesta.isSuccessful) {
                    val listaPaises = respuesta.body()
                    countries.value = listaPaises ?: emptyList()
                    Log.d("Retrofit", "Datos de países recibidos correctamente: $listaPaises")
                } else {
                    Log.e("Retrofit", "Error en la respuesta: ${respuesta.message()}")
                }
            }

            override fun onFailure(llamada: Call<List<Pais>>, t: Throwable) {
                Log.e("Retrofit", "Error al realizar la llamada: ${t.message}")
            }
        })
    }

    private fun obtenerPaisesPorCapital(capital: String) {
        val llamada: Call<List<Pais>> = InstanciaRetrofit.servicioPaises.obtenerPaisesPorCapital(capital)
        llamada.enqueue(object : Callback<List<Pais>> {
            override fun onResponse(llamada: Call<List<Pais>>, respuesta: Response<List<Pais>>) {
                if (respuesta.isSuccessful) {
                    val listaPaises = respuesta.body()
                    countries.value = listaPaises ?: emptyList()
                    Log.d("Retrofit", "Datos de países recibidos correctamente: $listaPaises")
                } else {
                    Log.e("Retrofit", "Error en la respuesta: ${respuesta.message()}")
                }
            }

            override fun onFailure(llamada: Call<List<Pais>>, t: Throwable) {
                Log.e("Retrofit", "Error al realizar la llamada: ${t.message}")
            }
        })
    }
}
package com.daw.tarea4

data class Pais(
    val translations: Map<String, Traduccion>,
    val capital: List<String>,
    val region: String,
    val fifa: String,
    val flags: Map<String, String>
) {
    val nombre: String?
        get() = translations["spa"]?.common

    val flagUrl: String
        get() = flags["png"] ?: ""
}

data class Traduccion(
    val official: String,
    val common: String
)
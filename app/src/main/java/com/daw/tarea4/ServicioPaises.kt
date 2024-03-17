package com.daw.tarea4

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ServicioPaises {
    @GET("v3.1/all")
    fun obtenerPaises(): Call<List<Pais>>

    @GET("v3.1/region/{region}")
    fun obtenerPaisesPorRegion(@Path("region") region: String): Call<List<Pais>>

    @GET("v3.1/capital/{capital}")
    fun obtenerPaisesPorCapital(@Path("capital") region: String): Call<List<Pais>>
}
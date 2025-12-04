package com.unitec.zafirohome.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Tu dirección IP y Puerto
    private const val BASE_URL = "http://31.97.150.130:3000/"

    // --- GESTIÓN DE COOKIES (Session) ---
    // Esto es vital porque tu backend usa req.session
    private val cookieJar = object : CookieJar {
        private val cookieStore = HashMap<String, List<Cookie>>()

        // Guarda las cookies que envía el servidor (ej. connect.sid)
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies
        }

        // Envía las cookies guardadas en las siguientes peticiones
        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: ArrayList()
        }
    }

    // Configuración del cliente HTTP
    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar) // Conectamos el gestor de cookies
        // Aumentamos el tiempo de espera a 60s para subir imágenes tranquilamente
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Instancia pública de la API
    val api: ZafiroApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZafiroApiService::class.java)
    }
}
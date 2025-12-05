package com.unitec.zafirohome.network

import android.util.Log // Import Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "http://31.97.150.130:3000/"
    private const val TAG = "API_LOGS" // Tag for Logcat filtering

    // --- GESTIÓN DE COOKIES (Session) ---
    private val cookieJar = object : CookieJar {
        private val cookieStore = HashMap<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            // Optional: Log cookies received
            if (cookies.isNotEmpty()) Log.d(TAG, "Cookies received: ${cookies.size}")
            cookieStore[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: ArrayList()
        }
    }

    // Configuración del cliente HTTP
    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        // --- ADD THIS INTERCEPTOR BLOCK ---
        .addInterceptor { chain ->
            val request = chain.request()

            // 1. Log the Request URL
            Log.d(TAG, "api called: ${request.method} ${request.url.encodedPath}")

            // Proceed with the request
            val response = chain.proceed(request)

            // 2. Log the Response
            // We use peekBody to read the content without consuming it (preventing crashes)
            val responseBody = response.peekBody(Long.MAX_VALUE).string()

            Log.d(TAG, "server response (${response.code}): $responseBody")

            response // Return the original response
        }
        // ----------------------------------
        .build()

    val api: ZafiroApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZafiroApiService::class.java)
    }
}
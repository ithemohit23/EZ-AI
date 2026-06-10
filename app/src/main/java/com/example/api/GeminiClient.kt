package com.example.api

import com.example.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// --- Gemini API Data structures using Moshi (compatible with converter-moshi) ---

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiContent(
    val parts: List<GeminiPart>
)

data class GeminiPart(
    val text: String
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

data class GeminiCandidate(
    val content: GeminiContent?
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(GeminiApiService::class.java)
    }
}

/**
 * Executes a text suggestion prompt securely using our injected key!
 */
suspend fun queryGeminiAI(userPrompt: String, systemPrompt: String = "You are EZ with AI, an expert exam preparation buddy for SSC, Railway, NDA, CDS and banking. Give highly informative, point-wise, structured replies. Keep comments under 150 words."): String {
    val apiKey = BuildConfig.GEMINI_API_KEY
    if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
        return "Gemini API key is not configured. Please enter your GEMINI_API_KEY securely into the Secrets panel in AI Studio."
    }

    val request = GeminiRequest(
        contents = listOf(
            GeminiContent(parts = listOf(GeminiPart(text = userPrompt)))
        ),
        systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
    )

    return try {
        val response = RetrofitClient.service.generateContent(apiKey, request)
        val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        text ?: "No answer received from Gemini."
    } catch (e: Exception) {
        "Failed to consult Gemini. Error: ${e.localizedMessage ?: e.message}"
    }
}

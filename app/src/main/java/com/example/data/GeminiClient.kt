package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val MODEL_NAME = "gemini-3.5-flash"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    suspend fun generateResponse(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key is missing or invalid. Please configure your GEMINI_API_KEY in the AI Studio Secrets panel to activate the AI Chat and Planner."
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey"

        val jsonRequest = JSONObject().apply {
            val contentsArray = JSONArray().apply {
                val partsArray = JSONArray().apply {
                    put(JSONObject().put("text", prompt))
                }
                put(JSONObject().put("parts", partsArray))
            }
            put("contents", contentsArray)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonRequest.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string()
                if (!response.isSuccessful || bodyString == null) {
                    return@withContext "API error ${response.code}: Please verify your API Key and Network Settings."
                }

                val rootJson = JSONObject(bodyString)
                val candidates = rootJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    if (content != null) {
                        val parts = content.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "No text part.")
                        }
                    }
                }
                "No content returned from AI assistant. Try rephrasing."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API contact failed", e)
            "Error contacting AI. Please ensure your internet connection works and retry. details: ${e.localizedMessage}"
        }
    }
}

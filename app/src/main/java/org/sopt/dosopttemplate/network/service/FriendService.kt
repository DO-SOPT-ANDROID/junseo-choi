package org.sopt.dosopttemplate.network.service

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.dosopttemplate.BuildConfig
import org.sopt.dosopttemplate.domain.model.FriendDto
import org.sopt.dosopttemplate.domain.model.OpenApiResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendService {
    @GET("api/users")
    fun getFriendList(
        @Query("page") page: Int,
    ): Call<OpenApiResponse<List<FriendDto>>>
}

object FriendApiFactory {
    private const val FRIEND_BASE_URL = BuildConfig.FRIEND_BASE_URL

    private fun getLogOkHttpClient(): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d("Retrofit2", "CONNECTION INFO -> $message")
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(getLogOkHttpClient())
        .build()

    val friendRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FRIEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    inline fun <reified T> create(): T = friendRetrofit.create<T>(T::class.java)
}
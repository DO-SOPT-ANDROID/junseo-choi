package org.sopt.dosopttemplate.network

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.sopt.dosopttemplate.BuildConfig
import org.sopt.dosopttemplate.network.service.AuthService
import org.sopt.dosopttemplate.network.service.FriendService
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object ApiFactory {

    lateinit var url: String

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            },
        )
    }

    private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(getLoggingInterceptor())
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(provideOkHttpClient())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    inline fun <reified T> create(url: String): T {
        this.url = url
        return retrofit.create<T>(T::class.java)
    }
}

object ServicePool {
    private const val AUTH_BASE_URL = BuildConfig.AUTH_BASE_URL
    private const val FRIEND_BASE_URL = BuildConfig.FRIEND_BASE_URL

    val authService = ApiFactory.create<AuthService>(AUTH_BASE_URL)
    val friendService = ApiFactory.create<FriendService>(FRIEND_BASE_URL)
}

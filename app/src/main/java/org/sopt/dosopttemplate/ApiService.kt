package org.sopt.dosopttemplate

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class RequestSignInDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class ResponseSignInDto(
    @SerialName("id")
    val id: Int,
    @SerialName("username")
    val username: String,
    @SerialName("nickname")
    val nickname: String,
)

@Serializable
data class RequestSignUpDto(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("nickname")
    val nickname: String,
)

@Serializable
data class ResponseGetUserInfoDto(
    @SerialName("username")
    val username: String,
    @SerialName("nickname")
    val nickname: String,
)

@Serializable
data class FriendDto(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("avatar")
    val avatar: String,
)

@Serializable
data class OpenApiResponse<T>(
    @SerialName("page") val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("total") val total: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("data") val data: T,
    @SerialName("support") val support: SupportDto,
)

@Serializable
data class SupportDto(
    @SerialName("url") val url: String,
    @SerialName("text") val text: String,
)

interface FriendService {
    @GET("api/users")
    fun getFriendList(
        @Query("page") page: Int,
    ): Call<OpenApiResponse<List<FriendDto>>>

}

interface AuthService {
    @POST("api/v1/members/sign-in")
    fun signIn(
        @Body request: RequestSignInDto,
    ): Call<ResponseSignInDto>

    @POST("api/v1/members")
    fun signUp(
        @Body request: RequestSignUpDto,
    ): Call<Unit>

    @GET("api/v1/members/{memberId}")
    fun getUserInfo(
        @Path("memberId") id: Int,
    ): Call<ResponseGetUserInfoDto>
}

object AuthApiFactory {
    private const val AUTH_BASE_URL = BuildConfig.AUTH_BASE_URL

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

    val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    inline fun <reified T> create(): T = authRetrofit.create<T>(T::class.java)
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

object ServicePool {
    val authService = AuthApiFactory.create<AuthService>()
    val friendService = FriendApiFactory.create<FriendService>()
}
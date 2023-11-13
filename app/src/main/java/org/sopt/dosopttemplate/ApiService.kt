package org.sopt.dosopttemplate

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
    @SerialName("password")
    val password: String,
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
data class ResponseSignUpDto(
    @SerialName("message")
    val message: String,
)

@Serializable
data class RequestGetUserInfoDto(
    @SerialName("memberId")
    val id: Int,
)

@Serializable
data class ResponseGetUserInfoDto(
    @SerialName("id")
    val id: Int,
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


interface AuthService {
    @POST("api/v1/members/sign-in")
    fun signIn(
        @Body request: RequestSignInDto,
    ): Call<ResponseSignInDto>

    @POST("api/v1/members")
    fun signUp(
        @Body request: RequestSignUpDto,
    ): Call<ResponseSignUpDto>

    @GET("api/v1/members/{memberId}")
    fun getUserInfo(
        @Path("memberId") id: Int,
    ): Call<ResponseGetUserInfoDto>

    @GET("users")
    fun getFriendList(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Call<OpenApiResponse<List<FriendDto>>>

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
}

object ApiFactory {
    private const val AUTH_BASE_URL = BuildConfig.AUTH_BASE_URL
    private const val FRIEND_BASE_URL = "https://reqres.in/api/"

    private fun getLogOkHttpClient(): Interceptor {
        val interceptor = HttpLoggingInterceptor {}
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(getLogOkHttpClient())
        .build()


    val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val friendRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(FRIEND_BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    inline fun <reified T> create(auth: Boolean = true): T {
        return if (auth) authRetrofit.create(T::class.java)
        else friendRetrofit.create(T::class.java)
    }
}

object ServicePool {
    val authService = ApiFactory.create<AuthService>()
}
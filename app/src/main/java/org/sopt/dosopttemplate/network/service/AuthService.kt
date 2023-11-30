package org.sopt.dosopttemplate.network.service

import org.sopt.dosopttemplate.network.dto.req.SignInRequest
import org.sopt.dosopttemplate.network.dto.res.SignInResponse
import org.sopt.dosopttemplate.network.dto.req.SignUpRequest
import org.sopt.dosopttemplate.network.dto.res.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("api/v1/members/sign-in")
    suspend fun signIn(
        @Body request: SignInRequest,
    ): SignInResponse

    @POST("api/v1/members")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): Unit

    @GET("api/v1/members/{memberId}")
    suspend fun getUserInfo(
        @Path("memberId") id: Int,
    ): UserInfoResponse
}
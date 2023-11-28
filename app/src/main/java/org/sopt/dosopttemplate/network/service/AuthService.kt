package org.sopt.dosopttemplate.network.service

import org.sopt.dosopttemplate.domain.model.RequestSignUpDto
import org.sopt.dosopttemplate.network.dto.BaseResponse
import org.sopt.dosopttemplate.network.dto.SignInRequest
import org.sopt.dosopttemplate.network.dto.SignInResponse
import org.sopt.dosopttemplate.network.dto.SignUpRequest
import org.sopt.dosopttemplate.network.dto.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("api/v1/members/sign-in")
    suspend fun signIn(
        @Body request: SignInRequest,
    ): BaseResponse<SignInResponse>

    @POST("api/v1/members")
    suspend fun signUp(
        @Body request: SignUpRequest,
    ): BaseResponse<Unit>

    @GET("api/v1/members/{memberId}")
    suspend fun getUserInfo(
        @Path("memberId") id: Int,
    ): BaseResponse<UserInfoResponse>
}
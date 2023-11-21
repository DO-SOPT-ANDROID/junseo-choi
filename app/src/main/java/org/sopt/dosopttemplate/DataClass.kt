package org.sopt.dosopttemplate

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("profileImage") val profileImage: String,
    @SerialName("mbti") val mbti: String,
    @SerialName("birthday") val birthday: String,
    @SerialName("self_description") val self_description: String,
)

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

@Serializable
data class UserInfoBundle(
    @SerialName("profileImage") val profileImage: String,
    @SerialName("userName") val userName: String,
    @SerialName("nickName") val nickName: String,
    @SerialName("mbti") val mbti: String,
    @SerialName("birthday") val birthday: String,
    @SerialName("self_description") val self_description: String,
)
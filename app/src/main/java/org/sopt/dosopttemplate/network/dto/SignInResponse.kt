package org.sopt.dosopttemplate.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("username")
    val username: String = ""
)
package org.sopt.dosopttemplate.network.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("password")
    val password: String = "",
    @SerialName("username")
    val username: String = ""
)
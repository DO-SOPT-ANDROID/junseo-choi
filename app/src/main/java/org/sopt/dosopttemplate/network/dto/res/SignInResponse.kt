package org.sopt.dosopttemplate.network.dto.res


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponse(
    @SerialName("id")
    val id: Int = -1,
    @SerialName("username")
    val username: String = "",
    @SerialName("nickname")
    val nickname: String = ""
)
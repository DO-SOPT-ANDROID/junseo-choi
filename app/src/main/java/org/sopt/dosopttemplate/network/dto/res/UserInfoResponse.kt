package org.sopt.dosopttemplate.network.dto.res


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("username")
    val username: String = ""
)
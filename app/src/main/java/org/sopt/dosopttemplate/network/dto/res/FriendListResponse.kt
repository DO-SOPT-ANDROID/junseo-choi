package org.sopt.dosopttemplate.network.dto.res


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendListResponse(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("page")
    val page: Int = 0,
    @SerialName("per_page")
    val perPage: Int = 0,
    @SerialName("support")
    val support: Support = Support(),
    @SerialName("total")
    val total: Int = 0,
    @SerialName("total_pages")
    val totalPages: Int = 0
) {
    @Serializable
    data class Data(
        @SerialName("avatar")
        val avatar: String = "",
        @SerialName("email")
        val email: String = "",
        @SerialName("first_name")
        val firstName: String = "",
        @SerialName("id")
        val id: Int = 0,
        @SerialName("last_name")
        val lastName: String = ""
    )

    @Serializable
    data class Support(
        @SerialName("text")
        val text: String = "",
        @SerialName("url")
        val url: String = ""
    )
}
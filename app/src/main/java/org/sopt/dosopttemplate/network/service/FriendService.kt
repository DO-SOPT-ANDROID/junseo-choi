package org.sopt.dosopttemplate.network.service

import org.sopt.dosopttemplate.network.dto.BaseResponse
import org.sopt.dosopttemplate.network.dto.FriendListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendService {
    @GET("api/users")
    suspend fun getFriendList(
        @Query("page") page: Int,
    ): BaseResponse<FriendListResponse>
}
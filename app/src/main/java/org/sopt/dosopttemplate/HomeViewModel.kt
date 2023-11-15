package org.sopt.dosopttemplate

import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeViewModel : ViewModel() {
    private val friendService = ServicePool.friendService

    private val allFriends = mutableListOf<FriendDto>()

    init {
        fetchFriendsData()
    }

    private fun fetchFriendsData() {
        val call = friendService.getFriendList(1)

        call.enqueue(object : Callback<OpenApiResponse<List<FriendDto>>> {
            override fun onResponse(
                call: Call<OpenApiResponse<List<FriendDto>>>,
                response: Response<OpenApiResponse<List<FriendDto>>>,
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    data?.let { allFriends.addAll(it) }
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<OpenApiResponse<List<FriendDto>>>, t: Throwable) {
                // 실패 처리
            }
        })
    }

    fun getAllFriends(): List<FriendDto> {
        return allFriends
    }
}
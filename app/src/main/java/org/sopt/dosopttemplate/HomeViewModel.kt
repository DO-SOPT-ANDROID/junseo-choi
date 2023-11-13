package org.sopt.dosopttemplate

import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeViewModel : ViewModel() {
    private val apiService: AuthService

    private val allFriends = mutableListOf<FriendDto>()

    init {
        apiService = ApiFactory.create()

        fetchFriendsData()
    }

    private fun fetchFriendsData() {
        val call = apiService.getFriendList(1, 6)

        call.enqueue(object : Callback<AuthService.OpenApiResponse<List<FriendDto>>> {
            override fun onResponse(
                call: Call<AuthService.OpenApiResponse<List<FriendDto>>>,
                response: Response<AuthService.OpenApiResponse<List<FriendDto>>>,
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    data?.let { allFriends.addAll(it) }
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<AuthService.OpenApiResponse<List<FriendDto>>>, t: Throwable) {
                // 실패 처리
            }
        })
    }

    fun getAllFriends(): List<FriendDto> {
        return allFriends
    }
}
package org.sopt.dosopttemplate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val friendService = ServicePool.friendService
    private val _allFriends = MutableLiveData<List<FriendDto>>()
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage
    val allFriends: LiveData<List<FriendDto>> get() = _allFriends

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
                    data?.let { _allFriends.value = it }
                } else {
                    ifServerError()
                }
            }
            override fun onFailure(call: Call<OpenApiResponse<List<FriendDto>>>, t: Throwable) {
                ifServerError()
            }
        })
    }
    private fun ifServerError() {
        _toastMessage.value = R.string.server_error.toString()
    }
}
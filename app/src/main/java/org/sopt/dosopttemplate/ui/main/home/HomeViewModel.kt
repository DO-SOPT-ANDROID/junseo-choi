package org.sopt.dosopttemplate.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.network.ServicePool
import org.sopt.dosopttemplate.network.dto.res.FriendListResponse
import org.sopt.dosopttemplate.network.dto.res.UserInfoResponse

class HomeViewModel : ViewModel() {
    private val _toastMessage = MutableLiveData<String>()
    private val _userInfo = MutableLiveData<UserInfoResponse>()
    private val _friendList = MutableLiveData<List<FriendListResponse.Data>>()

    val toastMessage: LiveData<String> get() = _toastMessage
    val userInfo: LiveData<UserInfoResponse> get() = _userInfo
    val friendList: LiveData<List<FriendListResponse.Data>> get() = _friendList

    fun getUserInfo(id: Int) {
        viewModelScope.launch {
            runCatching {
                ServicePool.authService.getUserInfo(id)
            }.onSuccess { response ->
                _userInfo.value = response
            }.onFailure {
                ifServerError()
            }
        }
    }
    fun getFriendInfo(page: Int = 1) {
        viewModelScope.launch {
            runCatching {
                ServicePool.friendService.getFriendList(page)
            }.onSuccess { response ->
                _friendList.value = response.data
            }.onFailure {
                ifServerError()
            }
        }
    }

    private fun ifServerError() {
        _toastMessage.value = R.string.server_error.toString()
    }
}
package org.sopt.dosopttemplate.ui.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.sopt.dosopttemplate.network.ServicePool
import org.sopt.dosopttemplate.network.dto.req.SignInRequest
import org.sopt.dosopttemplate.network.dto.res.SignInResponse
import retrofit2.HttpException

class SignInViewModel : ViewModel() {
    val signInStatus: MutableStateFlow<SignInStatus?> = MutableStateFlow(null)

    private val _userInfo = MutableStateFlow<SignInResponse?>(null)
    val userInfo: StateFlow<SignInResponse?> get() = _userInfo

    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            runCatching {
                ServicePool.authService.signIn(SignInRequest(password, userName))
            }.onSuccess { response ->
                _userInfo.value = response
                signInStatus.value = SignInStatus.SUCCESS
            }.onFailure { exception ->
                Log.e("NetworkTest", "error:$exception")
                if (exception is HttpException && exception.code() == 400) {
                    signInStatus.value = SignInStatus.FAILURE
                } else {
                    signInStatus.value = SignInStatus.SERVER_ERROR
                }
            }
        }
    }
}

enum class SignInStatus {
    SUCCESS,
    FAILURE,
    SERVER_ERROR
}
package org.sopt.dosopttemplate.ui.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.dosopttemplate.network.ServicePool
import org.sopt.dosopttemplate.network.dto.req.SignInRequest
import org.sopt.dosopttemplate.network.dto.res.SignInResponse
import retrofit2.HttpException


class SignInViewModel : ViewModel() {
    private val _isSignInSuccessful = MutableLiveData<Boolean>()
    val isSignUpSuccessful: LiveData<Boolean> get() = _isSignInSuccessful

    private val _isSignInError = MutableLiveData<Boolean>()
    val isSignInError: LiveData<Boolean> get() = _isSignInError

    private val _userInfo = MutableLiveData<SignInResponse>()
    val userInfo: LiveData<SignInResponse> get() = _userInfo

    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            runCatching {
                ServicePool.authService.signIn(SignInRequest(password, userName))
            }.onSuccess { response ->
                _userInfo.value = response.data ?: SignInResponse()
                _isSignInSuccessful.value = true
            }.onFailure { exception ->
                Log.e("NetworkTest", "error:$exception")
                _isSignInSuccessful.value = false
                _isSignInError.value = exception is HttpException && exception.code() == 400
            }
        }
    }
}
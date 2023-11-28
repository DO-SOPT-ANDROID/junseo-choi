package org.sopt.dosopttemplate.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.dosopttemplate.network.ServicePool
import org.sopt.dosopttemplate.network.dto.req.SignUpRequest
import retrofit2.HttpException


class SignUpViewModel : ViewModel() {
    private val _isSignUpSuccessful = MutableLiveData<Boolean>()
    val isSignUpSuccessful: LiveData<Boolean> get() = _isSignUpSuccessful

    private val _isSignUpError = MutableLiveData<Boolean>()
    val isSignUpError: LiveData<Boolean> get() = _isSignUpError

    fun signUp(userName: String, password: String, nickName: String) {
        viewModelScope.launch {
            runCatching {
                ServicePool.authService.signUp(SignUpRequest(userName, password, nickName))
            }.onSuccess {
                _isSignUpSuccessful.value = true
            }.onFailure { exception ->
                Log.e("NetworkTest", "error:$exception")
                _isSignUpSuccessful.value = false
                _isSignUpError.value = exception is HttpException && exception.code() == 400
            }
        }
    }
}
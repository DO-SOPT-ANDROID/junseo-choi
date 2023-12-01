package org.sopt.dosopttemplate.ui.signup

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

    private val _isUserNameValid = MutableLiveData<Boolean>()
    val isUserNameValid: LiveData<Boolean> get() = _isUserNameValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> get() = _isPasswordValid

    private val _isNickNameValid = MutableLiveData<Boolean>()
    val isNickNameValid: LiveData<Boolean> get() = _isNickNameValid

    private val _isSomeValueNotEmpty = MutableLiveData<Boolean>()
    val isSomeValueNotEmpty: LiveData<Boolean> get() = _isSomeValueNotEmpty

    private val _isAllValueEmptyAndValid = MutableLiveData<Boolean>()
    val isAllValueEmptyAndValid: LiveData<Boolean> get() = _isAllValueEmptyAndValid

    fun validateInput(userName: String, password: String, nickName: String) {
        _isUserNameValid.value = isUserNameValid(userName)
        _isPasswordValid.value = isPasswordValid(password)
        _isNickNameValid.value = isNickNameValid(nickName)

        _isSomeValueNotEmpty.value =
            userName.isNotEmpty() || password.isNotEmpty() || nickName.isNotEmpty()
        _isAllValueEmptyAndValid.value =
            isUserNameValid(userName) && isPasswordValid(password) && isNickNameValid(nickName) && userName.isNotEmpty() && password.isNotEmpty() && nickName.isNotEmpty()
    }

    private fun isUserNameValid(userName: String): Boolean {
        return userName.isEmpty() || userName.length in 6..10
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.isEmpty() || (password.length in 6..12 && password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@\$!%*#?&])[a-zA-Z\\d@\$!%*#?&]+\$")))
    }

    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.isEmpty() || nickName.length in 2..12
    }

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
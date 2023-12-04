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
    private val _isSignInSuccessful = MutableStateFlow<Boolean>(false)
    val isSignInSuccessful: StateFlow<Boolean> get() = _isSignInSuccessful

    private val _isSignInError = MutableStateFlow<Boolean>(false)
    val isSignInError: StateFlow<Boolean> get() = _isSignInError

    private val _userInfo = MutableStateFlow<SignInResponse?>(null)
    val userInfo: StateFlow<SignInResponse?> get() = _userInfo

    fun signIn(userName: String, password: String) {
        viewModelScope.launch {
            runCatching {
                ServicePool.authService.signIn(SignInRequest(password, userName))
            }.onSuccess { response ->
                _userInfo.value = response
                _isSignInSuccessful.value = true
            }.onFailure { exception ->
                Log.e("NetworkTest", "error:$exception")
                _isSignInSuccessful.value = false
                _isSignInError.value = exception is HttpException && exception.code() == 400
            }
        }
    }
}
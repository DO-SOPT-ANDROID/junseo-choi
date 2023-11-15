package org.sopt.dosopttemplate

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.dosopttemplate.ServicePool.authService
import org.sopt.dosopttemplate.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Serializable
data class UserInfo(
    @SerialName("profileImage") val profileImage: String,
    @SerialName("mbti") val mbti: String,
    @SerialName("birthday") val birthday: String,
    @SerialName("self_description") val self_description: String
)


enum class MBTIType {
    INFP, ENFP, ESFJ, ISFJ, ISFP, ESFP, INTP, INFJ, ENFJ, ENTP, ESTJ, ISTJ, INTJ, ISTP, ESTP, ENTJ
}

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val sharedPreferences by lazy {
        getSharedPreferences(SharedPreferencesKeys.USER_INFO, MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.btnSignUpInbutton.setOnClickListener {
            val userName = binding.etSignUpInputid.text.toString()
            val password = binding.etSignUpInputpw.text.toString()
            val nickName = binding.etSignUpInputNick.text.toString()
            if (userName.isEmpty() || password.isEmpty() || nickName.isEmpty()) {
                showEmptyFieldDialog()
                return@setOnClickListener
            }

            val errorMessage = when {
                !isUserNameValid(userName) -> getString(R.string.user_id_error)
                !isPasswordValid(password) -> getString(R.string.password_error)
                !isNickNameValid(nickName) -> getString(R.string.nickname_error)
                !isMBTIValid(binding.etSignUpInputMBTI.text.toString().uppercase()) -> getString(R.string.mbti_error)
                binding.etSignUpInputMBTI.text.toString().uppercase() == "SEXY" -> getString(R.string.sexy_error)
                else -> null
            }

            if (errorMessage != null) {
                Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
            } else {
                authService.signUp(RequestSignUpDto(userName, password, nickName))
                    .enqueue(object : Callback<Unit> {
                        override fun onResponse(
                            call: Call<Unit>,
                            response: Response<Unit>,
                        ) {
                            when (response.code()) {
                                201 -> {
                                    val userInfo = UserInfo(
                                        profileImage = "https://avatars.githubusercontent.com/u/127238018?v=4", // 임시 데이터
                                        mbti = binding.etSignUpInputMBTI.text.toString().uppercase(),
                                        birthday = defaultUserInfo.birthday, // 임시 데이터
                                        self_description = binding.root.context.getString(R.string.test_text) // 임시 데이터
                                    )

                                    val userInfoJson = userInfo.toJson()
                                    userInfoJson.saveAsJsonFile("user_info.json", this@SignupActivity)

                                    binding.root.showSnackbar(getString(R.string.signup_success_message))
                                    val intent = Intent(this@SignupActivity, SigninActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                400 -> {
                                    val errorResponse = response.errorBody()?.string()
                                    binding.root.showSnackbar(errorResponse ?: getString(R.string.signup_failed))
                                }
                            }
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            binding.root.showSnackbar(getString(R.string.server_error))
                        }
                })
            }
        }
    }

    private fun isUserNameValid(userName: String): Boolean {
        return userName.length in 6..10 && userName.none { it.isWhitespace() || !it.isLetterOrDigit() }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 8..12 && !password.contains(" ")
    }

    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.length in 1..12 && !nickName.all { it.isWhitespace() }
    }

    private fun isMBTIValid(MBTI: String): Boolean {
        return MBTIType.values().any { it.name == MBTI }
    }

    private fun showEmptyFieldDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.empty_field_message))
            .setCancelable(false)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("알림")
        alert.show()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@SignupActivity, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


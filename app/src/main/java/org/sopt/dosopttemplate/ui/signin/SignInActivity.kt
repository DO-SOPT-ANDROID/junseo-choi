package org.sopt.dosopttemplate.ui.signin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosopttemplate.ui.main.MainActivity
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.domain.model.RequestSignInDto
import org.sopt.dosopttemplate.domain.model.ResponseSignInDto
import org.sopt.dosopttemplate.network.ServicePool
import org.sopt.dosopttemplate.databinding.ActivitySigninBinding
import org.sopt.dosopttemplate.ui.auth.SignupActivity
import org.sopt.dosopttemplate.util.hideKeyboard
import org.sopt.dosopttemplate.util.showSnackbar
import org.sopt.dosopttemplate.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SharedPreferencesKeys {
    const val USER_INFO = "UserInfo"
    const val USERNAME = "UserName"
    const val PASSWORD = "Password"
}

class SigninActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val authService = ServicePool.authService

    private var backPressedTime = 0L

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime <= 2000) {
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                showToast(getString(R.string.double_back_to_exit))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SharedPreferencesKeys.USER_INFO, MODE_PRIVATE)

        setupAutoLogin()

        setupClickListeners()

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setupAutoLogin() {
        val isAutoLogin = sharedPreferences.getBoolean("AutoLogin", false)
        binding.chkSignInAutologin.isChecked = isAutoLogin

        if (isAutoLogin) {
            restoreSavedCredentials()
        }
    }

    private fun restoreSavedCredentials() {
        val savedUserName = sharedPreferences.getString(SharedPreferencesKeys.USERNAME, "")
        val savedPassword = sharedPreferences.getString(SharedPreferencesKeys.PASSWORD, "")

        if (!savedUserName.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            binding.etSignInInputid.setText(savedUserName)
            binding.etSignInInputpw.setText(savedPassword)

            performSignIn(savedUserName, savedPassword)
        }
    }

    private fun setupClickListeners() {
        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }

        binding.btnSignInInbutton.setOnClickListener {
            val inputId = binding.etSignInInputid.text.toString()
            val inputPw = binding.etSignInInputpw.text.toString()

            if (inputId.isNotEmpty() && inputPw.isNotEmpty()) {
                performSignIn(inputId, inputPw)
            }
        }

        binding.etSignInInputpw.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputId = binding.etSignInInputid.text.toString()
                val inputPw = binding.etSignInInputpw.text.toString()

                if (inputId.isNotEmpty() && inputPw.isNotEmpty()) {
                    performSignIn(inputId, inputPw)
                }
                true
            } else {
                false
            }
        }

        binding.tvSignUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performSignIn(userName: String, password: String) {
        val isAutoLogin = binding.chkSignInAutologin.isChecked
        authService.signIn(RequestSignInDto(userName, password))
            .enqueue(object : Callback<ResponseSignInDto> {
                override fun onResponse(
                    call: Call<ResponseSignInDto>,
                    response: Response<ResponseSignInDto>,
                ) {
                    handleSignInResponse(response, isAutoLogin, password)
                }

                override fun onFailure(call: Call<ResponseSignInDto>, t: Throwable) {
                    binding.root.showSnackbar(getString(R.string.server_error))
                }
            })

        hideKeyboard(this, binding.root)
    }

    private fun handleSignInResponse(
        response: Response<ResponseSignInDto>,
        isAutoLogin: Boolean,
        password: String,
    ) {
        when (response.code()) {
            200 -> {
                val data: ResponseSignInDto = response.body()!!
                val username = data.username
                val id = data.id

                if (isAutoLogin) {
                    saveAutoLoginInfo(username, password)
                }

                val intent = Intent(this@SigninActivity, MainActivity::class.java)
                showToast("$username${getString(R.string.login_success)}")
                intent.putExtra("id", id)
                startActivity(intent)
                finish()
            }

            400 -> {
                val errorResponse = response.errorBody()?.string()
                binding.root.showSnackbar(
                    errorResponse ?: getString(R.string.login_failed)
                )
            }

            else -> {
                binding.root.showSnackbar(response.code().toString())
            }
        }
    }

    private fun saveAutoLoginInfo(userName: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("AutoLogin", true)
        editor.putString(SharedPreferencesKeys.USERNAME, userName)
        editor.putString(SharedPreferencesKeys.PASSWORD, password)
        editor.apply()
    }
}

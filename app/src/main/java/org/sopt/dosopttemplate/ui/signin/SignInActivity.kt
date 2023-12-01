package org.sopt.dosopttemplate.ui.signin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.ActivitySigninBinding
import org.sopt.dosopttemplate.ui.main.MainActivity
import org.sopt.dosopttemplate.ui.signup.SignUpActivity
import org.sopt.dosopttemplate.util.hideKeyboard
import org.sopt.dosopttemplate.util.showSnackbar
import org.sopt.dosopttemplate.util.showToast

object SharedPreferencesKeys {
    const val USERNAME = "UserName"
    const val PASSWORD = "Password"
}

class SignInActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var binding: ActivitySigninBinding
    private lateinit var sharedPreferences: SharedPreferences

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

        sharedPreferences = getSharedPreferences(SharedPreferencesKeys.USERNAME, MODE_PRIVATE)

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
            binding.etSignInIdInput.setText(savedUserName)
            binding.etSignInPwInput.setText(savedPassword)

            performSignIn(savedUserName, savedPassword)
        }
    }

    private fun setupClickListeners() {
        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }

        binding.btnSignInInbutton.setOnClickListener {
            val inputId = binding.etSignInIdInput.text.toString()
            val inputPw = binding.etSignInPwInput.text.toString()

            if (inputId.isNotEmpty() && inputPw.isNotEmpty()) {
                performSignIn(inputId, inputPw)
            } else {
                showEmptyFieldDialog()
            }
        }

        binding.etSignInPwInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputId = binding.etSignInIdInput.text.toString()
                val inputPw = binding.etSignInPwInput.text.toString()

                if (inputId.isNotEmpty() && inputPw.isNotEmpty()) {
                    performSignIn(inputId, inputPw)
                }
                true
            } else {
                false
            }
        }

        binding.tvSignUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun performSignIn(userName: String, password: String) {
        val isAutoLogin = binding.chkSignInAutologin.isChecked
        viewModel.signIn(userName, password)

        viewModel.isSignUpSuccessful.observe(this) { isSuccess ->
            if (isSuccess) {
                if (isAutoLogin) {
                    saveAutoLoginInfo(userName, password)
                }

                viewModel.userInfo.observe(this) {
                    val signInId = viewModel.userInfo.value?.id ?: -1
                    showToast(getString(R.string.login_success, signInId))
                    val intent = Intent(this@SignInActivity, MainActivity::class.java)
                    intent.putExtra("userId", signInId)
                    startActivity(intent)
                    finish()
                }
            } else {
                viewModel.isSignInError.observe(this) { isSignInError ->
                    if (isSignInError) {
                        binding.root.showSnackbar(getString(R.string.login_failed))
                    } else {
                        binding.root.showSnackbar(getString(R.string.server_error))
                    }
                    hideKeyboard(this, binding.root)
                }
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

    private fun showEmptyFieldDialog() {
        binding.root.showSnackbar(getString(R.string.empty_field_message))
    }
}

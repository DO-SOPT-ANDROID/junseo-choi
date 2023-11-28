package org.sopt.dosopttemplate.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.ActivitySignupBinding
import org.sopt.dosopttemplate.ui.signin.SigninActivity
import org.sopt.dosopttemplate.util.OnBackPressedCallback
import org.sopt.dosopttemplate.util.hideKeyboard
import org.sopt.dosopttemplate.util.showSnackbar

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignUpViewModel>()
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }

        OnBackPressedCallback {
            val intent = Intent(this@SignupActivity, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignUpInbutton.setOnClickListener {
            validateInputAndSignUp()
        }
    }

    private fun validateInputAndSignUp() {
        val userName = binding.etSignUpInputid.text.toString()
        val password = binding.etSignUpInputpw.text.toString()
        val nickName = binding.etSignUpInputNick.text.toString()
        if (userName.isEmpty() || password.isEmpty() || nickName.isEmpty()) {
            showEmptyFieldDialog()
            return
        }

        val errorMessage = when {
            !isUserNameValid(userName) -> getString(R.string.user_id_error)
            !isPasswordValid(password) -> getString(R.string.password_error)
            !isNickNameValid(nickName) -> getString(R.string.nickname_error)
            else -> null
        }

        if (errorMessage != null) {
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.signUp(userName, password, nickName)
            observeSignUpResult()
        }
    }

    private fun observeSignUpResult() {
        viewModel.isSignUpSuccessful.observe(this) { isSuccess ->
            if (isSuccess) {
                binding.root.showSnackbar(getString(R.string.signup_success_message))
            } else {
                viewModel.signUpError.observe(this) { isSignUpError ->
                    if (isSignUpError) {
                        binding.root.showSnackbar(getString(R.string.signup_failed))
                    } else {
                        binding.root.showSnackbar(getString(R.string.server_error))
                    }
                }
            }
        }
    }

    private fun isUserNameValid(userName: String): Boolean {
        return userName.length in 6..10 && userName.none { it.isWhitespace() || !it.isLetterOrDigit() }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 6..12 && !password.contains(" ")
    }

    private fun isNickNameValid(nickName: String): Boolean {
        return nickName.length in 1..12 && !nickName.all { it.isWhitespace() }
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
}
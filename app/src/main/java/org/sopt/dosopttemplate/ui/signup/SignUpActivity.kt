package org.sopt.dosopttemplate.ui.signup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.ActivitySignupBinding
import org.sopt.dosopttemplate.ui.signin.SignInActivity
import org.sopt.dosopttemplate.util.hideKeyboard
import org.sopt.dosopttemplate.util.showSnackbar
import org.sopt.dosopttemplate.util.showToast

class SignUpActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignUpViewModel>()
    private lateinit var binding: ActivitySignupBinding

    private val handler = Handler(Looper.getMainLooper())
    private val validationRunnable = object : Runnable {
        override fun run() {
            val userName = binding.etSignUpIdInput.text.toString()
            val password = binding.etSignUpPwInput.text.toString()
            val nickName = binding.etSignUpNickInput.text.toString()

            viewModel.validateInput(userName, password, nickName)

            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeView()
        setEventListeners()
        observeInputInfo()
    }

    private fun initializeView() {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnClickListener {
            hideKeyboard(this, binding.root)
        }
        binding.etSignUpNickInput.imeOptions = EditorInfo.IME_ACTION_DONE
    }

    private fun setEventListeners() {
        handler.post(validationRunnable)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.btnSignUpFinish.setOnClickListener { handleSignUp() }
    }

    private fun observeInputInfo() {
        viewModel.isUserNameValid.observe(this) { isValid ->
            updateInputLayoutState(
                binding.tlSignUpIdLayout,
                binding.tvSignUpIdWarning,
                isValid,
                getString(R.string.signup_id_warning)
            )
        }
        viewModel.isPasswordValid.observe(this) { isValid ->
            updateInputLayoutState(
                binding.tlSignUpPwLayout,
                binding.tvSignUpPwWarning,
                isValid,
                getString(R.string.signup_pw_warning)
            )
        }
        viewModel.isNickNameValid.observe(this) { isValid ->
            updateInputLayoutState(
                binding.tlSignUpNickLayout,
                binding.tvSignUpNickWarning,
                isValid,
                getString(R.string.signup_nick_warning)
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(validationRunnable)
    }

    private fun handleSignUp() {
        val userName = binding.etSignUpIdInput.text.toString()
        val password = binding.etSignUpPwInput.text.toString()
        val nickName = binding.etSignUpNickInput.text.toString()

        val isUserNameValid = viewModel.isUserNameValid.value == true
        val isPasswordValid = viewModel.isPasswordValid.value == true
        val isNickNameValid = viewModel.isNickNameValid.value == true

        viewModel.validateInput(userName, password, nickName)

        if (isUserNameValid && isPasswordValid && isNickNameValid) {
            viewModel.signUp(userName, password, nickName)
            observeSignUpResult()
        }
    }

    private fun observeSignUpResult() {
        viewModel.isSignUpSuccessful.observe(this) { isSuccess ->
            if (isSuccess) {
                navigateToSignInActivity()
                showToast(getString(R.string.signup_success_message))
            } else {
                handleSignUpError()
            }
        }
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleSignUpError() {
        viewModel.isSignUpError.observe(this) { isSignUpError ->
            if (isSignUpError) {
                binding.root.showSnackbar(getString(R.string.signup_failed))
            } else {
                binding.root.showSnackbar(getString(R.string.server_error))
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navigateToSignInActivity()
        }
    }

    private fun updateInputLayoutState(
        inputLayout: TextInputLayout,
        warningTextView: TextView,
        isValid: Boolean,
        warning: String,
    ) {
        viewModel.isAllValueEmpty.observe(this) { isAllEmpty ->
            if (isAllEmpty) {
                if (isValid) {
                    inputLayout.apply {
                        boxStrokeColor = Color.parseColor("#79747e")
                        warningTextView.text = null
                    }
                    binding.btnSignUpFinish.isEnabled = true
                } else {
                    inputLayout.apply {
                        boxStrokeColor = Color.parseColor("#ff2222")
                        warningTextView.text = warning
                    }
                    binding.btnSignUpFinish.isEnabled = false
                }
            } else {
                binding.btnSignUpFinish.isEnabled = false
            }
        }
    }
}
    package com.example.dicodingstory.ui.login

    import android.animation.AnimatorSet
    import android.animation.ObjectAnimator
    import android.app.AlertDialog
    import android.content.Intent
    import android.os.Bundle
    import android.view.View
    import android.widget.Toast
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.lifecycleScope
    import com.example.dicodingstory.data.response.LoginResponse
    import com.example.dicodingstory.databinding.ActivityLoginBinding
    import com.example.dicodingstory.ui.MainActivity
    import com.example.dicodingstory.viewmodel.AuthViewModel
    import com.example.dicodingstory.viewmodel.ViewModelFactory
    import com.google.gson.Gson
    import kotlinx.coroutines.launch
    import retrofit2.HttpException

    class LoginActivity : AppCompatActivity() {
        private lateinit var binding: ActivityLoginBinding
        private val viewModel by viewModels<AuthViewModel> {
            ViewModelFactory.getInstance(this)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.loginButton.setOnClickListener{
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                lifecycleScope.launch {
                    try {
                        val loginresult = viewModel.login(email, password)
                        val error = loginresult.error
                        if (error == false) {
                            val token = loginresult.loginResult?.token
                            viewModel.saveToken(token.toString())
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Success")
                                setMessage(loginresult.message)
                                setPositiveButton("Next") { _, _ ->
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }
                    } catch (e: HttpException) {
                        val jsonInString = e.response()?.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                        val errorMessage = errorBody.message
                        showToast(errorMessage.toString())
                        e.printStackTrace()
                    }finally {
                        showLoading(false)
                    }

                }
            }
            viewModel.isLoading.observe(this) {
                showLoading(it)
            }
            playAnimation()
            supportActionBar?.hide()
        }

        private fun playAnimation() {
            ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
            val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
            val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
            val emailLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
            val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
            val passwordLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
            val registerButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)

            AnimatorSet().apply {
                playSequentially(title, message, email, emailLayout, password, passwordLayout, registerButton)
                start()
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }
    }
package com.amadev.rando.ui.fragments.signin

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth

    private val usernameInputErrorMutableLiveData = MutableLiveData<String>()
    val usernameInputErrorLiveData = usernameInputErrorMutableLiveData

    private val passwordInputErrorMutableLiveData = MutableLiveData<String>()
    val passwordInputErrorLiveData = passwordInputErrorMutableLiveData

    private val emptyRepeatPasswordInputMutableLiveData = MutableLiveData<String>()
    val emptyRepeatPasswordInputLiveData = emptyRepeatPasswordInputMutableLiveData

    private val invalidUsernameMutableLiveData = MutableLiveData<String>()
    val invalidUsernameLiveData = invalidUsernameMutableLiveData

    private val loginSuccessfulMutableLiveData = MutableLiveData<Boolean>()
    val loginSuccessfulLiveData = loginSuccessfulMutableLiveData

    private val messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData = messageMutableLiveData

    private val setUpProgressbarVisibilityMutableLiveData = MutableLiveData<Boolean>()
    val setUpProgressbarVisibility = setUpProgressbarVisibilityMutableLiveData

    private val loginAutomaticallyIfPossibleMutableLiveData = MutableLiveData<Boolean>()
    val loginAutomaticallyIfPossibleLiveData = loginAutomaticallyIfPossibleMutableLiveData


    fun validateInput(username: String, password: String) {
        val username = username.trim()
        val password = password.trim()
        when {
            username.isEmpty() -> usernameInputErrorMutableLiveData.value = "Field cannot be empty"
            password.isEmpty() -> passwordInputErrorLiveData.value = "Field cannot be empty"
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
                .not() -> invalidUsernameMutableLiveData.value = "Please enter valid email adress"
            else -> doLoginWithEmailAndPassword(username, password)
        }
    }

    private fun doLoginWithEmailAndPassword(username: String, password: String) {
        auth = FirebaseAuth.getInstance()
        val username = username.trim()
        val password = password.trim()
        setUpProgressbarVisibilityMutableLiveData.value = true
        auth.signInWithEmailAndPassword(username, password)
            .addOnSuccessListener {
                if (auth.currentUser != null) {
                    if (auth.currentUser!!.isEmailVerified.not()) {
                        loginSuccessfulMutableLiveData.value = false
                        messageMutableLiveData.value = "Please verify email sent"
                        setUpProgressbarVisibilityMutableLiveData.value = false
                    } else {
                        setUpProgressbarVisibilityMutableLiveData.value = false
                        loginSuccessfulMutableLiveData.value = true
                    }
                }
            }
            .addOnFailureListener {
                messageMutableLiveData.value = "Failed to log in"
                setUpProgressbarVisibilityMutableLiveData.value = false
            }
    }

    fun loginAutomaticallyIfPossible() {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            loginSuccessfulMutableLiveData.value = true
        }
    }
}
package com.amadev.rando.ui.fragments.signin

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

sealed class Messages {
    object EmptyField : Messages()
    object InvalidEmail : Messages()
}

fun getMessage(message: Messages) =
    when (message) {
        is Messages.EmptyField -> "Field cannot be empty"
        is Messages.InvalidEmail -> "Please enter valid email address"
    }

class SignInViewModel : ViewModel() {

    companion object {
        val emptyField = Messages.EmptyField
        val invalidEmail = Messages.InvalidEmail
    }

    private lateinit var auth: FirebaseAuth

    private val loginSuccessfulMutableLiveData = MutableLiveData<Boolean>()
    val loginSuccessfulLiveData = loginSuccessfulMutableLiveData

    private val messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData = messageMutableLiveData

    private val setUpProgressbarVisibilityMutableLiveData = MutableLiveData<Boolean>()
    val setUpProgressbarVisibility = setUpProgressbarVisibilityMutableLiveData

    private val loginAutomaticallyIfPossibleMutableLiveData = MutableLiveData<Boolean>()
    val loginAutomaticallyIfPossibleLiveData = loginAutomaticallyIfPossibleMutableLiveData

    private val errorMessageMutableLiveData = MutableLiveData<String>()
    val errorMessageLiveData = errorMessageMutableLiveData


    fun validateInput(username: String, password: String) {
        val username = username.trim()
        val password = password.trim()
        when {
            username.isEmpty() || password.isEmpty() ->
                errorMessageLiveData.value = getMessage(emptyField)

            Patterns.EMAIL_ADDRESS.matcher(username).matches()
                .not() -> errorMessageLiveData.value = getMessage(invalidEmail)

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
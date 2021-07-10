package com.amadev.rando.ui.fragments.signup

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

private lateinit var auth: FirebaseAuth

class SignUpViewModel : ViewModel() {


    private val usernameInputErrorMutableLiveData = MutableLiveData<String>()
    val usernameInputErrorLiveData = usernameInputErrorMutableLiveData

    private val passwordInputErrorMutableLiveData = MutableLiveData<String>()
    val passwordInputErrorLiveData = passwordInputErrorMutableLiveData

    private val emptyRepeatPasswordInputMutableLiveData = MutableLiveData<String>()
    val emptyRepeatPasswordInputLiveData = emptyRepeatPasswordInputMutableLiveData

    private val invalidUsernameMutableLiveData = MutableLiveData<String>()
    val invalidUsernameLiveData = invalidUsernameMutableLiveData

    private val passwordsAreNotTheSameMutableLiveData = MutableLiveData<String>()
    val passwordsAreNotTheSameLiveData = passwordsAreNotTheSameMutableLiveData

    private val toastTextMutableLiveData = MutableLiveData<String>()
    val toastTextLiveData = toastTextMutableLiveData

    private val progressBarVisibleMutableLiveData = MutableLiveData<Boolean>()
    val progressBarVisibleLiveData = progressBarVisibleMutableLiveData

    private val accountSuccessfullyCreatedMutableLiveData = MutableLiveData<Boolean>()
    val accountSuccessfullyCreatedLiveData = accountSuccessfullyCreatedMutableLiveData


    fun validateInput(username: String, password: String, repeatPassword: String) {
        val username = username.trim()
        val password = password.trim()
        val repeatPassword = repeatPassword.trim()

        when {
            username.isEmpty() -> usernameInputErrorMutableLiveData.value = "Field cannot be empty"
            password.isEmpty() -> passwordInputErrorLiveData.value = "Field cannot be empty"
            repeatPassword.isEmpty() -> emptyRepeatPasswordInputLiveData.value =
                "Field cannot be empty"
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
                .not() -> invalidUsernameMutableLiveData.value = "Please enter valid email adress"
            password != repeatPassword -> passwordsAreNotTheSameMutableLiveData.value =
                "Passwords are not the same"
            else -> createUserWithEmailAndPassword(username, password)
        }
    }

    fun createUserWithEmailAndPassword(username: String, password: String) {
        progressBarVisibleMutableLiveData.value = true
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                sendVerificationEmail()
                progressBarVisibleMutableLiveData.value = false
                toastTextMutableLiveData.value = "User created. Please verify email send"
                accountSuccessfullyCreatedMutableLiveData.value = true
            } else if (it.isSuccessful.not()) {
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    passwordInputErrorMutableLiveData.value = "Password is too weak"
                } catch (e: FirebaseAuthUserCollisionException) {
                    usernameInputErrorMutableLiveData.value = "Email already exists"
                } catch (e: Exception) {
                    toastTextMutableLiveData.value = "Failed to sign up"
                }
                progressBarVisibleMutableLiveData.value = false
            }
        }
    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.sendEmailVerification()
    }
}
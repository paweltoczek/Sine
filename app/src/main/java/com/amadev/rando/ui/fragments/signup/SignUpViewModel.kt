package com.amadev.rando.ui.fragments.signup

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.R
import com.google.firebase.auth.FirebaseAuth

sealed class Messages {
    object EmptyField : Messages()
    object InvalidEmail : Messages()
    object VerifyEmailSent : Messages()
    object SignInFailed : Messages()
    object PasswordsNotSame : Messages()
    object UserCreated : Messages()
}

class SignUpViewModel(private val context: Context, private val auth: FirebaseAuth) : ViewModel() {

    companion object {
        val emptyField = Messages.EmptyField
        val invalidEmail = Messages.InvalidEmail
        val verifyEmailSent = Messages.VerifyEmailSent
        val signInFailed = Messages.SignInFailed
        val passwordNotSame = Messages.PasswordsNotSame
        val userCreated = Messages.UserCreated
    }

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
        username.trim()
        password.trim()
        repeatPassword.trim()

        when {
            username.isEmpty() -> usernameInputErrorMutableLiveData.value = getMessage(emptyField)
            password.isEmpty() -> passwordInputErrorLiveData.value = getMessage(emptyField)
            repeatPassword.isEmpty() -> emptyRepeatPasswordInputLiveData.value =
                getMessage(emptyField)
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
                .not() -> invalidUsernameMutableLiveData.value = getMessage(invalidEmail)
            password != repeatPassword -> passwordsAreNotTheSameMutableLiveData.value =
                getMessage(passwordNotSame)
            else -> createUserWithEmailAndPassword(username, password)
        }
    }

    private fun createUserWithEmailAndPassword(username: String, password: String) {
        progressBarVisibleMutableLiveData.value = true
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                sendVerificationEmail()
                progressBarVisibleMutableLiveData.value = false
                toastTextMutableLiveData.value = getMessage(userCreated)
                accountSuccessfullyCreatedMutableLiveData.value = true
            } else if (it.isSuccessful.not()) {
                /*try {
                    throw it.exception!!
                } catch (e: FirebaseAuthWeakPasswordException) {
                    passwordInputErrorMutableLiveData.value = "Password is too weak"
                } catch (e: FirebaseAuthUserCollisionException) {
                    usernameInputErrorMutableLiveData.value = "Email already exists"
                } catch (e: Exception) {
                    toastTextMutableLiveData.value = "Failed to sign up"
                }
                progressBarVisibleMutableLiveData.value = false*/
            }
        }
    }

    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.sendEmailVerification()
    }

    private fun getMessage(message: Messages) =
        when (message) {
            is Messages.EmptyField -> context.getString(R.string.field_cannot_be_empty)
            is Messages.InvalidEmail -> context.getString(R.string.pleaseEnterValidEmailAdress)
            is Messages.VerifyEmailSent -> context.getString(R.string.verificationEmailSent)
            is Messages.SignInFailed -> context.getString(R.string.failedToLogIn)
            is Messages.PasswordsNotSame -> context.getString(R.string.passwordsAreNotSame)
            is Messages.UserCreated -> context.getString(R.string.userCreatedSuccessfully)
        }
}
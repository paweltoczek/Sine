package com.amadev.rando.ui.dialogs.forgotPassword

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.R
import com.google.firebase.auth.FirebaseAuth

sealed class Messages {
    object EmptyField : Messages()
    object InvalidInput : Messages()
    object EmailSent : Messages()
    object EmailSentFailed : Messages()
}

class ForgotPasswordDialogViewModel(val context: Context, private val auth: FirebaseAuth) :
    ViewModel() {

    companion object {
        val emptyField = Messages.EmptyField
        val invalidInput = Messages.InvalidInput
        val emailSent = Messages.EmailSent
        val emailSentFailed = Messages.EmailSentFailed
    }

    private val popUpMessageMutableLiveData = MutableLiveData<String>()
    val popUpMessageLiveData = popUpMessageMutableLiveData

    val dialogDismissMutableLiveData = MutableLiveData<Boolean>()

    fun validateInput(username: String) {
        when {
            username.trim().isEmpty() -> {
                popUpMessageMutableLiveData.value = getMessage(emptyField)
            }
            Patterns.EMAIL_ADDRESS.matcher(username).matches().not() -> {
                popUpMessageMutableLiveData.value = getMessage(invalidInput)
            }
            else -> {
                sendResetPasswordEmail(username)
            }
        }
    }

    private fun sendResetPasswordEmail(username: String) {
        auth.sendPasswordResetEmail(username)
            .addOnSuccessListener {
                popUpMessageMutableLiveData.value = getMessage(emailSent)
                dialogDismissMutableLiveData.value = true
            }
            .addOnFailureListener {
                Log.e("failed", it.toString())
                popUpMessageMutableLiveData.value = getMessage(emailSentFailed)
                dialogDismissMutableLiveData.value = false
            }
    }

    private fun getMessage(message: Messages) =
        when (message) {
            is Messages.EmptyField -> context.getString(R.string.field_cannot_be_empty)
            is Messages.InvalidInput -> context.getString(R.string.pleaseEnterValidEmailAdress)
            is Messages.EmailSent -> context.getString(R.string.emailSent)
            is Messages.EmailSentFailed -> context.getString(R.string.failedToSendAnEmail)
        }

}
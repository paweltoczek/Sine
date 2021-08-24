package com.amadev.rando.ui.dialogs.logout

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amadev.rando.R
import com.google.firebase.auth.FirebaseAuth

sealed class Messages {
    object LoggedOut : Messages()
}

class LogoutDialogViewModel(private val auth: FirebaseAuth, private val context: Context) :
    ViewModel() {

    private val popUpMessageMutableLiveData = MutableLiveData<String>()
    val popUpMessageLiveData = popUpMessageMutableLiveData

    companion object {
        val loggedOut = Messages.LoggedOut
    }

    fun logOut() {
        popUpMessageMutableLiveData.value = getMessage(loggedOut)
        auth.signOut()
    }

    private fun getMessage(messages: Messages) =
        when (messages) {
            is Messages.LoggedOut -> context.getString(R.string.youLoggedOut)
        }
}
package com.amadev.rando.ui.fragments.signinorsignup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInOrSignUpViewModel : ViewModel() {

    private lateinit var auth: FirebaseAuth

    private val loginAutomaticallyIfPossibleMutableLiveData = MutableLiveData<Boolean>()
    val loginAutomaticallyIfPossibleLiveData = loginAutomaticallyIfPossibleMutableLiveData

    private val messageMutableLiveData = MutableLiveData<String>()
    val messageLiveData = messageMutableLiveData

    fun loginAutomaticallyIfPossible() {
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            when {
                currentUser.isEmailVerified -> {
                    loginAutomaticallyIfPossibleMutableLiveData.value = true
                }
                currentUser.isEmailVerified.not() -> {
                    loginAutomaticallyIfPossibleMutableLiveData.value = false
                    messageMutableLiveData.value = "Please verify email sent"
                }
                else -> {
                    loginAutomaticallyIfPossibleMutableLiveData.value = false
                }
            }
        }
    }
}
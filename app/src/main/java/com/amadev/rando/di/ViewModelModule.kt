package com.amadev.rando.di

import com.amadev.rando.data.ApiClient
import com.amadev.rando.ui.fragments.choice.ChoiceFragmentViewModel
import com.amadev.rando.ui.fragments.signin.SignInViewModel
import com.amadev.rando.ui.fragments.signinorsignup.SignInOrSignUpViewModel
import com.amadev.rando.ui.fragments.signup.SignUpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ChoiceFragmentViewModel(provideApiClient()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel() }
    viewModel { SignInOrSignUpViewModel() }
}

fun provideApiClient() : ApiClient = ApiClient

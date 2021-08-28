package com.amadev.rando.di

import com.amadev.rando.ui.dialogs.castDetails.CastDetailsViewModel
import com.amadev.rando.ui.dialogs.forgotPassword.ForgotPasswordDialogViewModel
import com.amadev.rando.ui.dialogs.logout.LogoutDialogViewModel
import com.amadev.rando.ui.dialogs.movieDetails.MovieDetailsDialogViewModel
import com.amadev.rando.ui.fragments.choice.ChoiceFragmentViewModel
import com.amadev.rando.ui.fragments.favorites.FavoritesFragmentViewModel
import com.amadev.rando.ui.fragments.mainFragment.MainFragmentViewModel
import com.amadev.rando.ui.fragments.signin.SignInViewModel
import com.amadev.rando.ui.fragments.signinorsignup.SignInOrSignUpViewModel
import com.amadev.rando.ui.fragments.signup.SignUpViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainFragmentViewModel(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        ChoiceFragmentViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }

    viewModel {
        FavoritesFragmentViewModel(
            get(),
            get()
        )
    }

    viewModel { SignUpViewModel(get(), get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { SignInOrSignUpViewModel() }
    viewModel { ForgotPasswordDialogViewModel(get(), get()) }
    viewModel { CastDetailsViewModel() }
    viewModel { LogoutDialogViewModel(get(), get()) }
    viewModel { MovieDetailsDialogViewModel(get()) }
}






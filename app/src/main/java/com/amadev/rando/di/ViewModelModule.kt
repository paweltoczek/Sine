package com.amadev.rando.data

import com.amadev.rando.BuildConfig
import com.amadev.rando.ui.fragments.choice.ChoiceFragment
import com.amadev.rando.ui.fragments.choice.ChoiceFragmentViewModel
import com.amadev.rando.ui.fragments.signup.SignUpViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { ChoiceFragmentViewModel(provideApiClient()) }
    viewModel { SignUpViewModel(get()) }
}

fun provideApiClient() : ApiClient = ApiClient

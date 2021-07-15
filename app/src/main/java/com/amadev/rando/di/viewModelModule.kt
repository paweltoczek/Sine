package com.amadev.rando.data

import com.amadev.rando.BuildConfig
import com.amadev.rando.ui.fragments.choice.ChoiceFragment
import com.amadev.rando.ui.fragments.choice.ChoiceFragmentViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { ChoiceFragmentViewModel(provideApiClient()) }
}

fun provideApiClient() : ApiClient = ApiClient

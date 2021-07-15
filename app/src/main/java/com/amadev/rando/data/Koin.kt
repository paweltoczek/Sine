package com.amadev.rando.data

import com.amadev.rando.BuildConfig
import com.amadev.rando.ui.fragments.choice.ChoiceFragment
import com.amadev.rando.ui.fragments.choice.ChoiceFragmentViewModel
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideRetrofit(get()) }
    single { provideTmdbApi(get()) }
    single { PopularMoviesService(get()) }
    factory { ChoiceFragment() }
    viewModel { ChoiceFragmentViewModel(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.TMDB_BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideTmdbApi(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)
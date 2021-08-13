package com.amadev.rando.di

import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val firebaseModule = module {
    single { provideFirebaseAuth() }
}

fun provideFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}



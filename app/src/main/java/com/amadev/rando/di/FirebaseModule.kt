package com.amadev.rando.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val firebaseModule = module {
    factory { provideFirebaseAuth() }
    factory { provideFirebaseDatabase() }
}

fun provideFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}

fun provideFirebaseDatabase() : FirebaseDatabase {
    return FirebaseDatabase.getInstance()
}
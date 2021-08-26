package com.amadev.rando.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.dsl.module

val firebaseModule = module {
    single { provideFirebaseAuth() }
}

fun provideFirebaseAuth(): FirebaseAuth {
    return FirebaseAuth.getInstance()
}

fun provideFirebaseDatabase() : FirebaseDatabase {
    return FirebaseDatabase.getInstance()
}

fun provideFirebaseUsername(): String {
    val currentUser = FirebaseAuth.getInstance().currentUser
    lateinit var username: String
    currentUser?.let {
        for (profiler in it.providerData) {
            username = profiler.email.toString()
        }
    }
    return username
}






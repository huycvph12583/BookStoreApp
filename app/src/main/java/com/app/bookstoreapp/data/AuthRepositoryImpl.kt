package com.app.bookstoreapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepositoryImpl(val firebaseAuth: FirebaseAuth)  {

//    override val currentUser: FirebaseUser?
//        get() = firebaseAuth.currentUser
//
//    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
//       val result =  firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
//        }
//        return  result
//    }
//
//    override suspend fun signup(
//        name: String,
//        email: String,
//        password: String
//    ): Resource<FirebaseUser> {
//        TODO("Not yet implemented")
//    }
//
//    override fun logout() {
//        TODO("Not yet implemented")
//    }
}
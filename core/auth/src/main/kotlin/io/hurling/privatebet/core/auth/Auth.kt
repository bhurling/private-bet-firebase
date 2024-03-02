package io.hurling.privatebet.core.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

interface Auth {
    val currentUserId: String?
}

@Singleton
class DefaultAuth @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Auth {
    override val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid
}

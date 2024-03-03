package io.hurling.privatebet.core.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface Auth {
    val currentUserId: String?

    val authState: Flow<AuthState>
}

@Singleton
class DefaultAuth @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : Auth {
    override val currentUserId: String?
        get() = firebaseAuth.uid
    override val authState: Flow<AuthState>
        get() = firebaseAuth.authState
}

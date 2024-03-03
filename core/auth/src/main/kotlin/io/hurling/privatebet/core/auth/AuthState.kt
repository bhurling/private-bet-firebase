package io.hurling.privatebet.core.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

internal val FirebaseAuth.authState: Flow<AuthState>
    get() = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val state = auth.uid?.let { uid ->
                AuthState.Authenticated(uid)
            } ?: AuthState.NotAuthenticated

            launch { send(state) }
        }

        addAuthStateListener(listener)

        awaitClose {
            removeAuthStateListener(listener)
        }
    }

internal val FirebaseAuth.authUser: Flow<AuthUser?>
    get() = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.let { currentUser ->
                AuthUser(
                    uid = currentUser.uid,
                    displayName = currentUser.displayName,
                    photoUrl = currentUser.photoUrl?.toString()
                )
            }

            launch { send(user) }
        }

        addAuthStateListener(listener)

        awaitClose {
            removeAuthStateListener(listener)
        }
    }

sealed interface AuthState {
    data object Unknown : AuthState
    data object NotAuthenticated : AuthState
    data class Authenticated(val uid: String) : AuthState
}

data class AuthUser(
    val uid: String,
    val displayName: String?,
    val photoUrl: String?
)

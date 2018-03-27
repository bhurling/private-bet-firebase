package io.bhurling.privatebet.signup

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val signupKoinModule = applicationContext {
    factory {
        SignupInteractor(
                privateProfile = get(Qualifiers.Me.private),
                publicProfile = get(Qualifiers.Me.public)
        )
    }
}
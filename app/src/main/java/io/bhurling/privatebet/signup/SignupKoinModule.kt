package io.bhurling.privatebet.signup

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.module

val signupKoinModule = module {
    factory {
        SignupInteractor(
                privateProfile = get(Qualifiers.Me.private),
                publicProfile = get(Qualifiers.Me.public)
        )
    }
}
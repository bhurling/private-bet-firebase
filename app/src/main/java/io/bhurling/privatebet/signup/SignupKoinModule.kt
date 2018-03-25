package io.bhurling.privatebet.signup

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val signupKoinModule = applicationContext {
    factory {
        SignupInteractor(
                profile = get(Qualifiers.me)
        )
    }
}
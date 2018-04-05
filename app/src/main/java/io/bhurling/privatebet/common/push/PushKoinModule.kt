package io.bhurling.privatebet.common.push

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.applicationContext

val pushKoinModule = applicationContext {
    factory {
        TokenInteractor(
                devices = get(Qualifiers.devices)
        )
    }
}
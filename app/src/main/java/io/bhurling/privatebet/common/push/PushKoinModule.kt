package io.bhurling.privatebet.common.push

import io.bhurling.privatebet.Qualifiers
import org.koin.dsl.module.module

val pushKoinModule = module {
    factory {
        TokenInteractor(
                devices = get(Qualifiers.devices)
        )
    }
}
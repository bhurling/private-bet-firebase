package io.bhurling.privatebet.arch

import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

fun <T : Any, R : Any> Observable<T>.mapNotNull(converter: (T) -> R?) : Observable<R> {
    return map { converter(it).toOptional() }.filterIsSome()
}

fun <T : Any> Observable<Optional<T>>.filterIsSome() : Observable<T> {
    return ofType<Optional.Some<T>>().map { it.value }
}
package io.bhurling.privatebet.common.firestore

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T> Query.snapshots(): Flow<List<T>> =
    snapshots().map { it.toObjects(T::class.java) }
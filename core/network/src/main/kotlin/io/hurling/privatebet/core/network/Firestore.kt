package io.hurling.privatebet.core.network

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T> Query.snapshots(): Flow<List<T>> =
    snapshots().map { it.toObjects(T::class.java) }

inline fun <reified T> DocumentReference.snapshots(): Flow<T?> =
    snapshots().map { it.toObject(T::class.java) }
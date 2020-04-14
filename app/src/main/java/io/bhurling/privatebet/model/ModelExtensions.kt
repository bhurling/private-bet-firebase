package io.bhurling.privatebet.model

import com.google.firebase.firestore.DocumentSnapshot
import io.bhurling.privatebet.model.pojo.Person

// TODO can we use toObject()?
fun DocumentSnapshot.toPerson() = Person(
    id = id,
    displayName = getStringOrThrow("displayName"),
    photoUrl = getStringOrThrow("photoUrl")
)

fun DocumentSnapshot.getStringOrThrow(field: String) =
    getString(field) ?: throw ConversionException("No field \"$field\" in DocumentSnapshot with id $id.")

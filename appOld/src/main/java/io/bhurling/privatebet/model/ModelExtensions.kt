package io.bhurling.privatebet.model

import com.google.firebase.firestore.DocumentSnapshot
import io.bhurling.privatebet.model.pojo.Profile

// TODO can we use toObject()?
fun DocumentSnapshot.toPerson() = Profile(
    id = id,
    displayName = getStringOrThrow("displayName"),
    photoUrl = getString("photoUrl")
)

fun DocumentSnapshot.getStringOrThrow(field: String) =
    getString(field) ?: throw ConversionException("No field \"$field\" in DocumentSnapshot with id $id.")


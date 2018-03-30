package io.bhurling.privatebet.model

import com.google.firebase.database.DataSnapshot
import io.bhurling.privatebet.model.pojo.Person

fun DataSnapshot.toPerson() = Person(
        id = key,
        displayName = child("displayName").value?.toString() ?: "",
        photoUrl = child("photoUrl").value?.toString() ?: ""
)
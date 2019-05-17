package io.bhurling.privatebet.model

import com.google.firebase.firestore.DocumentSnapshot
import io.bhurling.privatebet.model.pojo.Person

// TODO can we use toObject()?
fun DocumentSnapshot.toPerson() = Person(
        id = id,
        displayName = getString("displayName") ?: "",
        photoUrl = getString("photoUrl") ?: ""
)
package io.bhurling.privatebet.feed

import androidx.annotation.Keep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import io.bhurling.privatebet.common.firestore.snapshots
import io.bhurling.privatebet.model.pojo.Bet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BetsRepository @Inject constructor(
    private val store: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private val feed by lazy {
        store.collection("feeds")
            .document(auth.currentUser?.uid ?: "")
            .collection("bets")
    }

    fun keys(): Flow<List<String>> {
        return feed.snapshots<FirestoreFeedEntry>()
            .map { entries -> entries.map(FirestoreFeedEntry::documentId) }
            .distinctUntilChanged()
    }

    fun byId(id: String): Flow<Bet> {
        return store.collection("bets").document(id)
            .snapshots<FirestoreBet>()
            .filterNotNull()
            .map(FirestoreBet::toBet)
            .distinctUntilChanged()
    }
}

@Keep
data class FirestoreFeedEntry(
    @DocumentId val documentId: String = "",
)

@Keep
data class FirestoreBet(
    @DocumentId val documentId: String = "",
    val statement: String = ""
)

fun FirestoreBet.toBet() = Bet(
    statement = statement
)

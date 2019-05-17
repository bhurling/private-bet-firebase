package io.bhurling.privatebet.rx

import com.google.firebase.firestore.*
import io.reactivex.Observable

class ReactiveFirebase {

    class RxFirebaseDatabaseException(val error: Exception) : Exception()

    fun observeValueEvents(reference: DocumentReference): Observable<DocumentSnapshot> {
        return Observable.create { emitter ->
            val listener = reference
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> {
                            emitter.onError(RxFirebaseDatabaseException(exception))
                        }
                        snapshot != null -> {
                            emitter.onNext(snapshot)
                        }
                    }
                }

            emitter.setCancellable { listener.remove() }
        }
    }

    fun observeValueEvents(query: Query): Observable<QuerySnapshot> {
        return Observable.create { emitter ->
            val listener = query
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> {
                            emitter.onError(RxFirebaseDatabaseException(exception))
                        }
                        snapshot != null -> {
                            emitter.onNext(snapshot)
                        }
                    }
                }

            emitter.setCancellable { listener.remove() }
        }
    }

}

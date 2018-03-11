package io.bhurling.privatebet.rx

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable

class ReactiveFirebase {

    class RxFirebaseDatabaseException(val error: DatabaseError) : Exception()

    fun observeValueEvents(query: Query): Observable<DataSnapshot> {
        return Observable.create { emitter ->
            val listener = query.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    emitter.onNext(dataSnapshot)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    emitter.onError(RxFirebaseDatabaseException(databaseError))
                }
            })

            emitter.setCancellable { query.removeEventListener(listener) }
        }
    }

}

package io.bhurling.privatebet.rx;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.Observable;

public class RxFirebaseDatabase {

    @SuppressWarnings("WeakerAccess")
    public static class RxFirebaseDatabaseException extends Exception {

        private final DatabaseError error;

        public RxFirebaseDatabaseException(DatabaseError error) {
            this.error = error;
        }

        @SuppressWarnings("unused")
        public DatabaseError getError() {
            return error;
        }
    }

    public static Observable<DataSnapshot> observeValueEvents(Query query) {
        return Observable.create(emitter -> {
            ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    emitter.onNext(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    emitter.onError(new RxFirebaseDatabaseException(databaseError));
                }
            });

            emitter.setCancellable(() -> query.removeEventListener(listener));
        });
    }

}
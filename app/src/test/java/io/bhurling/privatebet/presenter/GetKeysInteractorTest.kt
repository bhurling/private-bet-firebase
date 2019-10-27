package io.bhurling.privatebet.presenter

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.bhurling.privatebet.feed.GetKeysInteractor
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class GetKeysInteractorTest {

    @Mock
    lateinit var mockFirebase: ReactiveFirebase

    @Mock
    lateinit var mockFeed: CollectionReference

    @InjectMocks
    lateinit var interactor: GetKeysInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val snapshot = emptyFeed()

        whenever(mockFirebase.observeValueEvents(any<Query>()))
            .thenReturn(Observable.just(snapshot))
    }

    @Test
    fun whenFetchingTheFeed_weGetKeysInReversedOrder() {
        val snapshot = fullFeed()

        whenever(mockFirebase.observeValueEvents(any<Query>()))
            .thenReturn(Observable.just(snapshot))

        val testObserver = interactor.getKeys().test()

        testObserver.assertValue { keys ->
            keys == listOf("key3", "key2", "key1")
        }
    }

    private fun emptyFeed(): QuerySnapshot {
        val feedSnapshot = mock<QuerySnapshot>()
        whenever(feedSnapshot.documents).thenReturn(emptyList())

        return feedSnapshot
    }

    private fun fullFeed(): QuerySnapshot {
        val feedItem1 = mock<DocumentSnapshot>()
        whenever(feedItem1.id).thenReturn("key1")
        val feedItem2 = mock<DocumentSnapshot>()
        whenever(feedItem2.id).thenReturn("key2")
        val feedItem3 = mock<DocumentSnapshot>()
        whenever(feedItem3.id).thenReturn("key3")

        val feedSnapshot = mock<QuerySnapshot>()
        whenever(feedSnapshot.documents).thenReturn(
            listOf(
                feedItem1,
                feedItem2,
                feedItem3
            )
        )

        return feedSnapshot
    }
}
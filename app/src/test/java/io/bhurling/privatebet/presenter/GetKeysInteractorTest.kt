package io.bhurling.privatebet.presenter

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.bhurling.privatebet.feed.GetKeysInteractor
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

internal class GetKeysInteractorTest {

    @Mock
    lateinit var mockFirebase: ReactiveFirebase

    @Mock
    lateinit var mockFeed: DatabaseReference

    @InjectMocks
    lateinit var interactor: GetKeysInteractor

    private val keysCaptor = argumentCaptor<List<String>>()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val snapshot = emptyFeed()

        whenever(mockFirebase.observeValueEvents(any()))
             .thenReturn(Observable.just(snapshot))
        whenever(mockFeed.orderByValue())
            .thenReturn(mock())
    }

    @Test
    fun whenFetchingTheFeed_weOrderByValue() {
        interactor.getKeys().test()

        verify<DatabaseReference>(mockFeed, times(1)).orderByValue()
    }

    @Test
    fun whenFetchingTheFeed_weGetKeysInReversedOrder() {
        val snapshot = fullFeed()

        whenever(mockFirebase.observeValueEvents(any()))
            .thenReturn(Observable.just(snapshot))

        val testObserver = interactor.getKeys().test()

        testObserver.assertValue { keys ->
            keys == listOf("key3", "key2", "key1")
        }
    }

    private fun emptyFeed(): DataSnapshot {
        val feedSnapshot = mock<DataSnapshot>()
        whenever(feedSnapshot.children).thenReturn(emptyList())

        return feedSnapshot
    }

    private fun fullFeed(): DataSnapshot {
        val feedItem1 = mock<DataSnapshot>()
        whenever(feedItem1.key).thenReturn("key1")
        val feedItem2 = mock<DataSnapshot>()
        whenever(feedItem2.key).thenReturn("key2")
        val feedItem3 = mock<DataSnapshot>()
        whenever(feedItem3.key).thenReturn("key3")

        val feedSnapshot = mock<DataSnapshot>()
        whenever(feedSnapshot.children).thenReturn(Arrays.asList(
                feedItem1,
                feedItem2,
                feedItem3
        ))

        return feedSnapshot
    }
}
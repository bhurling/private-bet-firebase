package io.bhurling.privatebet.presenter

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.bhurling.privatebet.feed.FeedPresenter
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class FeedPresenterTest {

    @Mock
    lateinit var mockFirebase: ReactiveFirebase

    @Mock
    lateinit var mockFeed: DatabaseReference

    @Mock
    lateinit var mockView: FeedPresenter.View

    @InjectMocks
    lateinit var presenter: FeedPresenter

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
        presenter.attachView(mockView)

        verify<DatabaseReference>(mockFeed, times(1)).orderByValue()
    }

    @Test
    fun whenFetchingTheFeed_weGetKeysInReversedOrder() {
        val snapshot = fullFeed()

        whenever(mockFirebase.observeValueEvents(any()))
            .thenReturn(Observable.just(snapshot))

        presenter.attachView(mockView)

        verify(mockView, times(1)).updateKeys(keysCaptor.capture())

        with(keysCaptor.firstValue) {
            assertThat(get(0), equalTo("key3"))
            assertThat(get(1), equalTo("key2"))
            assertThat(get(2), equalTo("key1"))
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
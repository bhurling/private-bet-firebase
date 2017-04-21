package io.bhurling.privatebet.presenter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.bhurling.privatebet.rx.ReactiveFirebase;
import io.reactivex.Observable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FeedPresenterTest {

    @Mock
    ReactiveFirebase mockFirebase;

    @Mock
    DatabaseReference mockFeed;

    @Mock
    FeedPresenter.View mockView;

    @Captor
    ArgumentCaptor<List<String>> keysCaptor;

    @InjectMocks
    FeedPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        DataSnapshot snapshot = emptyFeed();
        when(mockFirebase.observeValueEvents(any(Query.class)))
                .thenReturn(Observable.just(snapshot));
    }

    @Test
    public void whenFetchingTheFeed_weOrderByValue() {
        presenter.attachView(mockView);

        verify(mockFeed, times(1)).orderByValue();
    }

    @Test
    public void whenFetchingTheFeed_weGetKeysInReversedOrder() {
        DataSnapshot snapshot = fullFeed();
        when(mockFirebase.observeValueEvents(any(Query.class)))
                .thenReturn(Observable.just(snapshot));

        presenter.attachView(mockView);

        verify(mockView, times(1)).updateKeys(keysCaptor.capture());

        assertThat(keysCaptor.getValue().get(0), equalTo("key3"));
        assertThat(keysCaptor.getValue().get(1), equalTo("key2"));
        assertThat(keysCaptor.getValue().get(2), equalTo("key1"));
    }

    private DataSnapshot emptyFeed() {
        DataSnapshot feedSnapshot = mock(DataSnapshot.class);
        when(feedSnapshot.getChildren()).thenReturn(Collections.emptyList());

        return feedSnapshot;
    }

    private DataSnapshot fullFeed() {
        DataSnapshot feedItem1 = mock(DataSnapshot.class);
        when(feedItem1.getKey()).thenReturn("key1");
        DataSnapshot feedItem2 = mock(DataSnapshot.class);
        when(feedItem2.getKey()).thenReturn("key2");
        DataSnapshot feedItem3 = mock(DataSnapshot.class);
        when(feedItem3.getKey()).thenReturn("key3");

        DataSnapshot feedSnapshot = mock(DataSnapshot.class);
        when(feedSnapshot.getChildren()).thenReturn(Arrays.asList(
                feedItem1,
                feedItem2,
                feedItem3
        ));

        return feedSnapshot;
    }
}
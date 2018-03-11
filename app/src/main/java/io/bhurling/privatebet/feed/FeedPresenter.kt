package io.bhurling.privatebet.feed

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable
import java.util.*

class FeedPresenter(
        private val firebase: ReactiveFirebase,
        private val feed: DatabaseReference
) : Presenter<FeedPresenter.View>() {

    override fun attachView(view: View) {
        super.attachView(view)

        disposables.addAll(
                firebase.observeValueEvents(this.feed.orderByValue())
                        .map { it.children }
                        .map { this.keys(it) }
                        .map { this.reverse(it) }
                        .subscribe { this.handleData(it) }
        )
    }

    private fun handleData(keys: List<String>) {
        view?.updateKeys(keys)
    }

    private fun keys(data: Iterable<DataSnapshot>): List<String> {
        return Observable.fromIterable(data)
                .map { it.getKey() }
                .toList()
                .blockingGet()
    }

    private fun reverse(list: List<String>): List<String> {
        Collections.reverse(list)

        return list
    }

    interface View : Presenter.View {
        fun updateKeys(keys: List<String>)
    }
}

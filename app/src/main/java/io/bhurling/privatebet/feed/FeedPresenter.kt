package io.bhurling.privatebet.feed

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import io.bhurling.privatebet.arch.Presenter
import io.bhurling.privatebet.rx.ReactiveFirebase
import io.reactivex.Observable

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
                        .map { it.reversed() }
                        .subscribe { this.handleData(it) }
        )
    }

    private fun handleData(keys: List<String>) {
        view.updateKeys(keys)
    }

    private fun keys(data: Iterable<DataSnapshot>): List<String> {
        return data.mapNotNull { it.key }
    }

    interface View : Presenter.View {
        fun updateKeys(keys: List<String>)
    }
}

package io.bhurling.privatebet.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.bhurling.privatebet.R
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotterknife.bindView
import org.koin.inject

internal class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by inject()
    private val adapter: FeedAdapter by inject()

    private val feed: RecyclerView by bindView(R.id.feed)

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feed.adapter = adapter
        feed.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.transparent_divider)!!)
        feed.addItemDecoration(decoration)

        viewModel.attach(Observable.never())

        disposables += viewModel.stateOf { keys }
            .subscribe { keys -> adapter.keys = keys }
    }

    override fun onDestroyView() {
        viewModel.detach()

        disposables.clear()

        // we need to unregister the adapter to make sure we call onViewDetachedFromWindow(ViewHolder)
        // for the visible items.
        feed.swapAdapter(null, true)

        super.onDestroyView()
    }
}
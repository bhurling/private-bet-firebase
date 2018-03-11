package io.bhurling.privatebet.feed

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.bhurling.privatebet.R
import io.bhurling.privatebet.add.AddBetActivity
import kotterknife.bindView
import org.koin.inject

class FeedActivity : AppCompatActivity(), FeedPresenter.View {

    private val presenter: FeedPresenter by inject()
    private val adapter: FeedAdapter by inject()

    private val feed: RecyclerView by bindView(R.id.feed)
    private val fab: View by bindView(R.id.fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        feed.adapter = adapter
        feed.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.transparent_divider)!!)
        feed.addItemDecoration(decoration)

        fab.setOnClickListener {
            startActivity(Intent(this, AddBetActivity::class.java))
        }

        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        // we need to unregister the adapter to make sure we call onViewDetachedFromWindow(ViewHolder)
        // for the visible items.
        feed.swapAdapter(null, true)

        presenter.detachView()
    }

    override fun updateKeys(keys: List<String>) {
        adapter.keys = keys
    }
}

package io.bhurling.privatebet.friends

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.bhurling.privatebet.R
import io.reactivex.Observable
import kotterknife.bindView
import org.koin.inject

internal class InviteActivity: AppCompatActivity(), InvitePresenter.View {

    private val presenter: InvitePresenter by inject()

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val list: RecyclerView by bindView(R.id.invite_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        list.adapter = InviteAdapter()

        presenter.attachView(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        presenter.detachView()

        super.onDestroy()
    }

    override fun actions(): Observable<InviteAction> = (list.adapter as InviteAdapter).actions()

    override fun updateItems(items: List<InviteAdapterItem>) {
        (list.adapter as InviteAdapter).items = items
    }
}

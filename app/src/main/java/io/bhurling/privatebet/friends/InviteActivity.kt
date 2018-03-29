package io.bhurling.privatebet.friends

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.bhurling.privatebet.R
import io.reactivex.Observable
import kotterknife.bindView
import org.koin.inject

internal class InviteActivity: AppCompatActivity(), InvitePresenter.View {

    private val presenter: InvitePresenter by inject()

    private val list: RecyclerView by bindView(R.id.invite_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        list.adapter = InviteAdapter()

        presenter.attachView(this)
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

package io.bhurling.privatebet.friends

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.bhurling.privatebet.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_invite.*
import org.koin.android.viewmodel.ext.android.viewModel

internal class InviteActivity: AppCompatActivity(R.layout.activity_invite) {

    private val viewModel: InviteViewModel by viewModel()

    private val adapter = InviteAdapter()

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        invite_list.layoutManager = LinearLayoutManager(this)
        invite_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        invite_list.adapter = adapter

        viewModel.attach(adapter.actions())

        disposables += viewModel.stateOf { items }
                .subscribe { items ->
                    adapter.items = items
                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModel.detach()

        disposables.dispose()

        super.onDestroy()
    }
}

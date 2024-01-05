package io.bhurling.privatebet.friends

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.databinding.ActivityInviteBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class InviteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBinding

    private val viewModel: InviteViewModel by viewModels()

    @Inject
    lateinit var adapter: InviteAdapter

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInviteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.inviteList.layoutManager = LinearLayoutManager(this)
        binding.inviteList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.inviteList.adapter = adapter

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .collect { state ->
                    adapter.items = state.items
                }
        }

        disposables += adapter.actions()
            .ofType<InviteAction.Invite>()
            .subscribe {
                viewModel.sendInvite(it.id)
            }

        disposables += adapter.actions()
            .ofType<InviteAction.Revoke>()
            .subscribe {
                viewModel.revokeInvite(it.id)
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
        disposables.dispose()

        super.onDestroy()
    }
}

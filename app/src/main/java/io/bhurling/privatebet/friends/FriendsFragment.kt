package io.bhurling.privatebet.friends

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.launch
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_friends.friends_connect
import kotlinx.android.synthetic.main.fragment_friends.friends_empty
import kotlinx.android.synthetic.main.fragment_friends.friends_list
import org.koin.android.viewmodel.ext.android.viewModel
import javax.inject.Inject

@AndroidEntryPoint
internal class FriendsFragment : Fragment(R.layout.fragment_friends) {

    private val viewModel: FriendsViewModel by viewModel()

    @Inject
    lateinit var adapter: FriendsAdapter

    private val disposables = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        friends_connect.setOnClickListener {
            activity?.let<Activity, Unit> { EntryPoint.Invite.launch(it) }
        }

        friends_list.layoutManager = LinearLayoutManager(activity)
        friends_list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        friends_list.adapter = adapter

        viewModel.attach(adapter.actions())

        disposables += viewModel.stateOf { items }
            .subscribe { items ->
                onItemsChanged(items)
            }
    }

    private fun onItemsChanged(items: List<FriendsAdapterItem>) {
        friends_empty.isVisible = items.isEmpty()

        adapter.items = items

        setHasOptionsMenu(items.isNotEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friends, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_invite) {
            activity?.let<Activity, Unit> { EntryPoint.Invite.launch(it) }

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        viewModel.detach()

        disposables.clear()

        super.onDestroyView()
    }
}
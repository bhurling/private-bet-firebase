package io.bhurling.privatebet.friends

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotterknife.bindView
import org.koin.inject

class FriendsFragment : Fragment() {

    private val viewModel: FriendsViewModel by inject()
    private val navigator: Navigator by inject()
    private val adapter: FriendsAdapter by inject()

    private val emptyView: View by bindView(R.id.friends_empty)
    private val list: RecyclerView by bindView(R.id.friends_list)
    private val connectButton: View by bindView(R.id.friends_connect)

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectButton.setOnClickListener { navigator.launchInviteFlow(activity) }

        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        list.adapter = adapter

        viewModel.attach(adapter.actions())

        disposables += viewModel.stateOf { items }
            .subscribe { items ->
                onItemsChanged(items)
            }
    }

    private fun onItemsChanged(items: List<FriendsAdapterItem>) {
        emptyView.isVisible = items.isEmpty()

        adapter.items = items

        setHasOptionsMenu(items.isNotEmpty())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friends, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_invite) {
            navigator.launchInviteFlow(activity)

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
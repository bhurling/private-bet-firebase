package io.bhurling.privatebet.friends

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import kotterknife.bindView
import org.koin.inject

class FriendsFragment : Fragment(), FriendsPresenter.View {

    private val presenter: FriendsPresenter by inject()
    private val navigator: Navigator by inject()
    private val adapter: FriendsAdapter by inject()

    private val emptyView: View by bindView(R.id.friends_empty)
    private val list: RecyclerView by bindView(R.id.friends_list)
    private val connectButton: View by bindView(R.id.friends_connect)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectButton.setOnClickListener { navigator.launchInviteFlow(activity) }

        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        list.adapter = adapter

        presenter.attachView(this)
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
        presenter.detachView()

        super.onDestroyView()
    }

    override fun showEmptyState() {
        emptyView.visibility = View.VISIBLE
        setHasOptionsMenu(false)
    }

    override fun showContent(ids: List<String>) {
        emptyView.visibility = View.GONE
        setHasOptionsMenu(true)

        adapter.items = ids
    }
}
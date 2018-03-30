package io.bhurling.privatebet.friends

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import kotterknife.bindView
import org.koin.inject

class FriendsFragment : Fragment(), FriendsPresenter.View {

    private val presenter: FriendsPresenter by inject()
    private val navigator: Navigator by inject()

    private val emptyView: View by bindView(R.id.friends_empty)
    private val connectButton: View by bindView(R.id.friends_connect)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.attachView(this)

        connectButton.setOnClickListener { navigator.launchInviteFlow(activity) }
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

    override fun hideEmptyState() {
        emptyView.visibility = View.GONE
        setHasOptionsMenu(true)
    }
}
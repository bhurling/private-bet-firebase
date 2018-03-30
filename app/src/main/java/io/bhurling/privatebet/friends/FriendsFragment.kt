package io.bhurling.privatebet.friends

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import org.koin.inject

class FriendsFragment : Fragment() {

    private val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
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
}
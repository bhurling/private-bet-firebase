package io.bhurling.privatebet.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import io.bhurling.privatebet.feed.FeedFragment
import io.bhurling.privatebet.friends.FriendsFragment
import kotterknife.bindView
import org.koin.inject

class HomeActivity : AppCompatActivity(), HomePresenter.View {

    private val presenter: HomePresenter by inject()
    private val navigator: Navigator by inject()

    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val pager: ViewPager by bindView(R.id.pager)
    private val tabs: TabLayout by bindView(R.id.navigation)
    private val fab: View by bindView(R.id.fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { navigator.launchCreationFlow(this) }

        pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                if (position == 0) return FeedFragment()
                if (position == 1) return FriendsFragment()

                error("We don't have that many pages.")
            }

            override fun getCount() = 2
        }

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))

        if (savedInstanceState == null && intent.defaultToFriends) {
            pager.setCurrentItem(1, false)
        }

        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.detachView()

        super.onDestroy()
    }

    override fun showPrimaryAction() {
        fab.visibility = View.VISIBLE
    }

    override fun hidePrimaryAction() {
        fab.visibility = View.GONE
    }

    companion object {
        fun makeIntent(context: Context) = Intent(context, HomeActivity::class.java)

        fun makeFriendsIntent(context: Context) = makeIntent(context).apply {
            defaultToFriends = true
        }
    }
}

private var Intent.defaultToFriends: Boolean
    get() = getBooleanExtra("io.bhurling.privatebet.DEFAULT_TO_FRIENDS", false)
    set(value) {
        putExtra("io.bhurling.privatebet.DEFAULT_TO_FRIENDS", value)
    }

package io.bhurling.privatebet.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import io.bhurling.privatebet.Navigator
import io.bhurling.privatebet.R
import io.bhurling.privatebet.feed.FeedFragment
import io.bhurling.privatebet.friends.FriendsFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.inject

class HomeActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: HomeViewModel by inject()
    private val navigator: Navigator by inject()

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        fab.setOnClickListener { navigator.launchCreationFlow(this) }

        pager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                if (position == 0) return FeedFragment()
                if (position == 1) return FriendsFragment()

                error("We don't have that many pages.")
            }

            override fun getCount() = 2
        }

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(navigation))
        navigation.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))

        if (savedInstanceState == null && intent.defaultToFriends) {
            pager.setCurrentItem(1, false)
        }

        viewModel.attach(Observable.never())

        disposables += viewModel.stateOf { isPrimaryActionVisible }
            .subscribe { isVisible ->
                fab.isVisible = isVisible
            }
    }

    override fun onDestroy() {
        viewModel.detach()

        super.onDestroy()
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

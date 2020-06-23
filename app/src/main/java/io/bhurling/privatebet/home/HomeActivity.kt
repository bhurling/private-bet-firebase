package io.bhurling.privatebet.home

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import io.bhurling.privatebet.R
import io.bhurling.privatebet.feed.FeedFragment
import io.bhurling.privatebet.friends.FriendsFragment
import io.bhurling.privatebet.navigation.ActivityStartParams
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.HomeActivityStartParams
import io.bhurling.privatebet.navigation.launch
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: HomeViewModel by inject()

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            this?.let<Activity, Unit> { EntryPoint.CreateBet.launch(it) }
        }

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

        val params = intent?.let {
            ActivityStartParams.from<HomeActivityStartParams>(it)
        }

        if (savedInstanceState == null && params?.defaultToFriends == true) {
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
}

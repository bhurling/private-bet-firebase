package io.bhurling.privatebet.home

import android.app.Activity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.databinding.ActivityMainBinding
import io.bhurling.privatebet.feed.FeedFragment
import io.bhurling.privatebet.friends.FriendsFragment
import io.bhurling.privatebet.navigation.ActivityStartParams
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.HomeActivityStartParams
import io.bhurling.privatebet.navigation.launch
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModels()

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener {
            this?.let<Activity, Unit> { EntryPoint.CreateBet.launch(it) }
        }

        binding.pager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                if (position == 0) return FeedFragment()
                if (position == 1) return FriendsFragment()

                error("We don't have that many pages.")
            }

            override fun getCount() = 2
        }

        binding.pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.navigation))
        binding.navigation.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.pager))

        val params = intent?.let {
            ActivityStartParams.from<HomeActivityStartParams>(it)
        }

        if (savedInstanceState == null && params?.defaultToFriends == true) {
            binding.pager.setCurrentItem(1, false)
        }

        viewModel.attach(Observable.never())

        disposables += viewModel.stateOf { isPrimaryActionVisible }
            .subscribe { isVisible ->
                binding.fab.isVisible = isVisible
            }
    }

    override fun onDestroy() {
        viewModel.detach()

        super.onDestroy()
    }
}

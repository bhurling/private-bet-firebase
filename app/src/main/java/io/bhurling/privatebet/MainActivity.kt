package io.bhurling.privatebet

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.bhurling.privatebet.feed.FeedFragment
import kotterknife.bindView
import org.koin.inject

class MainActivity : AppCompatActivity() {

    private val navigator: Navigator by inject()

    private val pager: ViewPager by bindView(R.id.pager)
    private val tabs: TabLayout by bindView(R.id.navigation)
    private val fab: View by bindView(R.id.fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { navigator.launchCreationFlow(this) }

        pager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return FeedFragment()
            }

            override fun getCount() = 2
        }

        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(pager))
    }
}

package io.bhurling.privatebet.friends

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.databinding.FragmentFriendsBinding
import io.bhurling.privatebet.navigation.EntryPoint
import io.bhurling.privatebet.navigation.launch
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class FriendsFragment : Fragment(R.layout.fragment_friends) {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendsViewModel by viewModels()

    @Inject
    lateinit var adapter: FriendsAdapter

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFriendsBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.friendsConnect.setOnClickListener {
            activity?.let<Activity, Unit> { EntryPoint.Invite.launch(it) }
        }

        binding.friendsList.layoutManager = LinearLayoutManager(activity)
        binding.friendsList.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.friendsList.adapter = adapter

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .map { it.items }
                .filterNotNull()
                .distinctUntilChanged()
                .collect { items ->
                    onItemsChanged(items)
                }
        }

        disposables += adapter.actions()
            .ofType<InviteAction.Accept>()
            .subscribe {
                viewModel.acceptInvitation(it.id)
            }
    }

    private fun onItemsChanged(items: List<FriendsAdapterItem>) {
        binding.friendsEmpty.isVisible = items.isEmpty()

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
        disposables.clear()

        super.onDestroyView()

        _binding = null
    }
}
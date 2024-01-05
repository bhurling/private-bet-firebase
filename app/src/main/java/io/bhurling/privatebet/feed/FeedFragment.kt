package io.bhurling.privatebet.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.bhurling.privatebet.R
import io.bhurling.privatebet.databinding.FragmentFeedBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class FeedFragment : Fragment(R.layout.fragment_feed) {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModels()

    @Inject
    lateinit var adapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFeedBinding.inflate(inflater, container, false).apply {
            _binding = this
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.feed.adapter = adapter
        binding.feed.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        val decoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.transparent_divider
            )!!
        )
        binding.feed.addItemDecoration(decoration)

        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle)
                .collect { state ->
                    adapter.keys = state.keys
                }
        }
    }

    override fun onDestroyView() {
        // we need to unregister the adapter to make sure we call onViewDetachedFromWindow(ViewHolder)
        // for the visible items.
        binding.feed.swapAdapter(null, true)

        super.onDestroyView()

        _binding = null
    }
}
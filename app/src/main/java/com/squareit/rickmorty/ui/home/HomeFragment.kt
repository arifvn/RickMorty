package com.squareit.rickmorty.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareit.rickmorty.R
import com.squareit.rickmorty.data.datasource.common.Resource
import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.databinding.FragmentHomeBinding
import com.squareit.rickmorty.ui.CharacterListAdapter
import com.squareit.rickmorty.ui.CharacterViewModel
import com.squareit.rickmorty.utils.snack
import com.squareit.viewbinder.viewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: CharacterViewModel by viewModels()
    private val binding: FragmentHomeBinding by viewBinder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitleBar()
        onSwipeRefreshAction()
        initRecyclerView()
    }

    private fun setTitleBar() {
        activity?.title = "Characters"
    }

    private fun onSwipeRefreshAction() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getCharacters(true)
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            val adapter = CharacterListAdapter(
                onBtnFavClick = { onBtnFavoriteClick(it) },
                onItemClick = { onItemClick(it) }
            )

            rvHome.adapter = adapter
            rvHome.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            initViewModel(adapter)
        }
    }

    private fun onBtnFavoriteClick(character: Character) {
        viewModel.updateCharacterFavoriteDB(character.id, !character.isFavorite)
    }

    private fun onItemClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToFavoriteDetailFragment(id)
        binding.root.findNavController().navigate(action)
    }

    private fun initViewModel(adapter: CharacterListAdapter) {
        viewModel.result.observe(viewLifecycleOwner) {
            it?.let {
                handleResultState(it, adapter)
            }
        }

        viewModel.getCharacters(false)
    }

    private fun handleResultState(it: Resource<List<Character>>, adapter: CharacterListAdapter) {
        when (it.status) {
            Resource.Status.LOADING -> {
                renderLoadingState()
            }
            Resource.Status.ERROR -> {
                renderErrorState(it.message!!)
            }
            Resource.Status.SUCCESS -> {
                it.data?.let { data ->
                    renderSuccessState(adapter, data)
                }
            }
        }
    }

    private fun renderLoadingState() {
        if (!binding.swipeRefresh.isRefreshing) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun renderErrorState(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
        snack(binding.root, message)
    }

    private fun renderSuccessState(adapter: CharacterListAdapter, data: List<Character>) {
        binding.progressBar.visibility = View.GONE
        binding.swipeRefresh.isRefreshing = false
        adapter.submitList(data as MutableList<Character>)
    }
}
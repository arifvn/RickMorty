package com.squareit.rickmorty.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareit.rickmorty.R
import com.squareit.rickmorty.data.datasource.common.Resource
import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.databinding.FragmentFavoriteBinding
import com.squareit.rickmorty.ui.CharacterListAdapter
import com.squareit.rickmorty.ui.CharacterViewModel
import com.squareit.viewbinder.viewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: CharacterViewModel by viewModels()
    private val binding: FragmentFavoriteBinding by viewBinder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitleBar()
        initRecyclerView()
    }

    private fun setTitleBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Favorite Characters"
    }

    private fun initRecyclerView() {
        binding.apply {
            val adapter = CharacterListAdapter(
                onBtnFavClick = null,
                onItemClick = { onItemClick(it) }
            )
            rvFavorite.adapter = adapter
            rvFavorite.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            initViewModel(adapter)
        }
    }

    private fun onItemClick(id: Int) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToFavoriteDetailFragment(id)
        binding.root.findNavController().navigate(action)
    }

    private fun initViewModel(adapter: CharacterListAdapter) {
        viewModel.result.observe(viewLifecycleOwner) {
            it?.let {
                handleResultState(it, adapter)
            }
        }

        viewModel.getCharactersFavoriteDB()
    }

    private fun handleResultState(it: Resource<List<Character>>, adapter: CharacterListAdapter) {
        when (it.status) {
            Resource.Status.LOADING -> {
                renderLoadingState()
            }
            Resource.Status.ERROR -> {
                renderErrorState()
            }
            Resource.Status.SUCCESS -> {
                it.data?.let { data ->
                    renderSuccessState(adapter, data)
                }
            }
        }
    }

    private fun renderLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun renderErrorState() {
        binding.progressBar.visibility = View.GONE
    }

    private fun renderSuccessState(adapter: CharacterListAdapter, data: List<Character>) {
        if (data.isEmpty()) binding.tvError.visibility = View.VISIBLE
        else binding.tvError.visibility = View.GONE

        binding.progressBar.visibility = View.GONE
        adapter.submitList(data as MutableList<Character>)
    }
}
package com.squareit.rickmorty.ui.favorite_detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.squareit.rickmorty.R
import com.squareit.rickmorty.data.datasource.common.Resource
import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.databinding.FragmentFavoriteDetailBinding
import com.squareit.viewbinder.viewBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteDetailFragment : Fragment(R.layout.fragment_favorite_detail) {

    private val binding: FragmentFavoriteDetailBinding by viewBinder()
    private val viewModel: CharacterDetailViewModel by viewModels()
    private val args: FavoriteDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavigationUpButton()
        viewModel.getCharacter(args.id)
        initViewModel()
    }

    private fun setNavigationUpButton() {
        setHasOptionsMenu(true)
    }

    private fun initViewModel() {
        viewModel.result.observe(viewLifecycleOwner) {
            it?.let { handleResultStatus(it) }
        }
    }

    private fun handleResultStatus(it: Resource<Character>) {
        when (it.status) {
            Resource.Status.LOADING -> {
                renderLoadingState()
            }
            Resource.Status.ERROR -> {
                renderErrorState()
            }
            Resource.Status.SUCCESS -> {
                it.data?.let { data ->
                    renderSuccessState(data)
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

    private fun renderSuccessState(data: Character) {
        binding.apply {
            setTitleBar(data.name)

            progressBar.visibility = View.GONE

            Glide.with(root.context)
                .load(data.image)
                .into(ivImage)

            tvName.text = data.name
            tvGender.text = data.gender
            tvSpecies.text = data.species
            tvStatus.text = data.status

            btnFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    root.context,
                    if (data.isFavorite) R.drawable.ic_favorite_red
                    else R.drawable.ic_favorite
                )
            )
        }
    }

    private fun setTitleBar(name: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = name
    }
}
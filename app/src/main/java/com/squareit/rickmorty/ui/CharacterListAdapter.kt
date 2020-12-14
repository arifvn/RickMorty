package com.squareit.rickmorty.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareit.rickmorty.R
import com.squareit.rickmorty.data.entities.Character
import com.squareit.rickmorty.databinding.ItemCharacterBinding
import com.squareit.rickmorty.utils.lok

class CharacterListAdapter(
    private val onBtnFavClick: ((character: Character) -> Unit)?,
    private val onItemClick: ((id: Int) -> Unit)?
) :
    RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    })

    fun submitList(data: MutableList<Character>) {
        try {
            differ.submitList(data)
        } catch (e: Exception) {
            lok(e.localizedMessage!!)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        val itemBinding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindView(differ.currentList[position])
    }

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(character: Character) {

            binding.apply {
                Glide.with(root.context)
                    .load(character.image)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivImage)

                tvName.text = character.name
                tvSpecies.text = character.species
                tvStatus.text = character.status

                btnFavorite.setImageDrawable(setBtnFavoriteDrawable(this, character))

                if (onBtnFavClick != null) {
                    btnFavorite.setOnClickListener {
                        btnFavorite.setImageDrawable(setBtnFavoriteDrawable(this, character))
                        onBtnFavClick.invoke(character)
                    }
                } else btnFavorite.visibility = View.GONE

                if (onItemClick != null) {
                    root.setOnClickListener {
                        onItemClick.invoke(character.id)
                    }
                }
            }
        }

        private fun setBtnFavoriteDrawable(
            binding: ItemCharacterBinding,
            character: Character
        ): Drawable? {
            return ContextCompat.getDrawable(
                binding.root.context,
                if (character.isFavorite) R.drawable.ic_favorite_red
                else R.drawable.ic_favorite
            )
        }
    }
}
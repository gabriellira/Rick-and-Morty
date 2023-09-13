package br.com.lira.rickandmorty.features.episodes.presentation.view.adapter

import androidx.recyclerview.widget.DiffUtil
import br.com.lira.rickandmorty.features.episodes.presentation.model.EpisodeUIModel

object EpisodeItemDiffCallback : DiffUtil.ItemCallback<EpisodeUIModel>() {

    override fun areItemsTheSame(
        oldItem: EpisodeUIModel,
        newItem: EpisodeUIModel
    ): Boolean {
        return when {
            oldItem is EpisodeUIModel.Episode &&
                    newItem is EpisodeUIModel.Episode -> oldItem.id == newItem.id

            oldItem is EpisodeUIModel.Header &&
                    newItem is EpisodeUIModel.Header -> oldItem.title == newItem.title

            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: EpisodeUIModel,
        newItem: EpisodeUIModel
    ) = oldItem == newItem
}
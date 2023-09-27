package br.com.lira.rickandmorty.features.characters.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import br.com.lira.rickandmorty.core.viewmodel.ViewModel
import br.com.lira.rickandmorty.features.characters.domain.usecase.GetCharacterByIdUseCase
import br.com.lira.rickandmorty.features.characters.presentation.mapper.CharacterDetailsModelToUIMapper
import br.com.lira.rickandmorty.features.characters.presentation.mapper.CharacterEpisodeUIModelMapper
import br.com.lira.rickandmorty.features.characters.presentation.viewaction.CharacterDetailsViewAction
import br.com.lira.rickandmorty.features.characters.presentation.viewstate.CharacterDetailsViewState
import br.com.lira.rickandmorty.features.episodes.domain.usecase.GetMultipleEpisodesUseCase
import br.com.lira.rickandmorty.main.domain.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterById: GetCharacterByIdUseCase,
    private val uiMapper: CharacterDetailsModelToUIMapper,
    private val getMultipleEpisodes: GetMultipleEpisodesUseCase,
    private val episodeUiMapper: CharacterEpisodeUIModelMapper
) : ViewModel<CharacterDetailsViewState, CharacterDetailsViewAction>(CharacterDetailsViewState()) {

    fun init(characterId: Long?) {
        setState { it.setLoadingState() }

        viewModelScope.launch {
            runCatching {
                getCharacterById(characterId)
            }.onSuccess { character ->
                handleCharacterSuccess(character)
                fetchEpisodes(character.episodeIds)
            }.onFailure { e ->
                Log.e("AppError", e.message.orEmpty())
            }
        }
    }

    private fun handleCharacterSuccess(character: Character) {
        val uiModel = uiMapper.mapFrom(character)
        setState { it.setSuccessState(uiModel) }
    }

    private suspend fun fetchEpisodes(episodeIds: List<String>) {
        runCatching {
            setState { it.setEpisodesLoadingState() }
            getMultipleEpisodes(episodeIds)
        }.onSuccess { episodes ->
            val episodesUi = episodes.map(episodeUiMapper::mapFrom)
            setState { it.setEpisodesSuccessState(episodes = episodesUi) }
        }.onFailure {
            setState { it.setEpisodesErrorState() }
        }
    }
}
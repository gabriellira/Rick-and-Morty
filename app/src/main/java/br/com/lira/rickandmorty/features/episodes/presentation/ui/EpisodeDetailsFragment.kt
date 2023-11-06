package br.com.lira.rickandmorty.features.episodes.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.lira.rickandmorty.R
import br.com.lira.rickandmorty.core.toolkit.addPopBackStackHandler
import br.com.lira.rickandmorty.core.toolkit.popBackStack
import br.com.lira.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import br.com.lira.rickandmorty.features.characters.presentation.CharacterNavigator
import br.com.lira.rickandmorty.features.characters.presentation.viewaction.CharactersListViewAction
import br.com.lira.rickandmorty.features.episodes.presentation.viewmodel.EpisodeDetailsViewAction
import br.com.lira.rickandmorty.features.episodes.presentation.viewmodel.EpisodeDetailsViewModel
import br.com.lira.rickandmorty.features.episodes.presentation.viewmodel.EpisodeDetailsViewState
import br.com.lira.rickandmorty.features.shared.presentation.adapter.CharactersListAdapter
import br.com.lira.rickandmorty.main.navigation.ImmersiveNavigationMode
import br.com.lira.rickandmorty.main.navigation.NavigationModeHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_EPISODE_ID = "episode_id"

@AndroidEntryPoint
class EpisodeDetailsFragment : Fragment(), NavigationModeHandler by ImmersiveNavigationMode {

    private var episodeId: Long? = null
    private lateinit var binding: FragmentEpisodeDetailsBinding
    private val viewModel: EpisodeDetailsViewModel by viewModels()

    private lateinit var charactersAdapter: CharactersListAdapter

    @Inject
    lateinit var characterNavigator: CharacterNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episodeId = it.getLong(ARG_EPISODE_ID)
        }

        addPopBackStackHandler()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEpisodeDetailsBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(episodeId)
        setupToolbar()
        setupViews()
        observeViewState()
        observeActions()
    }

    private fun setupViews() {
        charactersAdapter = CharactersListAdapter(true) {
            viewModel.onCharacterClicked(it)
        }
        binding.content.charactersList.adapter = charactersAdapter
    }

    private fun setupToolbar() = with(binding.toolbarView) {
        title.setText(R.string.episode_details_title)
        searchIcon.isVisible = false
        navigationIcon.isVisible = true
        navigationIcon.setOnClickListener {
            popBackStack()
        }
    }

    private fun observeActions() {
        viewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is EpisodeDetailsViewAction.OpenCharacterDetails -> {
                    openCharactersScreen(action.characterId)
                }
            }
        }
    }

    private fun observeViewState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.loading.root.isVisible = state.isLoading
            binding.content.root.isVisible = state.shouldDisplayContent

            handleEpisode(state)
            handleCharactersList(state)
        }
    }

    private fun handleCharactersList(state: EpisodeDetailsViewState) = with(binding.content) {
        charactersList.isVisible = state.shouldDisplayCharacters
        episodesLoading.root.isVisible = state.isCharactersLoading
        charactersListHeader.isVisible = state.shouldDisplayCharacters
        charactersListHeader.text = state.charactersHeader
        charactersAdapter.submitList(state.characters)
    }

    private fun handleEpisode(state: EpisodeDetailsViewState) = with(binding.content) {
        state.episode?.let { episodeUi ->
            tvName.text = episodeUi.name
            episodeNumber.text = episodeUi.episodeNumber
            airDate.text = episodeUi.airDate
        }
    }

    private fun openCharactersScreen(characterId: Long) {
        characterNavigator.openCharacterDetails(this, characterId)
    }

    companion object {
        fun newInstance(episodeId: Long) = EpisodeDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_EPISODE_ID, episodeId)
            }
        }
    }
}
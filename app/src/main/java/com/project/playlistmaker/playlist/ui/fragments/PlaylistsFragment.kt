package com.project.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.FragmentPlaylistsBinding
import com.project.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import com.project.playlistmaker.playlist.ui.adapters.PlaylistAdapter
import com.project.playlistmaker.playlist.ui.viewmodels.PlaylistsViewModel
import com.project.playlistmaker.utils.ArgsTransfer
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val myPlaylistArray = ArrayList<Playlist>()
    private lateinit var playlistAdapter: PlaylistAdapter

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistsBinding
    private lateinit var listenerAdapter: PlaylistAdapter.PlaylistClickListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.getEmptyStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        listenerAdapter = object : PlaylistAdapter.PlaylistClickListener {
            override fun onClickView(playlist: Playlist) {
                findNavController().navigate(
                    R.id.action_MediaFragment_to_PlaylistTracksFragment,
                    bundleOf(Pair(PlaylistTracksFragment.ID_ARG, playlist.id))
                )
            }
        }

        playlistAdapter = PlaylistAdapter(myPlaylistArray, { lifecycleScope }, listenerAdapter)
        binding.playlistsRecyclerView.adapter = playlistAdapter
        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.btnCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_MediaFragment_to_NewPlaylistFragment)
        }
        playlistsViewModel.getAllPlaylists()
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.getAllPlaylists()
        checkTransferredArgs()
    }

    private fun checkTransferredArgs() {
        val bundle = (requireActivity() as ArgsTransfer).getArgs()
        if (bundle != null) {
            (requireActivity() as ArgsTransfer).postArgs(null)
        }
    }

    private fun render(state: EmptyStatePlaylist) {
        when (state) {
            is EmptyStatePlaylist.EmptyPlaylist -> showEmptyState()
            is EmptyStatePlaylist.NotEmptyPlaylist -> showNotEmptyState(state.playlist)
        }
    }

    private fun showEmptyState() {
        binding.playlistsRecyclerView.isVisible = false
        binding.llLibraryError.isVisible = true
    }

    private fun showNotEmptyState(list: List<Playlist>) {
        binding.llLibraryError.isVisible = false
        binding.playlistsRecyclerView.isVisible = true

        myPlaylistArray.clear()
        myPlaylistArray.addAll(list)
        playlistAdapter.notifyDataSetChanged()

    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
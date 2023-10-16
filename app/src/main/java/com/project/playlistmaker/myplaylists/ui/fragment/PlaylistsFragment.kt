package com.project.playlistmaker.myplaylists.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.project.playlistmaker.R
import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.databinding.FragmentMyPlaylistsBinding
import com.project.playlistmaker.myplaylists.ui.adapters.PlaylistsLargeAdapter
import com.project.playlistmaker.myplaylists.viewmodel.models.PlaylistState
import com.project.playlistmaker.myplaylists.viewmodel.viewmodel.MyPlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), PlaylistsLargeAdapter.PlaylistLargeClickListener {

    //Binding
    private lateinit var binding: FragmentMyPlaylistsBinding

    //ViewModel
    private val myPlayListViewModel by viewModel<MyPlayListViewModel>()

    //Adapter
    private val playlistsLargeAdapter = PlaylistsLargeAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        myPlayListViewModel.displayState()
        myPlayListViewModel.observePlaylists().observe(viewLifecycleOwner) {
            render(it)
        }

        //CreatePlaylistBtn
        binding.btnCreatePlaylist.setOnClickListener {
            myPlayListViewModel.clickDebounce()
            findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        myPlayListViewModel.displayState()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.DisplayEmptyPlaylists -> displayEmptyPlaylists()
            is PlaylistState.DisplayPlaylists -> displayPlaylists(state.playlists)
        }

    }

    private fun displayEmptyPlaylists() {
        binding.playlistsRecyclerView.visibility = View.GONE
        binding.llLibraryError.visibility = View.VISIBLE
    }

    private fun displayPlaylists(playlists: List<Playlist>) {
        binding.llLibraryError.visibility = View.GONE
        binding.playlistsRecyclerView.visibility = View.VISIBLE
        playlistsLargeAdapter.playlists.clear()
        playlistsLargeAdapter.playlists.addAll(playlists)
        playlistsLargeAdapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        binding.playlistsRecyclerView.layoutManager = GridLayoutManager(requireContext(), NUMBER_OF_COLUMNS)
        binding.playlistsRecyclerView.adapter = playlistsLargeAdapter
    }

    override fun onPlaylistClickListener(playlist: Playlist) {
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
        private const val NUMBER_OF_COLUMNS = 2
    }
}

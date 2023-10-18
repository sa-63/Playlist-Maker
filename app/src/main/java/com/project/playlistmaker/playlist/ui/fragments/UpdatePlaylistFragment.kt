package com.project.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.playlistmaker.R
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb

class UpdatePlaylistFragment: NewPlaylistFragment() {

    companion object{
        const val ID_PLAYLIST = "ID_PL"
        const val NAME_PLAYLIST = "NAME_PL"
        const val DESCRIPTION_PLAYLIST = "DESC_PL"
        const val IMAGE_PLAYLIST = "IMAGE_PL"
    }

    private var idPlaylistTemp: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idPlaylistTemp = requireArguments().getInt(ID_PLAYLIST)
        playlistNameTemp = requireArguments().getString(NAME_PLAYLIST)
        descriptionPlaylistTemp = requireArguments().getString(DESCRIPTION_PLAYLIST)
        if(requireArguments().getString(IMAGE_PLAYLIST) != null){
            uriImageTemp = requireArguments().getString(IMAGE_PLAYLIST)!!.toUri()
        }

        val roundCorners = RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_button_low))
        val options = RequestOptions().transform(CenterCrop(),roundCorners)
        Glide.with(this)
            .load(uriImageTemp)
            .placeholder(R.drawable.add_playlist_holder)
            .apply(options)
            .into(binding.ivCover)

        binding.titleNewPlaylist.text = getString(R.string.edit)
        binding.btnCreatePlaylist.text = getString(R.string.save)
        binding.etPlaylistTitle.setText(playlistNameTemp)
        binding.etPlaylistDescription.setText(descriptionPlaylistTemp)

        binding.btnCreatePlaylist.setOnClickListener{
                newPlaylistViewModel.updatePlaylist(
                    idPlaylistTemp!!,
                    playlistNameTemp,
                    descriptionPlaylistTemp,
                    uriImageTemp.toString()
                )
            }

        binding.backButtonNewPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }
        switchOnBackPressedDispatcher(false,null)
        }

    override fun switchOnBackPressedDispatcher(
        switch: Boolean,
        dialogExit: MaterialAlertDialogBuilder?
    ) {
    }

       override fun render(state: StateAddDb){
              when(state){
                  is StateAddDb.NoError -> {
                      findNavController().popBackStack()
                  }
                  is StateAddDb.Error -> {
                      Log.e("ErrorUpdatePlaylist",getString(R.string.error_query_db))
                  }
                  is StateAddDb.Match -> {
                      Log.e("ErrorUpdatePlaylist", getString(R.string.error_match_playlist))
                  }
                  is StateAddDb.NoData ->{
                      //State for Single Live Event, show Toast and other way. Not use in this fragment.
                  }
              }
        }
   }



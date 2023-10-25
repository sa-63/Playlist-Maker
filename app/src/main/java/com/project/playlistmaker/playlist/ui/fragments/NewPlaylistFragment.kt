package com.project.playlistmaker.playlist.ui.fragments

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.project.playlistmaker.mainscreen.RootActivity
import com.project.playlistmaker.playerscreen.ui.activity.PlayerActivity
import com.project.playlistmaker.playlist.domain.models.entity.Playlist
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb
import com.project.playlistmaker.playlist.ui.viewmodels.NewPlaylistViewModel
import com.project.playlistmaker.utils.ArgsTransfer
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class NewPlaylistFragment : Fragment() {

    open val newPlaylistViewModel: NewPlaylistViewModel by viewModel()
    open lateinit var binding: FragmentNewPlaylistBinding
    private val requester = PermissionRequester.instance()

    var uriImageTemp: Uri? = null
    var playlistNameTemp: String? = null
    var descriptionPlaylistTemp: String? = null


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        if (it != null) {
            val roundCorners =
                RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_button_low))
            val options = RequestOptions().transform(CenterCrop(), roundCorners)

            Glide.with(this).load(it.toString()).placeholder(R.drawable.add_playlist_holder)
                .apply(options).into(binding.ivCover)
            uriImageTemp = it
        } else {
            Log.d("Image", R.string.image_not_select.toString())
        }
    }


    open fun checkPermission(): Boolean {
        val permissionProvided =
            ContextCompat.checkSelfPermission(requireContext(), getCheckedStorageConst())
        return (permissionProvided == PackageManager.PERMISSION_GRANTED)
    }

    private fun getCheckedStorageConst(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogExit =
            MaterialAlertDialogBuilder(requireContext(), R.style.DialogStyle)
                .setTitle(R.string.ask_to_cancel)
                .setMessage(R.string.confirm_cancel)
                .setNegativeButton(R.string.no) { dialog, which ->
                }
                .setPositiveButton(R.string.finish) { dialog, which ->
                    backStackSelector()
                }
                .setOnDismissListener {
                }

        newPlaylistViewModel.getLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.ivCover.setOnClickListener {
            if (!checkPermission()) {
                lifecycleScope.launch {
                    requester.request(getCheckedStorageConst()).collect {
                        when (it) {
                            is PermissionResult.Granted -> {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }

                            is PermissionResult.Denied.NeedsRationale -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.rationale_permission_on_storage),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            is PermissionResult.Denied.DeniedPermanently -> {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.data =
                                    Uri.fromParts("package", requireContext().packageName, null)
                                requireContext().startActivity(intent)
                            }

                            is PermissionResult.Cancelled -> {
                                return@collect
                            }

                            is PermissionResult.Denied -> {
                                Log.d("Permission", R.string.permission_denied.toString())
                            }
                        }
                    }
                }

            } else {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        binding.backButtonNewPlaylist.setOnClickListener {
            if (uriImageTemp == null && (playlistNameTemp == null || playlistNameTemp == "") && (descriptionPlaylistTemp == null || descriptionPlaylistTemp == "")) {
                backStackSelector()
            } else {
                dialogExit.show().apply {
                    getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.blue))
                    getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(resources.getColor(R.color.blue))
                }
            }
        }

        binding.etPlaylistTitle.addTextChangedListener(getTextWatcherForName())
        binding.etPlaylistDescription.addTextChangedListener(getTextWatcherForDescription())

        binding.btnCreatePlaylist.setOnClickListener {
            playlistCreatedSnackBar(view.findViewById(R.id.parent_layout))
            newPlaylistViewModel.addPlaylist(
                Playlist(
                    playlistName = playlistNameTemp,
                    descriptionPlaylist = descriptionPlaylistTemp,
                    imageInStorage = uriImageTemp.toString()
                )
            )
        }
        switchOnBackPressedDispatcher(true, dialogExit)
    }

    open fun switchOnBackPressedDispatcher(
        switch: Boolean, dialogExit: MaterialAlertDialogBuilder?
    ) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(switch) {
                override fun handleOnBackPressed() {
                    if (uriImageTemp == null && (playlistNameTemp == null || playlistNameTemp == "") && (descriptionPlaylistTemp == null || descriptionPlaylistTemp == "")) {
                        backStackSelector()
                    } else {
                        dialogExit!!.show().apply {
                            getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                                resources.getColor(
                                    R.color.blue
                                )
                            )
                            getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(
                                resources.getColor(
                                    R.color.blue
                                )
                            )
                        }
                    }
                }
            })
    }

    private fun backStackSelector() {
        if (creationWithoutGraph) {
            requireActivity().findViewById<CoordinatorLayout>(R.id.player_constraint).isVisible =
                true
            parentFragmentManager.popBackStack()
            creationWithoutGraph = false
        } else {
            findNavController().popBackStack()
        }
    }

    //SnackBar
    private fun playlistCreatedSnackBar(view: View) {
        val snackBar = Snackbar.make(
            requireContext(),
            view,
            "${getString(R.string.playlist)} ${binding.etPlaylistTitle.text} ${getString(R.string.created)}",
            Snackbar.LENGTH_LONG
        )
        snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
            textSize = resources.getDimension(R.dimen.text_5)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            typeface = Typeface.DEFAULT
        }
        snackBar.show()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(IMAGE_PL) != "null") {
                val roundCorners =
                    RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_button_low))
                val options = RequestOptions().transform(CenterCrop(), roundCorners)

                Glide.with(this).load(savedInstanceState.getString(IMAGE_PL))
                    .placeholder(R.drawable.add_playlist_holder).apply(options)
                    .into(binding.ivCover)

                uriImageTemp = savedInstanceState.getString(IMAGE_PL)!!.toUri()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(IMAGE_PL, uriImageTemp.toString())
    }

    override fun onStart() {
        super.onStart()
        if (creationWithoutGraph) requireActivity().findViewById<CoordinatorLayout>(R.id.player_constraint).isVisible =
            false
    }


    private fun getTextWatcherForName(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnCreatePlaylist.isEnabled = s?.isNotEmpty() == true
                playlistNameTemp = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    private fun getTextWatcherForDescription(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descriptionPlaylistTemp = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    open fun render(state: StateAddDb) {
        when (state) {
            is StateAddDb.Error -> {
                Log.e("ErrorAddPlaylist", R.string.error_add_playlist.toString())
            }

            is StateAddDb.NoError -> {
                if (creationWithoutGraph) {
                    (requireActivity() as ArgsTransfer).postArgs(
                        bundleOf(
                            Pair(
                                PlayerActivity.BUNDLE_ARGS,
                                playlistNameTemp
                            )
                        )
                    )
                    requireActivity().findViewById<CoordinatorLayout>(R.id.player_constraint).isVisible =
                        true
                    creationWithoutGraph = false
                    parentFragmentManager.popBackStack()
                } else {
                    (requireActivity() as ArgsTransfer).postArgs(
                        bundleOf(
                            Pair(
                                RootActivity.BUNDLE_ARGS,
                                playlistNameTemp
                            )
                        )
                    )
                    findNavController().popBackStack()
                }
            }

            is StateAddDb.Match -> {
                Log.e("ErrorAddPlaylist", getString(R.string.error_match_playlist))
            }

            is StateAddDb.NoData -> {
            }
        }
    }

    companion object {
        const val IMAGE_PL = "IMAGE"
        private var creationWithoutGraph = false
        fun newInstance(flagWithoutGraph: Boolean): NewPlaylistFragment {
            creationWithoutGraph = flagWithoutGraph
            return NewPlaylistFragment()
        }
    }
}
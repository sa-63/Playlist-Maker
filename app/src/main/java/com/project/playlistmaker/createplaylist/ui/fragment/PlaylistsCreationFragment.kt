package com.project.playlistmaker.createplaylist.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.project.playlistmaker.R
import com.project.playlistmaker.createplaylist.ui.viewmodel.CreateButtonState
import com.project.playlistmaker.createplaylist.ui.viewmodel.PermissionState
import com.project.playlistmaker.createplaylist.ui.viewmodel.PlaylistCreationState
import com.project.playlistmaker.createplaylist.ui.viewmodel.PlaylistsCreationViewModel
import com.project.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class PlaylistsCreationFragment : Fragment() {
    //ViewModel
    private val playlistsCreationViewModel: PlaylistsCreationViewModel by viewModel()

    //Binding
    private lateinit var binding: FragmentNewPlaylistBinding

    //Request Related
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        checkPermission()

        //Select image from storage
        val selectMedia =
            registerForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    binding.ivCover.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.ivCover.setImageURI(uri)
                    playlistsCreationViewModel.saveCoverUri(uri)
                } else {
                    Log.d("picker", "not chosen")
                }
            }

        //Cover
        binding.ivCover.setOnClickListener {
            playlistsCreationViewModel.coverClick()
        }

        //Title
        binding.etPlaylistTitle.doOnTextChanged { text, _, _, _ ->
            playlistsCreationViewModel.onNameChanged(text)
            setHintAndBoxColor(text, binding.textInputPlaylistTitle)
        }

        //Description
        binding.etPlaylistDescription.doOnTextChanged { text, _, _, _ ->
            playlistsCreationViewModel.onDescriptionChanged(text)
            setHintAndBoxColor(text, binding.textInputPlaylistDescription)
        }

        //Create
        binding.btnCreatePlaylist.setOnClickListener {
            playlistsCreationViewModel.onCreatePlClicked()
            playlistCreatedSnackBar(view.findViewById(R.id.parent_layout))
        }

        //Back btn
        binding.backButtonNewPlaylist.setOnClickListener() {
            playlistsCreationViewModel.onBackPressed()
        }

        //Back gesture
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            playlistsCreationViewModel.onBackPressed()
        }

        //Confirm cancel
        val confirmDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_MyMaterialAlertDialog)
                .setTitle(resources.getString(R.string.ask_to_cancel))
                .setMessage(resources.getString(R.string.confirm_cancel))
                .setNegativeButton(resources.getString(R.string.cancel)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.finish)) { _, _ -> findNavController().navigateUp() }

        playlistsCreationViewModel.observeScreenState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistCreationState.EMPTY_STATE -> findNavController().navigateUp()
                PlaylistCreationState.PLAYLIST_CREATED -> findNavController().navigateUp()
                PlaylistCreationState.SHOW_DIALOG -> {
                    confirmDialog.show()
                }
            }
        }

        playlistsCreationViewModel.observeCreateButtonState().observe(viewLifecycleOwner) { state ->
            when (state) {
                CreateButtonState.DISABLED -> binding.btnCreatePlaylist.isEnabled = false
                CreateButtonState.ENABLED -> binding.btnCreatePlaylist.isEnabled = true
            }
        }

        //Permission
        playlistsCreationViewModel.observePermissionState().observe(viewLifecycleOwner) { state ->
            when (state) {
                PermissionState.GRANTED -> selectMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )

                PermissionState.DENIED_PERMANENTLY -> displaySettings()
                else -> permissionRationaleToast()
            }
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

    //Show settings app
    private fun displaySettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", requireContext().packageName, null)
        requireContext().startActivity(intent)
    }

    //For permission
    private fun permissionRationaleToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.read_image_rationale),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkPermission(): Boolean {
        val permissionProvided =
            ContextCompat.checkSelfPermission(requireContext(), getCheckedStorageConst())
        return (permissionProvided == PackageManager.PERMISSION_GRANTED)
    }

    private fun getCheckedStorageConst(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    }

    //Change edittext color
    private fun setHintAndBoxColor(text: CharSequence?, view: TextInputLayout) {
        if (text.isNullOrEmpty()) {
            view.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.pl_creation_edit_text)
            ContextCompat.getColorStateList(requireContext(), R.color.pl_creation_edit_text)
                ?.let { view.setBoxStrokeColorStateList(it) }
        } else {
            view.defaultHintTextColor =
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.pl_creation_edit_text_blue
                )
            ContextCompat.getColorStateList(requireContext(), R.color.pl_creation_edit_text_blue)
                ?.let { view.setBoxStrokeColorStateList(it) }
        }
    }
}
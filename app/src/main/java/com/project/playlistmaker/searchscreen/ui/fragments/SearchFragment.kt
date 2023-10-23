package com.project.playlistmaker.searchscreen.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.FragmentSearchBinding
import com.project.playlistmaker.playerscreen.ui.activity.PlayerActivity
import com.project.playlistmaker.playlist.ui.model.TrackPlr
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.ui.adapter.TrackListAdapter
import com.project.playlistmaker.searchscreen.ui.models.SearchScreenStatus
import com.project.playlistmaker.searchscreen.ui.view_holder.TrackListViewHolder
import com.project.playlistmaker.searchscreen.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), TrackListViewHolder.TrackListClickListener {
    //ViewModel
    private val searchViewModel by viewModel<SearchViewModel>()

    //Binding
    private lateinit var binding: FragmentSearchBinding

    //Adapters
    private lateinit var tracksAdapter: TrackListAdapter
    private lateinit var historyAdapter: TrackListAdapter

    //Lists for adapters
    private var tracksList = mutableListOf<Track>()
    private var historyList = mutableListOf<Track>()

    //Other
    var saveInputText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ViewModel
        searchViewModel.observeSearchStatusResultLiveData()
            .observe(viewLifecycleOwner) { status ->
                showContentBasedOnState(status)
            }

        //Adapters
        initAdapters()
        historyList.addAll(searchViewModel.getSearchHistory())

        //Listeners
        binding.ivClear.setOnClickListener {
            binding.etSearch.text.clear()
            binding.ivClear.visibility = clearButtonVisibility("")
            binding.etSearch.hideKeyboard()
            clearAndNotifyTracksAdapter()
            searchViewModel.showHistory()
        }

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty() && historyList.isNotEmpty()) {
                showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
            } else {
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }
        }

        binding.btnUpdate.setOnClickListener {
            search()
        }

        binding.btnClearHistory.setOnClickListener {
            searchViewModel.clearSearchHistory()
            historyAdapter.notifyItemRangeChanged(0, historyList.size)
            binding.llSearchHistory.visibility = View.GONE
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.ivClear.visibility = clearButtonVisibility(s)
                saveInputText = binding.etSearch.text.toString()
                if (binding.etSearch.text.isNotEmpty()) {
                    searchViewModel.searchDebounce(s.toString())
                }
            }
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }
            false
        }
    }

    //Refresh RecyclerView items
    private fun clearAndNotifyTracksAdapter() {
        tracksList.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun clearAndNotifyHistoryAdapter() {
        historyList.clear()
        historyAdapter.notifyDataSetChanged()
    }

    //Search tracks
    private fun search() {
        searchViewModel.searchForTracks(binding.etSearch.text.toString())
    }

    //Show error message you need
    enum class ViewToShow {
        SHOW_TRACKS_RV, SHOW_ERROR_LL, SHOW_SEARCH_HISTORY_LL, SHOW_PROGRESS_BAR
    }

    private fun showErrorMessage(errorType: SearchScreenStatus) {
        var errorText = ""
        var needBtn = false
        var imageSrc: Int = R.drawable.placeholder

        when (errorType) {
            is SearchScreenStatus.ConnectionError -> {
                errorText = getString(R.string.connection_error)
                needBtn = true
                imageSrc = R.drawable.no_connection_image
            }

            is SearchScreenStatus.NotFound -> {
                errorText = getString(R.string.not_found)
                imageSrc = R.drawable.nothing_found_image
            }

            else -> {}
        }
        if (errorText.isNotEmpty()) {
            clearAndNotifyTracksAdapter()

            binding.tvError.text = errorText
            binding.ivError.setImageResource(imageSrc)
            showViews(ViewToShow.SHOW_ERROR_LL)
            binding.btnClearHistory.visibility = View.GONE

            if (needBtn) {
                binding.btnUpdate.visibility = View.VISIBLE
            }
        } else {
            showViews(ViewToShow.SHOW_TRACKS_RV)
        }
    }

    private fun showContentBasedOnState(status: SearchScreenStatus) {
        when (status) {
            is SearchScreenStatus.ShowTracks -> {
                clearAndNotifyTracksAdapter()
                tracksList.addAll(status.trackList)
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }

            is SearchScreenStatus.ShowHistory -> {
                clearAndNotifyHistoryAdapter()
                historyList.addAll(status.historyList)
                if (historyList.isNotEmpty()) {
                    showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
                }
            }

            is SearchScreenStatus.Loading -> {
                showViews(ViewToShow.SHOW_PROGRESS_BAR)
            }

            is SearchScreenStatus.NotFound -> {
                showErrorMessage(status)
            }

            is SearchScreenStatus.ConnectionError -> {
                showErrorMessage(status)
            }
        }
    }

    //RecyclerView click listener
    override fun setTrackClickListener(track: Track) {
        searchViewModel.clickDebounce()
        openPlayer(track)
        searchViewModel.addToSearchHistory(track)
    }

    //Open Player
    private fun openPlayer(track: Track) {
        val intent = Intent(requireContext(), PlayerActivity::class.java)
        intent.putExtra("dataTrack", TrackPlr.mappingTrack(track))
        requireContext().startActivity(intent)
    }

    //Adapters
    private fun initAdapters() {
        tracksAdapter = TrackListAdapter(tracksList, this)
        binding.rvTracks.adapter = tracksAdapter

        historyAdapter = TrackListAdapter(historyList, this)
        binding.rvSearchHistory.adapter = historyAdapter
    }

    //EditText related
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.etSearch.setText(saveInputText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_CONTENT, saveInputText)
        super.onSaveInstanceState(outState)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    //Change views visibility
    private fun showViews(view: ViewToShow) = with(binding) {
        when (view) {
            ViewToShow.SHOW_TRACKS_RV -> {
                llError.visibility = View.GONE
                llSearchHistory.visibility = View.GONE
                pbSearch.visibility = View.GONE
                rvTracks.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_ERROR_LL -> {
                rvTracks.visibility = View.GONE
                llSearchHistory.visibility = View.GONE
                pbSearch.visibility = View.GONE
                llError.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_SEARCH_HISTORY_LL -> {
                rvTracks.visibility = View.GONE
                llError.visibility = View.GONE
                pbSearch.visibility = View.GONE
                llSearchHistory.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_PROGRESS_BAR -> {
                rvTracks.visibility = View.GONE
                llError.visibility = View.GONE
                llSearchHistory.visibility = View.GONE
                pbSearch.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val EDIT_TEXT_CONTENT = "CONTENT"
    }
}
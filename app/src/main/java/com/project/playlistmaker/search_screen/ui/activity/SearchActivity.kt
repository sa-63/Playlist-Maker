 package com.project.playlistmaker.search_screen.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivitySearchBinding
import com.project.playlistmaker.player_screen.ui.activity.ActivityPlayer
import com.project.playlistmaker.player_screen.ui.activity.ActivityPlayer.Companion.TRACK_DTO_DATA
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.search_screen.ui.adapter.TrackListAdapter
import com.project.playlistmaker.search_screen.ui.models.SearchScreenStatus
import com.project.playlistmaker.search_screen.ui.view_holder.TrackListViewHolder
import com.project.playlistmaker.search_screen.ui.view_model.SearchActivityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

 class SearchActivity : AppCompatActivity(), TrackListViewHolder.TrackListClickListener {
    //ViewModel
    private val searchActivityViewModel by viewModel<SearchActivityViewModel>()
    //Binding
    private lateinit var binding: ActivitySearchBinding

    //Adapters
    private lateinit var tracksAdapter: TrackListAdapter
    private lateinit var historyAdapter: TrackListAdapter

    //Lists for adapters
    private var tracksList = mutableListOf<Track>()
    private var historyList = mutableListOf<Track>()

    //Other
    var saveInputText: String? = null

    companion object {
        const val EDIT_TEXT_CONTENT = "CONTENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //Binding
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ViewModel
        searchActivityViewModel.observeSearchStatusResultLiveData().observe(this) { status ->
            showContentBasedOnState(status)
        }
        //Adapters
        initAdapters()
        historyList.addAll(searchActivityViewModel.getSearchHistory())

        //Listeners
        binding.ivClear.setOnClickListener {
            binding.etSearch.text.clear()
            binding.ivClear.visibility = clearButtonVisibility("")
            binding.etSearch.hideKeyboard()
            searchActivityViewModel.notifyCleared()
        }

        binding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.etSearch.text.isEmpty() && historyList.isNotEmpty()) {
                showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
            } else {
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnUpdate.setOnClickListener {
            search()
        }

        binding.btnClearHistory.setOnClickListener {
            searchActivityViewModel.clearSearchHistory()
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
                    searchActivityViewModel.searchDebounce(s.toString())
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
        searchActivityViewModel.searchForTracks(binding.etSearch.text.toString())
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
            searchActivityViewModel.clickDebounce()
            openPlayer(track)
            searchActivityViewModel.addToSearchHistory(track)
    }

    //Open Player Activity
    private fun openPlayer(track: Track) {
        val intent = Intent(this, ActivityPlayer::class.java)
        intent.putExtra(TRACK_DTO_DATA, track)
        startActivity(intent)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
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
}
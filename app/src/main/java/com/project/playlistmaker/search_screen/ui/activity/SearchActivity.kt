package com.project.playlistmaker.search_screen.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivitySearchBinding
import com.project.playlistmaker.player_screen.ui.activity.ActivityPlayer
import com.project.playlistmaker.player_screen.ui.activity.ActivityPlayer.Companion.TRACK_DTO_DATA
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.search_screen.ui.adapter.TrackListAdapter
import com.project.playlistmaker.search_screen.ui.models.SearchStatusResult
import com.project.playlistmaker.search_screen.ui.view_holder.TrackListViewHolder
import com.project.playlistmaker.search_screen.ui.view_model.SearchActivityViewModel

class SearchActivity : ComponentActivity(), TrackListViewHolder.TrackListClickListener {
    //ViewModel
    private lateinit var searchActivityViewModel: SearchActivityViewModel

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
        searchActivityViewModel = ViewModelProvider(
            this,
            SearchActivityViewModel.getViewModelFactory(this)
        )[SearchActivityViewModel::class.java]

        searchActivityViewModel.observeTracksResultLiveData().observe(this) { trackListVm ->
            clearAndNotifyTracksAdapter()
            tracksList.addAll(trackListVm)
        }

        searchActivityViewModel.observeSearchHistory().observe(this) { historyListVm ->
            clearAndNotifyHistoryAdapter()
            historyList.addAll(historyListVm)
        }

        searchActivityViewModel.observeSearchStatusResultLiveData().observe(this) { errorType ->
            showErrorMessage(errorType)
        }

        //Adapters
        initAdapters()
        searchActivityViewModel.getTracksHistory()
        checkAndShowHistory()

        //Listeners
        binding.clearBtn.setOnClickListener {
            binding.editTextInSearch.setText("")
            binding.clearBtn.visibility = clearButtonVisibility("")
            binding.editTextInSearch.hideKeyboard()
            clearAndNotifyTracksAdapter()
            checkAndShowHistory()
        }

        binding.editTextInSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.editTextInSearch.text.isEmpty() && historyList.isNotEmpty()) {
                showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
            } else {
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }
        }

        binding.backImageBtnInSearch.setOnClickListener {
            onBackPressed()
        }

        binding.updateBtn.setOnClickListener {
            search()
        }

        binding.clearSearchHistoryBtn.setOnClickListener {
            searchActivityViewModel.clearSearchHistory()
            historyAdapter.notifyItemRangeChanged(0, historyList.size)
            binding.searchHistoryLl.visibility = View.GONE
        }

        //TextWatcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchActivityViewModel.searchDebounce(s.toString())
                binding.clearBtn.visibility = clearButtonVisibility(s)
                saveInputText = binding.editTextInSearch.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.editTextInSearch.addTextChangedListener(simpleTextWatcher)

        binding.editTextInSearch.setOnEditorActionListener { _, actionId, _ ->
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

    override fun onPause() {
        searchActivityViewModel.addHistoryToLocalDb()
        super.onPause()
    }

    //Search tracks
    private fun search() {
        searchActivityViewModel.searchForTracks(binding.editTextInSearch.text.toString())
    }

    //Show error message you need
    enum class ViewToShow {
        SHOW_TRACKS_RV, SHOW_ERROR_LL, SHOW_SEARCH_HISTORY_LL, SHOW_PROGRESS_BAR
    }

    private fun showErrorMessage(errorType: SearchStatusResult) {
        val errorText: String
        var needBtn = false
        var imageSrc: Int = R.drawable.placeholder

        when (errorType) {
            SearchStatusResult.CONNECTION_ERROR -> {
                errorText = getString(R.string.connection_error)
                needBtn = true
                imageSrc = R.drawable.no_connection_image
            }

            SearchStatusResult.NOTING_FOUND_ERROR -> {
                errorText = getString(R.string.not_found)
                imageSrc = R.drawable.nothing_found_image
            }
        }
        if (errorText.isNotEmpty()) {
            clearAndNotifyTracksAdapter()

            binding.errorTv.text = errorText
            binding.errorIv.setImageResource(imageSrc)
            showViews(ViewToShow.SHOW_ERROR_LL)
            binding.clearBtn.visibility = View.GONE

            if (needBtn) {
                binding.updateBtn.visibility = View.VISIBLE
            }
        } else {
            showViews(ViewToShow.SHOW_TRACKS_RV)
        }
    }

    //RecyclerView click listener
    override fun setTrackClickListener(track: Track) {
        if (searchActivityViewModel.clickDebounce()) {
            clearAndNotifyHistoryAdapter()
            searchActivityViewModel.addToSearchHistory(track)
            openPlayer(track)
        }
    }

    //Open Player Activity
    private fun openPlayer(track: Track) {
        val intent = Intent(this, ActivityPlayer::class.java)
        intent.putExtra(TRACK_DTO_DATA, track)
        startActivity(intent)
    }

    //Check if history is empty
    private fun checkAndShowHistory() {
        showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
    }

    //Adapters
    private fun initAdapters() {
        tracksAdapter = TrackListAdapter(tracksList, this)
        binding.trackListRv.adapter = tracksAdapter

        historyAdapter = TrackListAdapter(historyList, this)
        binding.searchHistoryRv.adapter = historyAdapter
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
        binding.editTextInSearch.setText(saveInputText)
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
                errorLl.visibility = View.GONE
                searchHistoryLl.visibility = View.GONE
                searchProgressBar.visibility = View.GONE
                trackListRv.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_ERROR_LL -> {
                trackListRv.visibility = View.GONE
                searchHistoryLl.visibility = View.GONE
                searchProgressBar.visibility = View.GONE
                errorLl.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_SEARCH_HISTORY_LL -> {
                trackListRv.visibility = View.GONE
                errorLl.visibility = View.GONE
                searchProgressBar.visibility = View.GONE
                searchHistoryLl.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_PROGRESS_BAR -> {
                trackListRv.visibility = View.GONE
                errorLl.visibility = View.GONE
                searchHistoryLl.visibility = View.GONE
                searchProgressBar.visibility = View.VISIBLE
            }
        }
    }
}
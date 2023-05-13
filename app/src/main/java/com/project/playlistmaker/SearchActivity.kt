package com.project.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.project.playlistmaker.retrofit.ItunesSearchApi
import com.project.playlistmaker.retrofit.SongsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), TrackListViewHolder.TrackListClickListener {

    companion object {
        const val EDIT_TEXT_CONTENT = "PRODUCT_AMOUNT"
        const val SHARED_PREF_SEARCH = "search_preferences"
        const val SEARCH_HISTORY_KEY = "key_for_search_history_prefs"
    }

    //Retrofit related
    private val itunesBaseUrl = "http://itunes.apple.com"

    private var interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchApi = retrofit.create(ItunesSearchApi::class.java)

    //Views
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var errorImage: ImageView
    private lateinit var backBtn: ImageView
    private lateinit var searchHistoryLL: LinearLayout
    private lateinit var errorLL: LinearLayout
    private lateinit var errorTv: TextView
    private lateinit var clearHistoryBtn: Button
    private lateinit var updateBtn: Button
    private lateinit var searchHistoryRv: RecyclerView
    private lateinit var trackListRv: RecyclerView

    //Other
    private val trackDtoListArray = ArrayList<TrackDto>()
    private val historyList = ArrayList<TrackDto>()
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var tracksAdapter: TrackListAdapter
    private lateinit var historyAdapter: TrackListAdapter
    private val searchHistory = SearchHistory(historyList)
    var saveInputText: String? = null
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        saveInputText = savedInstanceState?.getString(EDIT_TEXT_CONTENT)

        sharedPrefs = getSharedPreferences(SHARED_PREF_SEARCH, MODE_PRIVATE)
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
            historyList.addAll(
                fromJsonToTracksArray(
                    sharedPrefs.getString(SEARCH_HISTORY_KEY, "")!!
                )
            )
        }

        bindViewsAndAdapters()
        checkAndShowHistory()

        //Listeners
        clearButton.setOnClickListener {
            searchEditText.setText("")
            clearButton.visibility = clearButtonVisibility("")
            searchEditText.hideKeyboard()
            trackDtoListArray.clear()
            tracksAdapter.notifyDataSetChanged()
            showViews(ViewToShow.SHOW_TRACKS_RV)
            checkAndShowHistory()
        }

        searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty() && historyList.isNotEmpty()) {
                showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
            } else {
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

        updateBtn.setOnClickListener {
            search()
        }

        clearHistoryBtn.setOnClickListener {
            historyList.clear()
            historyAdapter.notifyItemRangeChanged(0, historyList.size)
            searchHistoryLL.visibility = View.GONE
        }

        //TextWatcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                saveInputText = searchEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                showViews(ViewToShow.SHOW_TRACKS_RV)
            }
            false
        }
    }

    override fun onPause() {
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(historyList))
            .apply()
        super.onPause()
    }

    //EditText related
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText.setText(saveInputText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_CONTENT, saveInputText)
        super.onSaveInstanceState(outState)
    }

    //Search related
    private fun search() {
        itunesSearchApi.search(searchEditText.text.toString())
            .enqueue(object : Callback<SongsResponse> {
                override fun onResponse(
                    call: Call<SongsResponse>,
                    response: Response<SongsResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackDtoListArray.clear()
                                trackDtoListArray.addAll(response.body()?.results!!)
                                tracksAdapter.notifyDataSetChanged()
                                showViews(ViewToShow.SHOW_TRACKS_RV)
                            } else {
                                showMessage(NetworkStatus.NOTING_FOUND_ERROR)
                            }
                        }

                        else -> {
                            showMessage(NetworkStatus.NOTING_FOUND_ERROR)
                        }
                    }
                }

                override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                    showMessage(NetworkStatus.CONNECTION_ERROR)
                    Log.e("ServerError", t.message.toString())
                }
            })
    }

    private fun showMessage(errorType: NetworkStatus) {
        val errorText: String
        var needBtn = false
        var imageSrc: Int = R.drawable.placeholder

        when (errorType) {
            NetworkStatus.CONNECTION_ERROR -> {
                errorText = getString(R.string.connection_error)
                needBtn = true
                imageSrc = R.drawable.no_connection_image
            }

            NetworkStatus.NOTING_FOUND_ERROR -> {
                errorText = getString(R.string.not_found)
                imageSrc = R.drawable.nothing_found_image
            }
        }
        if (errorText.isNotEmpty()) {
            trackDtoListArray.clear()
            tracksAdapter.notifyDataSetChanged()

            errorTv.text = errorText
            errorImage.setImageResource(imageSrc)
            showViews(ViewToShow.SHOW_ERROR_LL)
            updateBtn.visibility = View.GONE

            if (needBtn) {
                updateBtn.visibility = View.VISIBLE
            }
        } else {
            showViews(ViewToShow.SHOW_TRACKS_RV)
        }
    }

    private fun showViews(view: ViewToShow) {
        when (view) {
            ViewToShow.SHOW_TRACKS_RV -> {
                errorLL.visibility = View.GONE
                searchHistoryLL.visibility = View.GONE
                trackListRv.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_ERROR_LL -> {
                trackListRv.visibility = View.GONE
                searchHistoryLL.visibility = View.GONE
                errorLL.visibility = View.VISIBLE
            }

            ViewToShow.SHOW_SEARCH_HISTORY_LL -> {
                trackListRv.visibility = View.GONE
                errorLL.visibility = View.GONE
                searchHistoryLL.visibility = View.VISIBLE
            }
        }
    }

    override fun setTrackClickListener(trackDto: TrackDto) {
        searchHistory.addTrackToHistory(trackDto)
        historyAdapter.notifyDataSetChanged()
    }

    private fun fromJsonToTracksArray(stringToConvert: String): Array<TrackDto> {
        return gson.fromJson(stringToConvert, Array<TrackDto>::class.java)
    }

    private fun checkAndShowHistory() {
        if (historyList.isNotEmpty()) {
            showViews(ViewToShow.SHOW_SEARCH_HISTORY_LL)
        }
    }

    enum class NetworkStatus {
        CONNECTION_ERROR, NOTING_FOUND_ERROR
    }

    enum class ViewToShow {
        SHOW_TRACKS_RV, SHOW_ERROR_LL, SHOW_SEARCH_HISTORY_LL
    }

    private fun bindViewsAndAdapters() {
        searchEditText = findViewById(R.id.edit_text_in_search)
        clearButton = findViewById(R.id.clear_btn)
        backBtn = findViewById(R.id.back_imageBtn_in_search)
        clearHistoryBtn = findViewById(R.id.clear_search_history_btn)
        errorLL = findViewById(R.id.error_ll)
        errorImage = findViewById(R.id.error_iv)
        errorTv = findViewById(R.id.error_tv)
        updateBtn = findViewById(R.id.update_btn)
        errorLL.visibility = View.GONE
        //Tracks
        trackListRv = findViewById(R.id.track_list_rv)
        tracksAdapter = TrackListAdapter(trackDtoListArray, this)
        trackListRv.adapter = tracksAdapter
        //History
        searchHistoryLL = findViewById(R.id.search_history_ll)
        searchHistoryRv = findViewById(R.id.search_history_rv)
        historyAdapter = TrackListAdapter(historyList, this)
        searchHistoryRv.adapter = historyAdapter
    }
}

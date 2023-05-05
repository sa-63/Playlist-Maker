package com.project.playlistmaker

import android.content.Context
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
    var saveInputText: String? = null

    companion object {
        const val EDIT_TEXT_CONTENT = "PRODUCT_AMOUNT"
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
    private val trackDtoListArray = ArrayList<TrackDto>()

    //Views
    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backBtn: ImageView
    private lateinit var errorLL: LinearLayout
    private lateinit var searchHistoryLL: LinearLayout
    private lateinit var errorImage: ImageView
    private lateinit var errorTv: TextView
    private lateinit var updateBtn: Button
    private lateinit var trackListRv: RecyclerView
    private lateinit var searchHistoryRv: RecyclerView
    private lateinit var tracksAdapter: TrackListAdapter

    private var historyList = ArrayList<TrackDto>()
    private lateinit var historyAdapter: TrackListAdapter
    private val searchHistory = SearchHistory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        saveInputText = savedInstanceState?.getString(EDIT_TEXT_CONTENT)

        //Binding
        searchEditText = findViewById(R.id.edit_text_in_search)
        clearButton = findViewById(R.id.clear_btn)
        backBtn = findViewById(R.id.back_imageBtn_in_search)
        errorLL = findViewById(R.id.error_ll)
        errorImage = findViewById(R.id.error_iv)
        errorTv = findViewById(R.id.error_tv)
        updateBtn = findViewById(R.id.update_btn)
        errorLL.visibility = View.GONE

        trackListRv = findViewById(R.id.track_list_rv)
        tracksAdapter = TrackListAdapter(trackDtoListArray,this)
        trackListRv.adapter = tracksAdapter

        searchHistoryLL = findViewById(R.id.search_history_ll)
        searchHistoryRv = findViewById(R.id.search_history_rv)
        historyAdapter = TrackListAdapter(historyList,this)
        searchHistoryRv.adapter = historyAdapter

        //Listeners
        clearButton.setOnClickListener {
            searchEditText.setText("")
            trackListRv.visibility = View.GONE
            clearButton.visibility = clearButtonVisibility("")
            searchEditText.hideKeyboard()

            trackListRv.visibility = View.GONE
            searchHistoryLL.visibility = View.VISIBLE
            historyList.clear()
            historyList.addAll(searchHistory.getTrackHistory())
            historyList.reverse()
            historyAdapter.notifyItemRangeChanged(0, 10)
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

        updateBtn.setOnClickListener {
            search()
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
            }
            false
        }
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
                                tracksAdapter.notifyItemRangeChanged(0, trackDtoListArray.size)
                                hideRecyclerView(false)
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

    private fun showMessage(
        errorType: NetworkStatus
    ) {
       val errorText: String
       var needBtn = false
       var imageSrc: Int = R.drawable.placeholder

        when(errorType) {
            NetworkStatus.CONNECTION_ERROR -> {
                errorText = getString(R.string.connection_error)
                needBtn = true
                imageSrc = R.drawable.no_connection_image
            }
            NetworkStatus.NOTING_FOUND_ERROR -> {
                errorText = getString(R.string.not_found)
                imageSrc =  R.drawable.nothing_found_image
            }
        }
        if (errorText.isNotEmpty()) {
            trackDtoListArray.clear()
            tracksAdapter.notifyItemRangeChanged(0, trackDtoListArray.size)

            errorTv.text = errorText
            errorImage.setImageResource(imageSrc)
            hideRecyclerView(true)
            updateBtn.visibility = View.GONE

            if (needBtn) {
                updateBtn.visibility = View.VISIBLE
            }
        } else {
            hideRecyclerView(false)
        }
    }

    enum class NetworkStatus {
        CONNECTION_ERROR, NOTING_FOUND_ERROR
    }

    private fun hideRecyclerView(hide: Boolean) {
        if (hide) {
            trackListRv.visibility = View.GONE
            errorLL.visibility = View.VISIBLE
        } else {
            trackListRv.visibility = View.VISIBLE
            errorLL.visibility = View.GONE
        }
    }

    override fun setTrackClickListener(trackDto: TrackDto) {
        searchHistory.addTrackToHistory(trackDto)
    }
}

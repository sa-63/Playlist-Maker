package com.project.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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

class SearchActivity : AppCompatActivity() {
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
    private lateinit var errorImage: ImageView
    private lateinit var errorTv: TextView
    private lateinit var updateBtn: Button

    private lateinit var trackListRv: RecyclerView
    private lateinit var tracksAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        saveInputText = savedInstanceState?.getString(EDIT_TEXT_CONTENT)

//        trackListRv = findViewById(R.id.track_list_rv)

        searchEditText = findViewById(R.id.edit_text_in_search)
        clearButton = findViewById(R.id.clear_btn)
        backBtn = findViewById(R.id.back_imageBtn_in_search)
        errorLL = findViewById(R.id.error_ll)
        errorImage = findViewById(R.id.error_image)
        errorTv = findViewById(R.id.error_tv)
        updateBtn = findViewById(R.id.update_btn)
        errorLL.visibility = View.GONE

//        tracksAdapter = TrackListAdapter(trackDtoListArray)
//        trackListRv.adapter = tracksAdapter

        clearButton.setOnClickListener {
            searchEditText.setText("")
            clearButton.visibility = clearButtonVisibility("")
            searchEditText.hideKeyboard()
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }

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
//                                tracksAdapter.notifyDataSetChanged()
                                showMessage("", "", 0)
                            } else {
                                showMessage(
                                    getString(R.string.not_found), "",
                                    R.drawable.nothing_found_image
                                )
                                updateBtn.visibility = View.GONE
                            }
                        }
                        else -> {
                            showMessage(
                                getString(R.string.not_found),
                                response.code().toString(),
                                R.drawable.nothing_found_image
                            )
                            updateBtn.visibility = View.GONE
                        }
                    }

                }

                override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                    showMessage(
                        getString(R.string.connection_error),
                        t.message.toString(),
                        R.drawable.no_connection_image
                    )
                    updateBtn.visibility = View.VISIBLE
                }
            })
    }

    private fun showMessage(text: String, additionalMessage: String, imageViewRes: Int) {
        if (text.isNotEmpty()) {
            showPlaceholderError(text, true, imageViewRes)
            trackDtoListArray.clear()
//            tracksAdapter.notifyDataSetChanged()
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            showPlaceholderError(text, false, imageViewRes)
        }
    }

    private fun showPlaceholderError(errorText: String, show: Boolean, imageViewRes: Int) {
        if (show) {
            errorTv.text = errorText
            errorImage.setImageResource(imageViewRes)
//            trackListRv.isVisible = false
            errorLL.isVisible = true
        } else {
//            trackListRv.isVisible = true
            errorLL.isVisible = false
        }
    }
}
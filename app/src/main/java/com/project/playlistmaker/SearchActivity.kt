package com.project.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    var saveInputText: String? = null
    private val trackDtoListArrays: ArrayList<TrackDto> = arrayListOf(
        TrackDto(
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),
        TrackDto(
            "Billie Jean",
            "Michael Jackson",
            "4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),
        TrackDto(
            "Stayin' Alive",
            "Bee Gees",
            "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),
        TrackDto(
            "Whole Lotta Love",
            "Led Zeppelin",
            "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),
        TrackDto(
            "Sweet Child O'Mine",
            "Guns N' Roses",
            "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        ),
    )

    companion object {
        const val EDIT_TEXT_CONTENT = "PRODUCT_AMOUNT"
    }

    private lateinit var searchEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backBtn: ImageView
    private val tracksAdapter = TrackListAdapter(trackDtoListArrays)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        saveInputText = savedInstanceState?.getString(EDIT_TEXT_CONTENT)

        searchEditText = findViewById(R.id.edit_text_in_search)
        clearButton = findViewById(R.id.clear_btn)
        backBtn = findViewById(R.id.back_imageBtn_in_search)

        val trackListRv: RecyclerView = findViewById(R.id.track_list_rv)
        trackListRv.adapter = tracksAdapter

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
}
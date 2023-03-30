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

class SearchActivity : AppCompatActivity() {
    var saveInputText: String? = null

    companion object {
        const val EDIT_TEXT_CONTENT = "PRODUCT_AMOUNT"
    }

    private var searchEditText: EditText? = null
    private var clearButton: ImageView? = null
    private var backBtn: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        saveInputText = savedInstanceState?.getString(EDIT_TEXT_CONTENT)

        searchEditText = findViewById(R.id.edit_text_in_search)
        clearButton = findViewById(R.id.clear_btn)
        backBtn = findViewById(R.id.back_imageBtn_in_search)

        clearButton?.setOnClickListener {
            searchEditText?.setText("")
            clearButton?.visibility = clearButtonVisibility("")
            searchEditText?.hideKeyboard()
        }

        backBtn?.setOnClickListener {
            onBackPressed()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton?.visibility = clearButtonVisibility(s)
                saveInputText = searchEditText?.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText?.addTextChangedListener(simpleTextWatcher)
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
        searchEditText?.setText(saveInputText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_CONTENT, saveInputText)
        super.onSaveInstanceState(outState)
    }
}


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

var saveInputText: String? = null

class SearchActivity : AppCompatActivity() {
    companion object {
        const val EDIT_TEXT_CONTENT = "PRODUCT_AMOUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchEditText = findViewById<EditText>(R.id.edit_text_in_search)
        val clearButton = findViewById<ImageView>(R.id.clear_btn)
        val backBtn = findViewById<ImageView>(R.id.back_imageBtn_in_search)

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
        val searchEditText = findViewById<EditText>(R.id.edit_text_in_search)
        searchEditText?.setText(saveInputText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EDIT_TEXT_CONTENT, saveInputText)
        super.onSaveInstanceState(outState)
    }
}


package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import android.widget.EditText

class SearchActivity : AppCompatActivity() {

    private lateinit var clearButton: MaterialButton
    private lateinit var inputEditText: EditText
    private var searchQuery: String = ""

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val returnButton = findViewById<MaterialToolbar>(R.id.searchToolbar)
            clearButton = findViewById(R.id.search_close_button)
            inputEditText = findViewById(R.id.editTextFrame)

                returnButton.setOnClickListener {
                    finish()
                }

            searchQuery = savedInstanceState?.getString(SEARCH_QUERY_KEY, "") ?: ""
            inputEditText.setText(searchQuery)

            inputEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    searchQuery = s.toString()
                    clearButton.isVisible = !s.isNullOrEmpty()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            clearButton.setOnClickListener {
                inputEditText.text.clear()
                clearButton.isVisible = false
                searchQuery = ""
                hideKeyboard()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, searchQuery)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }
}
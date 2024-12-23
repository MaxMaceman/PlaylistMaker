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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var clearButton: MaterialButton
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private var tracksList: ArrayList<Track> = arrayListOf()
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
        recyclerView = findViewById(R.id.search_tracksRecycle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(tracksList) { track ->
        }
        recyclerView.adapter = trackAdapter

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

        populateTrackList()
        trackAdapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, searchQuery)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
    }

    private fun populateTrackList() {
        tracksList.add(Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"))
        tracksList.add(Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"))
        tracksList.add(Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"))
        tracksList.add(Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"))
    }
}
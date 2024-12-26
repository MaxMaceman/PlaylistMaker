package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SearchActivity : AppCompatActivity() {

    private lateinit var clearButton: MaterialButton
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private val tracksList = mutableListOf<Track>()
    private val historyList = mutableListOf<Track>()
    private lateinit var searchHistory: SearchHistory

    private var searchQuery: String = ""
    private var lastFailedQuery: String? = null
    interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferences: SharedPreferences = getSharedPreferences("search_prefs", MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        var historyView = findViewById<LinearLayout>(R.id.search_history)
        val returnButton = findViewById<MaterialToolbar>(R.id.searchToolbar)
        clearButton = findViewById(R.id.search_close_button)
        inputEditText = findViewById(R.id.editTextFrame)
        recyclerView = findViewById(R.id.search_tracksRecycle)
        historyRecyclerView = findViewById(R.id.history_tracksRecycle)

        recyclerView = findViewById(R.id.search_tracksRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(tracksList) { track ->
            tracksList.addAll(searchHistory.getSearchHistory())
            searchHistory.addSong(track)
            trackAdapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()

        }
        recyclerView.adapter = trackAdapter
        historyRecyclerView = findViewById(R.id.history_tracksRecycle)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        historyAdapter = TrackAdapter(historyList) { track ->
            doSearch(track.trackName)
        }
        historyRecyclerView.adapter = historyAdapter

        returnButton.setOnClickListener {
            finish()
        }

        searchQuery = savedInstanceState?.getString(SEARCH_QUERY_KEY, "") ?: ""
        inputEditText.setText(searchQuery)
        inputEditText.setOnFocusChangeListener { view, b -> displaySearchHistory() }

        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                inputEditText.setOnFocusChangeListener { view, hasFocus ->
                    if (hasFocus && inputEditText.text.isEmpty()) {
                        displaySearchHistory()
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()
                clearButton.visibility = if (s.isNullOrEmpty())  View.GONE  else View.VISIBLE
                historyView.visibility = if (s.isNullOrEmpty() != true )  View.GONE  else View.VISIBLE

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            clearButton.visibility = View.GONE
            searchQuery = ""
            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
            recyclerView.visibility = View.GONE
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doSearch(inputEditText.text.toString())
                true
            } else {
                false
            }
        }

        findViewById<MaterialButton>(R.id.clear_history_button).setOnClickListener {
            searchHistory.clearHistory()
            historyList.clear()
            historyAdapter.notifyDataSetChanged()
            findViewById<View>(R.id.search_history).visibility = View.GONE
        }

        findViewById<MaterialButton>(R.id.splashscreenNothingBtn).setOnClickListener {
            lastFailedQuery?.let { query ->
                doSearch(query)
            }
        }
    }

    companion object {
        const val SEARCH_QUERY_KEY = "search_query"
        const val QUERY_DEFAULT = ""
    }

    private fun doSearch(userInput: String) {
        if (userInput.isNotEmpty()) {
            lastFailedQuery = userInput
            RetrofitClient.iTunesService.search(userInput)
                .enqueue(object : Callback<SearchResult> {
                    override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                        if (response.isSuccessful) {
                            tracksList.clear()
                            response.body()?.results?.let { results ->
                                if (results.isNotEmpty()) {
                                    tracksList.addAll(results)
                                    trackAdapter.notifyDataSetChanged()
                                    historyAdapter.notifyDataSetChanged()
                                    findViewById<View>(R.id.splashscreenNothingFound).visibility = View.GONE
                                    findViewById<View>(R.id.splashscreenNetworkError).visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                } else {
                                    showNothingFoundMessage()
                                }
                            }
                        } else {
                            showNothingFoundMessage()
                        }
                    }
                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        showNetworkError()
                    }
                })
        } else {
            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
            recyclerView.visibility = View.GONE
            findViewById<View>(R.id.splashscreenNothingFound).visibility = View.GONE
        }
    }

    private fun displaySearchHistory() {
        historyList.clear()
        historyList.addAll(searchHistory.getSearchHistory())
        historyAdapter.notifyDataSetChanged()
        findViewById<View>(R.id.search_history).visibility = if (historyList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun showNothingFoundMessage() {
        findViewById<View>(R.id.splashscreenNothingFound).visibility = View.VISIBLE
        findViewById<View>(R.id.splashscreenNetworkError).visibility = View.GONE
    }

    private fun showNetworkError() {
        findViewById<View>(R.id.splashscreenNothingFound).visibility = View.GONE
        findViewById<View>(R.id.splashscreenNetworkError).visibility = View.VISIBLE
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


package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.inputmethod.EditorInfo


class SearchActivity : AppCompatActivity() {

    private lateinit var clearButton: MaterialButton
    private lateinit var inputEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private var tracksList: ArrayList<Track> = ArrayList()
    private var searchQuery: String = ""
    private var lastFailedQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val returnButton = findViewById<MaterialToolbar>(R.id.searchToolbar)
        clearButton = findViewById(R.id.search_close_button)
        inputEditText = findViewById(R.id.editTextFrame)
        recyclerView = findViewById(R.id.search_tracksRecycle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(tracksList) { track -> }
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
                doSearch(searchQuery)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearButton.setOnClickListener {
            inputEditText.text.clear()
            clearButton.isVisible = false
            searchQuery = ""
            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
            recyclerView.visibility = View.GONE
            findViewById<View>(R.id.splashscreenNothingFound).visibility = View.GONE
            hideKeyboard()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doSearch(inputEditText.text.toString())
                true
            } else {
                false
            }
        }

        findViewById<MaterialButton>(R.id.splashscreenNothingBtn).setOnClickListener {
            lastFailedQuery?.let { query ->
                doSearch(query)
            }
        }

        trackAdapter.notifyDataSetChanged()
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
                    override fun onResponse(
                        call: Call<SearchResult>,
                        response: Response<SearchResult>
                    ) {
                        if (response.isSuccessful) {
                            tracksList.clear()
                            response.body()?.results?.let {
                                if (it.isNotEmpty()) {
                                    tracksList.addAll(it)
                                    trackAdapter.notifyDataSetChanged()
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
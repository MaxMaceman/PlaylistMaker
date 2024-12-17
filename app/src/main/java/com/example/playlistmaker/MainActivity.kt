package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.search_btn)
        val media = findViewById<Button>(R.id.media_btn)
        val settings = findViewById<Button>(R.id.settings_btn)

        search.setOnClickListener {
            val settingsIntent = Intent(this, SearchActivity::class.java)
            startActivity(settingsIntent)
        }

        media.setOnClickListener {
            val settingsIntent = Intent(this, MediaActivity::class.java)
            startActivity(settingsIntent)
        }

        settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}
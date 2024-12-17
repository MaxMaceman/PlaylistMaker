package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search_button = findViewById<Button>(R.id.search_btn)
        val media_button = findViewById<Button>(R.id.media_btn)
        val settings_button = findViewById<Button>(R.id.settings_btn)

        media_button.setOnClickListener {
        }

        settings_button.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {

            override fun onClick(v: View?) {
            }
        }

        search_button.setOnClickListener(buttonClickListener)
    }
}
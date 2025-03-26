package com.example.playlistmaker.ui.settings

import android.net.Uri
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HandlerInteractor
import com.example.playlistmaker.domain.api.ThemeInteractor
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeInteractor: ThemeInteractor
    private lateinit var handlerInteractor: HandlerInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        themeInteractor = Creator.themeInteractorDo()
        handlerInteractor = Creator.handlerInteractorDo()

        val returnButton = findViewById<MaterialToolbar>(R.id.back)
        val themeSwitch = findViewById<Switch>(R.id.dayNightSwitcher)
        val shareButton = findViewById<TextView>(R.id.share)
        val supportButton = findViewById<TextView>(R.id.support)
        val agreementButton = findViewById<TextView>(R.id.user_agreement)

        returnButton.setOnClickListener {
            finish()
        }

        themeSwitch.isChecked = themeInteractor.getCurrentTheme()
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            themeInteractor.switchTheme(isChecked)
        }

        shareButton.setOnClickListener {
            handlerInteractor.shareApp()
        }

        supportButton.setOnClickListener {
            handlerInteractor.writeSupport()
        }

        agreementButton.setOnClickListener {
            handlerInteractor.agreement()
        }
    }
}
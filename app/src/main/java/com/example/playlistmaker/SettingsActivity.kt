package com.example.playlistmaker

import android.net.Uri
import android.view.View
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.content.Intent
import android.widget.ImageButton
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.switchmaterial.SwitchMaterial

private lateinit var themeSwitch: SwitchMaterial
private lateinit var sharedPreferences: SharedPreferences

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val return_button = findViewById<MaterialToolbar>(R.id.back)

            return_button.setOnClickListener {
                finish()
            }

        sharedPreferences = getPreferences(MODE_PRIVATE)
        themeSwitch = findViewById(R.id.dayNightSwitcher)

        themeSwitch.isChecked = sharedPreferences.getBoolean("isDarkTheme", false)

        themeSwitch.setOnCheckedChangeListener { _, isChacked ->
            if (isChacked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            with(sharedPreferences.edit()) {
                putBoolean("isDarkTheme", isChacked)
                apply()
            }

        }

        val share_button = findViewById<MaterialTextView>(R.id.share)

            share_button.setOnClickListener {

                val shareText = "https://practicum.yandex.ru/profile/android-developer/"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }

                val chooser = Intent.createChooser(sendIntent, "Поделиться с помощью:")
                startActivity(chooser)
        }

        val support_button = findViewById<MaterialTextView>(R.id.support)

        support_button.setOnClickListener {
            val email = "maseman.sa@yandex.ru" // Убедитесь, что это правильный адрес
            val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val body = "Спасибо разработчикам и разработчицам за крутое приложение!"

            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:") // только для почты
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(emailIntent)
        }

        val agreement_button = findViewById<MaterialTextView>(R.id.user_agreement)
            agreement_button.setOnClickListener {
                val url = "https://yandex.ru/legal/practicum_offer/"
                val agreementIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url)
                }
                startActivity(agreementIntent)
            }
    }
}
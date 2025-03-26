package com.example.playlistmaker.data

import android.app.Application
import android.net.Uri
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HandlerRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.switchmaterial.SwitchMaterial

class HandlerRepositoryImpl(private val application: Application) : HandlerRepository {
    override fun shareApp() {
        val shareThisApp = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, application.getString(R.string.share_link))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(shareThisApp)
    }

    override fun writeSupport() {
        val writeToSupport = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(application.getString(R.string.support_message_email)))
            putExtra(Intent.EXTRA_SUBJECT, application.getString(R.string.support_message_subject))
            putExtra(Intent.EXTRA_TEXT, application.getString(R.string.support_message_body))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(writeToSupport)
    }

    override fun agreement() {
        val showAgreement = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(application.getString(R.string.agreement_link))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.startActivity(showAgreement)
    }
}

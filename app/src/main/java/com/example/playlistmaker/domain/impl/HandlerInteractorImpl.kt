package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HandlerInteractor
import com.example.playlistmaker.domain.api.HandlerRepository

class HandlerInteractorImpl(private val action: HandlerRepository): HandlerInteractor {
    override fun shareApp() {
        action.shareApp()
    }

    override fun writeSupport() {
        action.writeSupport()
    }

    override fun agreement() {
        action.agreement()
    }
}
package com.project.playlistmaker.sharing.domain.repository

import com.project.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(shareAppLink:String)
    fun openLink(termsLink:String)
    fun openEmail(supportEmailData: EmailData)
    fun share(info: String)
}
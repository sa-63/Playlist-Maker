package com.project.playlistmaker.searchscreen.data.localstorage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.project.playlistmaker.searchscreen.domain.models.Track

class SearchHistoryStorageImpl(
    private val gson: Gson,
    private val sharedPref: SharedPreferences
) : SearchHistoryStorage {

    override fun addHistoryToLocalDb(historyList: ArrayList<Track>) {
        sharedPref.edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(historyList))
            .apply()
    }

    override fun getHistoryFromLocalDb(): ArrayList<Track> {
        if (sharedPref.contains(SEARCH_HISTORY_KEY)) {
            return fromJsonToTracksArray(
                sharedPref.getString(SEARCH_HISTORY_KEY, "")!!
            ).toCollection(ArrayList())
        }
        return arrayListOf()
    }

    override fun clearHistoryForLocalDb() {
        sharedPref.edit().clear().apply()
    }

    private fun fromJsonToTracksArray(stringToConvert: String): Array<Track> {
        return gson.fromJson(stringToConvert, Array<Track>::class.java)
    }

    companion object {
        const val SHARED_PREF_SEARCH = "search_preferences"
        const val SEARCH_HISTORY_KEY = "key_for_search_history_prefs"
    }
}
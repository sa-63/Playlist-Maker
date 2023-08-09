package com.project.playlistmaker.search_screen.data.local_storage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.project.playlistmaker.search_screen.domain.models.Track

class SearchHistoryStorageImpl(private val context: Context) : SearchHistoryStorage {
    companion object {
        const val SHARED_PREF_SEARCH = "search_preferences"
        const val SEARCH_HISTORY_KEY = "key_for_search_history_prefs"
    }

    private val gson = Gson()
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_SEARCH, AppCompatActivity.MODE_PRIVATE)

    override fun addHistoryToLocalDb(historyList: ArrayList<Track>) {
        sharedPrefs.edit()
            .putString(SEARCH_HISTORY_KEY, gson.toJson(historyList))
            .apply()
    }

    override fun getHistoryFromLocalDb(): ArrayList<Track> {
        if (sharedPrefs.contains(SEARCH_HISTORY_KEY)) {
                return fromJsonToTracksArray(
                    sharedPrefs.getString(SEARCH_HISTORY_KEY, "")!!
                ).toCollection(ArrayList())
        }
        return arrayListOf()
    }

    override fun clearHistoryForLocalDb() {
        sharedPrefs.edit().clear().apply()
    }

    private fun fromJsonToTracksArray(stringToConvert: String): Array<Track> {
        return gson.fromJson(stringToConvert, Array<Track>::class.java)
    }
}
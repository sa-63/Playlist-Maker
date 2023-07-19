package com.project.playlistmaker.presentation.ui.activity_player

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.creator.Creator
import com.project.playlistmaker.domain.models.Track
import com.project.playlistmaker.presentation.ui.activity_player.play_back.PlayBackControl
import com.project.playlistmaker.domain.use_cases.utilities.DataFormat
import com.project.playlistmaker.presentation.ui.activity_player.updateDurationView.UpdateDurationTaskImpl

class ActivityPlayer : AppCompatActivity() {

    companion object {
        const val TRACK_DTO_DATA = "track_dto_data"
        const val SEPARATE_DATE_STR_YEAR = 4
    }

    //Views
    private lateinit var backBtn: ImageButton
    private lateinit var addToPlaylistBtn: ImageButton
    private lateinit var playPauseBtn: ImageButton
    private lateinit var addToFavBtn: ImageButton
    private lateinit var albumCover: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var durationTotalTv: TextView
    private lateinit var albumTv: TextView
    private lateinit var yearTv: TextView
    private lateinit var genreTv: TextView
    private lateinit var countryTv: TextView
    private lateinit var currentDuration: TextView

    //Other
    private var cornerSize: Int = 0
    private lateinit var track: Track
    private val dataFormat = DataFormat()
    private val playerInterectorImpl = Creator.providePlayerRepository()
    private lateinit var playBackControl: PlayBackControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        bindViews()

        track = intent.getSerializableExtra(TRACK_DTO_DATA) as Track

        playBackControl()

        fillContent()

        backBtn.setOnClickListener {
            onBackPressed()
        }

        playPauseBtn.setOnClickListener {
            playBackControl.playbackControl()
        }
    }

    override fun onDestroy() {
        playerInterectorImpl.releasePlayer()
        super.onDestroy()
    }

    override fun onPause() {
        playerInterectorImpl.pausePlayer()
        super.onPause()
    }

    private fun fillContent() {
        Glide.with(this)
            .load(track.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(cornerSize))
            .placeholder(R.drawable.placeholder)
            .into(albumCover)
        trackName.text = track.trackName
        artistName.text = track.artistName
        durationTotalTv.text = dataFormat.convertTimeToMnSs(track.trackTimeMillis)

        albumTv.text = track.collectionName
        yearTv.text = track.releaseDate.substring(0, SEPARATE_DATE_STR_YEAR)
        genreTv.text = track.primaryGenreName
        countryTv.text = track.country
    }

    private fun playBackControl() {
        playBackControl = PlayBackControl(
            playerInterectorImpl,
            UpdateDurationTaskImpl(
                playerInterectorImpl,
                currentDuration,
                this
            ),
            playPauseBtn,
            track.previewUrl
        )
    }

    //Binding
    private fun bindViews() {
        backBtn = findViewById(R.id.back_btn_player)
        addToPlaylistBtn = findViewById(R.id.add_to_playlist_btn)
        playPauseBtn = findViewById(R.id.play_pause_btn)
        addToFavBtn = findViewById(R.id.add_to_fav_btn)
        albumCover = findViewById(R.id.album_cover_iv)
        trackName = findViewById(R.id.track_name_in_player_tv)
        artistName = findViewById(R.id.artist_name_tv)
        durationTotalTv = findViewById(R.id.duration_total_tv)
        albumTv = findViewById(R.id.album_in_table_tv)
        yearTv = findViewById(R.id.year_in_table_tv)
        genreTv = findViewById(R.id.genre_in_table_tv)
        countryTv = findViewById(R.id.country_in_table_tv)
        currentDuration = findViewById(R.id.track_duration_current)
        cornerSize = this.resources.getDimensionPixelSize(R.dimen.cover_corner_size)
    }
}
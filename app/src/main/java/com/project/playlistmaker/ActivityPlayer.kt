package com.project.playlistmaker

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class ActivityPlayer : AppCompatActivity() {

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

    private lateinit var trackDto: TrackDto
    private var cornerSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        trackDto = intent.getSerializableExtra(SearchActivity.TRACK_DTO_DATA) as TrackDto
        bindViews()
        fillContent()

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fillContent() {
        Glide.with(this)
            .load(trackDto.getCoverArtwork())
            .fitCenter()
            .transform(RoundedCorners(cornerSize))
            .placeholder(R.drawable.placeholder)
            .into(albumCover)
        trackName.text = trackDto.trackName
        artistName.text = trackDto.artistName
        durationTotalTv.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(trackDto.trackTimeMillis)
        albumTv.text = trackDto.collectionName
        yearTv.text = trackDto.releaseDate.substring(0, 4)
        genreTv.text = trackDto.primaryGenreName
        countryTv.text = trackDto.country
    }

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
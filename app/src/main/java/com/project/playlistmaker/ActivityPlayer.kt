package com.project.playlistmaker

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale


class ActivityPlayer : AppCompatActivity() {

    companion object {
        const val TRACK_DTO_DATA = "track_dto_data"
        const val SEPARATE_DATE_STR_YEAR = 4
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val UPDATE_DURATION_TIME = 1000L
    }

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
    private var mainHandler: Handler = Handler(Looper.getMainLooper())
    val createDurationCountTask: Runnable = createDurationCountTask()

    //MediaPlayer
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var currentTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        trackDto = intent.getSerializableExtra(TRACK_DTO_DATA) as TrackDto
        bindViews()
        fillContent()
        preparePlayer()

        backBtn.setOnClickListener {
            onBackPressed()
        }

        playPauseBtn.setOnClickListener {
            playbackControl()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
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
        yearTv.text = trackDto.releaseDate.substring(0, SEPARATE_DATE_STR_YEAR)
        genreTv.text = trackDto.primaryGenreName
        countryTv.text = trackDto.country
    }

    //MediaPlayer
    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackDto.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playPauseBtn.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playPauseBtn.setImageResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mainHandler.post(
            createDurationCountTask()
        )
        playPauseBtn.setImageResource(R.drawable.pause_btn)
        playerState = STATE_PLAYING
        mediaPlayer.start()
    }

    private fun pausePlayer() {
        mainHandler.removeCallbacks(createDurationCountTask)
        mainHandler.removeCallbacksAndMessages(null)
        playPauseBtn.setImageResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        mediaPlayer.pause()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun createDurationCountTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val remainingTime = (mediaPlayer.duration - mediaPlayer.currentPosition) / 1000
                if (remainingTime > 0) {
                    currentDuration.text =
                        String.format("%d:%02d", remainingTime / 60, remainingTime % 60)
                    mainHandler.postDelayed(this, UPDATE_DURATION_TIME)
                } else {
                    currentDuration.text = getString(R.string.no_time)
                }
            }
        }
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
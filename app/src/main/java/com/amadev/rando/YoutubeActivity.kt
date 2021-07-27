package com.amadev.rando

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


class YoutubeActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val intent = intent
        val videoID = intent.getStringExtra("videoId")
        val ytPlayer = findViewById<YouTubePlayerView>(R.id.youtube_player)

        initializeYouTubePlayer(ytPlayer, videoID)
    }

    private fun initializeYouTubePlayer(ytPlayer : YouTubePlayerView, videoID : String?) {
        ytPlayer.initialize(BuildConfig.YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                player?.setFullscreen(true)
                player?.setShowFullscreenButton(true)
                player?.loadVideo(videoID)
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@YoutubeActivity, "Failed to play video", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
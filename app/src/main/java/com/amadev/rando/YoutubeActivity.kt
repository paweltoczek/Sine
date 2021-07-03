package com.amadev.rando

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView


class YoutubeActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val intent = intent
        val VIDEO_ID = intent.getStringExtra("videoId")

        val ytPlayer = findViewById<YouTubePlayerView>(R.id.youtube_player)

        ytPlayer.initialize(BuildConfig.YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                player?.setFullscreen(true)
                player?.setShowFullscreenButton(true)
                player?.loadVideo(VIDEO_ID)
                player?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(this@YoutubeActivity, "Failed to play video", Toast.LENGTH_SHORT)
                    .show()

                Log.e("yterror", p1.toString())
                Log.e("yterror", p0.toString())
            }

        })

    }
}
package com.amadev.rando

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class YoutubeActivity : YouTubeBaseActivity() {
    val VIDEO_ID = "mYfJxlgR2jw"
    val YOUTUBE_API_KEY = "AIzaSyBjo1tRnyVdZjqJAYGwxqqBXRFH_jvNzH4"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        val ytPlayer = findViewById<YouTubePlayerView>(R.id.youtube_player)

        ytPlayer.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
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
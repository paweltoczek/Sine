package com.amadev.rando

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class YouTubeFragment : Fragment() {

    val VIDEO_ID = "mYfJxlgR2jw"
    val YOUTUBE_API_KEY = "AIzaSyBjo1tRnyVdZjqJAYGwxqqBXRFH_jvNzH4"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_you_tube, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val ytPlayer = R.id.youtube_player
//
//        ytPlayer.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
//            override fun onInitializationSuccess(
//                provider: YouTubePlayer.Provider?,
//                player: YouTubePlayer?,
//                p2: Boolean
//            ) {
//                player?.loadVideo(VIDEO_ID)
//                player?.play()
//            }
//
//            override fun onInitializationFailure(
//                p0: YouTubePlayer.Provider?,
//                p1: YouTubeInitializationResult?
//            ) {
//                Toast.makeText(this@YoutubeActivity, "Failed to play video", Toast.LENGTH_SHORT)
//                    .show()
//
//                Log.e("yterror", p1.toString())
//                Log.e("yterror", p0.toString())
//            }
//
//        })
    }

}
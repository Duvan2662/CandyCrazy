package com.example.candy

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.candy.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mediaPlayer = MediaPlayer.create(this, R.raw.beats)
        mediaPlayer.isLooping = true//se repite una vez finalice
        mediaPlayer.setVolume(0.3f, 0.3f)



        binding.jugar.setOnClickListener {
            nivel()
        }
    }
    fun nivel(){
        var intent = Intent(this, Nivel::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

}
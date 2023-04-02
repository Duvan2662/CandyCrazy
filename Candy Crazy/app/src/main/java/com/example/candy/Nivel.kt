package com.example.candy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.candy.databinding.ActivityNivelBinding

class Nivel : AppCompatActivity() {
    private lateinit var binding: ActivityNivelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivel)

        binding = ActivityNivelBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.nivel1.setOnClickListener {
            uno()
        }
        binding.nivel2.setOnClickListener {
            dos()
        }


    }
    fun uno(){
        var intent = Intent(this, Uno::class.java)
        startActivity(intent)
    }

    fun dos(){
        var intent = Intent(this, Dos::class.java)
        startActivity(intent)
    }


}
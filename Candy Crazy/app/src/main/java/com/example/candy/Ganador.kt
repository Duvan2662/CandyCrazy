package com.example.candy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.candy.databinding.ActivityGanadorBinding

class Ganador : AppCompatActivity() {
    private lateinit var binding:ActivityGanadorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityGanadorBinding.inflate(layoutInflater)
        setContentView(binding.root)




        cargarDatos();
        binding.inicio.setOnClickListener {
            inicio()
        }

    }

    private fun cargarDatos(){
        val imageResource = resources.getIdentifier("ganador","drawable",packageName)
        binding.imagen.setImageResource(imageResource)
        binding.puntos.text = "Puntos: " + intent.getIntExtra("puntos",0)
    }
    private fun inicio(){
        var intent = Intent(this, Nivel::class.java)
        startActivity(intent)
    }
}
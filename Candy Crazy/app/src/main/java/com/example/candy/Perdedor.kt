package com.example.candy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.candy.databinding.ActivityPerdedorBinding

class Perdedor : AppCompatActivity() {
    private lateinit var binding:ActivityPerdedorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerdedorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        cargar()
        binding.inicio.setOnClickListener {
            inicio()
        }

    }
    //Carga los datos de la activity Perdedor
    fun cargar(){
        val imageResource = resources.getIdentifier("perdedor","drawable",packageName)//Imagen del ganador
        binding.imagen.setImageResource(imageResource)//Coloca la imagen en la imagen del layout
        binding.puntos.text = "Puntos obtenidos: " + intent.getIntExtra("puntos",0)//Recibe los puntos del juego anterior y los coloca
    }
    //Vuelve al inicio
    fun inicio(){
        var intent = Intent(this, Nivel::class.java)
        startActivity(intent)
    }
}
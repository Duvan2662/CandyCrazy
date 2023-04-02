package com.example.candy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.candy.databinding.ActivityGanador2Binding

class Ganador2 : AppCompatActivity() {
    private lateinit var binding:ActivityGanador2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGanador2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        cargarDatos();
        binding.pasar.setOnClickListener {
            siguiente()
        }
        binding.inicio.setOnClickListener {
            inicio()
        }
    }

    //Carga los datos de la activity Ganador
    private fun cargarDatos(){
        val imageResource = resources.getIdentifier("ganador","drawable",packageName)//Imagen del ganador
        binding.imagen.setImageResource(imageResource)//Coloca la imagen en la imagen del layout
        binding.puntos.text = "Puntos: " + intent.getIntExtra("puntos",0)//Recibe los puntos del juego anterior y los coloca
    }
    //Inicia el nivel 2
    private fun siguiente(){
        var intent = Intent(this, Dos::class.java)
        startActivity(intent)
    }
    //Vuelve al inicio
    private fun inicio(){//Vuelve al inicio
        var intent = Intent(this, Nivel::class.java)
        startActivity(intent)
    }
}
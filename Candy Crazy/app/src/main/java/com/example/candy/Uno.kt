package com.example.candy


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TableRow
import androidx.appcompat.app.AlertDialog
import com.example.candy.databinding.ActivityUnoBinding
import java.util.concurrent.TimeUnit


class Uno : AppCompatActivity() {
    private lateinit var binding: ActivityUnoBinding

    private var puntos: Int = 0
    private var posicionActual: Pair<Int, Int>? = null
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var  soundEffect: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uno)
        binding = ActivityUnoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        soundEffect = MediaPlayer.create(this, R.raw.mario)

        val tablero = binding.tablero

        binding.Reiniciar.setOnClickListener {
            Reiniciar()
        }
        binding.Finalizar.setOnClickListener {
            Finalizar()
        }
        Tiempo()
        informacion()
        countDownTimer.start()
        crearBotones(tablero)


    }

    //Funcion Tiempo
    private fun Tiempo(){
        val intent = Intent(this, Uno::class.java)//Actividad Uno

        countDownTimer = object : CountDownTimer(240000, 1000) {//Defino el tiempo
        override fun onTick(millisUntilFinished: Long) {
            //Pasa el tiempo a minutos y segundos
            val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
            val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished - TimeUnit.MINUTES.toMillis(minutesLeft))
            //Verifica si se acabo el tiempo
            if (minutesLeft == 0L && secondsLeft == 0L) {
                binding.txtTiempo.text = "Tiempo agotado"//Actualiza el cuadro de texto si se termina el tiempo
                onFinish()//Funcion que tiene un mensaje emergente
            } else {
                val timeLeft = String.format("%02d:%02d", minutesLeft, secondsLeft)//Formato en minutos y segundos
                binding.txtTiempo.text = "$timeLeft"//Actualiza el cuadro de texto con el tiempo
            }
        }

            //Funcion si se acabo el tiempo
            override fun onFinish() {
                // Mostrar una alerta cuando se acabe el tiempo
                val builder = AlertDialog.Builder(this@Uno)//Se muestra en la actividad 1
                builder.setTitle("Tiempo agotado")//Titulo de la ventana
                builder.setMessage("Se te ha agotado el tiempo \uD83D\uDE2D \n")//Mensaje de la ventana
                builder.setPositiveButton("Reiniciar") { dialog, which ->//Boton reiniciar
                    startActivity(intent)//Vuelve a reiniciar el nivel actual
                }
                builder.setNegativeButton("Salir") { dialog, which ->//Boton Salir
                    Perdedor()//Vuelve a la pagina pricipal MainActiviti
                }
                builder.show()
            }
        }
    }


    //Informaciom
    private fun informacion() {
        binding.txtPuntos.text = "Puntos " + this.puntos.toString()
    }

    //Crea los botones con un color
    private fun crearBotones(tablero: GridLayout) {
        for (i in 0..4) {
            for (j in 0..4) {
                val boton = Button(tablero.context)
                propiedades(boton, i, j, tablero)
                tablero.addView(boton)
                boton.setOnClickListener {
                    if (posicionActual == null) {
                        // Guarda la posición actual del botón seleccionado
                        posicionActual = Pair(i, j)
                    } else {
                        // Comprueba si la posición del botón seleccionado es adyacente a la posición actual
                        val filaActual = posicionActual!!.first
                        val columnaActual = posicionActual!!.second
                        if ((i == filaActual - 1 && j == columnaActual) ||
                            (i == filaActual + 1 && j == columnaActual) ||
                            (i == filaActual && j == columnaActual - 1) ||
                            (i == filaActual && j == columnaActual + 1)
                        ) {
                            // Intercambia las posiciones de los botones
                            val botonActual =
                                tablero.getChildAt(filaActual * 5 + columnaActual) as Button
                            botonActual.text = boton.text.also { boton.text = botonActual.text }
                            botonActual.background =
                                boton.background.also { boton.background = botonActual.background }
                            posicionActual = null
                        }
                        verificarCombinacion(tablero)
                        if(puntos>=30){
                            Ganador()
                        }
                    }
                }
            }
        }
    }

    private fun propiedades(boton: Button, i: Int, j: Int, tablero: GridLayout) {
        boton.id = View.generateViewId()
        boton.layoutParams = TableRow.LayoutParams(180, 180)
        val layoutParams = boton.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(7, 7, 7, 7)
        boton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        pintar(boton, i, j, tablero)
    }

    private fun pintar(boton: Button, i: Int, j: Int, tablero: GridLayout) {
        val colores = listOf("#BB8FCE", "#7FB3D5", "#F8C471", "#7DCEA0")

        // Asignar color aleatorio y verificar que no sea igual al color del botón anterior en la misma fila o columna
        var color: Int

        do {
            val colorStr = colores.random()
            if(colorStr == "#BB8FCE"){
                boton.text = "\uD83C\uDF69"
            }else{
                if(colorStr == "#7FB3D5"){
                    boton.text = "\uD83C\uDF6C"
                }else{
                    if(colorStr == "#F8C471"){
                        boton.text = "\uD83C\uDF67"
                    }else{
                        boton.text = "\uD83C\uDF6B"

                    }
                }
            }
            color = Color.parseColor(colorStr)
        } while (i > 0 && (tablero.getChildAt((i - 1) * 5 + j).background as? ColorDrawable)?.color == color ||
            j > 0 && (tablero.getChildAt(i * 5 + j - 1).background as? ColorDrawable)?.color == color
        )
        boton.setBackgroundColor(color)
    }

    private fun verificarCombinacion(tablero: GridLayout) {
        val filas = tablero.rowCount
        val columnas = tablero.columnCount

        // Verificar combinaciones horizontales
        for (i in 0 until filas) {
            var contador = 1
            var colorAnterior: Int? = null
            for (j in 0 until columnas) {
                val boton = tablero.getChildAt(i * columnas + j) as Button
                val color = (boton.background as? ColorDrawable)?.color
                if (color == colorAnterior) {
                    contador++
                } else {
                    contador = 1
                }
                colorAnterior = color
                if (contador == 3) {
                    // Cambiar color de los botones y aumentar puntos
                    soundEffect.start()
                    for (k in j - 2..j) {
                        val botonCombinado = tablero.getChildAt(i * columnas + k) as Button
                        pintar(botonCombinado, i, k, tablero)
                    }
                    this.puntos++
                    informacion()
                    // Verificar combinaciones horizontales después de cambiar el color de los botones
                    for (k in j - 2..j) {
                        if (k > 0) {
                            val colorAnterior =
                                (tablero.getChildAt(i * columnas + k - 1).background as? ColorDrawable)?.color
                            val boton = tablero.getChildAt(i * columnas + k) as Button
                            val color = (boton.background as? ColorDrawable)?.color
                            val colorSiguiente =
                                (tablero.getChildAt(i * columnas + k + 1).background as? ColorDrawable)?.color
                            if (color == colorAnterior && color == colorSiguiente) {
                                pintar(
                                    tablero.getChildAt(i * columnas + k - 1) as Button,
                                    i,
                                    k - 1,
                                    tablero
                                )
                                pintar(
                                    tablero.getChildAt(i * columnas + k + 1) as Button,
                                    i,
                                    k + 1,
                                    tablero
                                )
                            }
                        }
                    }
                    break
                }
            }
        }

        // Verificar combinaciones verticales
        for (j in 0 until columnas) {
            var contador = 1
            var colorAnterior: Int? = null
            for (i in 0 until filas) {
                val boton = tablero.getChildAt(i * columnas + j) as? Button
                if (boton != null) {
                    val color = (boton.background as? ColorDrawable)?.color
                    if (color == colorAnterior) {
                        contador++
                    } else {
                        contador = 1
                    }
                    colorAnterior = color
                    if (contador == 3) {
                        // Cambiar color de los botones y aumentar puntos
                        soundEffect.start()
                        for (k in i - 2..i) {
                            val botonCombinado = tablero.getChildAt(k * columnas + j) as Button
                            pintar(botonCombinado, k, j, tablero)
                        }
                        this.puntos++
                        informacion()
                        // Verificar combinaciones verticales después de cambiar el color de los botones
                        for (k in i - 2..i) {
                            if (k > 0) {
                                val colorAnterior =
                                    (tablero.getChildAt((k - 1) * columnas + j).background as? ColorDrawable)?.color
                                val boton = tablero.getChildAt(k * columnas + j) as? Button
                                val color = (boton?.background as? ColorDrawable)?.color
                                val colorSiguiente =
                                    (tablero.getChildAt((k + 1) * columnas + j).background as? ColorDrawable)?.color
                                if (color == colorAnterior && color == colorSiguiente) {
                                    // Cambiar color de los botones y aumentar puntos
                                    for (l in k - 1..k + 1) {
                                        val botonCombinado =
                                            tablero.getChildAt(l * columnas + j) as Button
                                        pintar(botonCombinado, l, j, tablero)
                                    }
                                    this.puntos++
                                    informacion()
                                }
                            }
                        }
                        break
                    }
                }
            }
        }

    }



    //Funcion que vuelve a reiniciar el juego
    fun Reiniciar(){
        val dialogo = AlertDialog.Builder(this)
        val intent = Intent(this, Uno::class.java)//Activity info1

        dialogo.setTitle("Esta seguro de reiniciar el juego presione OK")
        dialogo.setPositiveButton("OK") { dialog, which ->// Este código se ejecutará cuando se presione el botón "OK"
            startActivity(intent)//Inicia la activiti info 1
        }
        val dialog = dialogo.create()
        dialog.show()
    }


    //Funcion que finaliza el juego
    fun Finalizar(){
        val dialogo = AlertDialog.Builder(this)
        val intent = Intent(this, Nivel::class.java)//Activity Main

        dialogo.setTitle("Si esta seguro de salir presione OK")//Mensaj de la ventana emergente
        dialogo.setPositiveButton("OK") { dialog, which ->// Este código se ejecutará cuando se presione el botón "OK"
            startActivity(intent)//Inicia la activity main
        }
        val dialog = dialogo.create()
        dialog.show()
    }


    //Funcion que me lleva a la activyti ganador
    fun Ganador(){
        var intent = Intent(this, Ganador2::class.java)//Muestra la vista del ganador
        intent.putExtra("puntos", puntos)//Envia los puntos para mostrar
        startActivity(intent)//Inicia la actividad ganador
    }

    //Funcion que me lleva a la activyti perdedor
    fun Perdedor(){
        var intent = Intent(this, Perdedor::class.java)//Muestra la vista del perdedor
        intent.putExtra("puntos", puntos)//Envia los puntos para mostrar
        startActivity(intent)//Inicia la actividad perdedor
    }




}




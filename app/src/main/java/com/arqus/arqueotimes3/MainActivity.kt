package com.arqus.arqueotimes3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el contenido de la actividad directamente usando el ID del layout
        setContentView(R.layout.activity_main)

        // Encuentra el botón por su ID y configura un click listener, por ejemplo:
        val miBoton: Button = findViewById(R.id.button_articles) // Reemplaza 'miBoton' con el ID real de tu botón
        miBoton.setOnClickListener {
            // Iniciar com.arqus.arqueotimes3.ArticlesActivity
            val intent = Intent(this@MainActivity, ArticlesActivity::class.java)
            startActivity(intent)
        }
    }
}

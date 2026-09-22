package com.example.trabalho1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun startQuiz(visualizacao: View) {
        val entradaNome = findViewById<EditText>(R.id.editTextName)
        val nome = entradaNome.text.toString().trim()

        if (nome.isEmpty()) {
            Toast.makeText(
                this, "Informe seu nome.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val proximaTela = Intent(this, QuizActivity::class.java)
        proximaTela.putExtra("userName", nome)
        startActivity(proximaTela)
    }
}

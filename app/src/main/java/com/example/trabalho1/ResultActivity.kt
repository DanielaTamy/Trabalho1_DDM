package com.example.trabalho1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val dados = intent.extras
        if (dados == null) {
            Toast.makeText(this, "Resultado indisponível.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val nome = dados.getString("userName")
        val pontuacao = dados.getInt("score", -1)

        if (nome.isNullOrEmpty() || pontuacao < 0 || pontuacao > 100) {
            Toast.makeText(this, "Dados inválidos.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.textViewName).text = "Jogador: $nome"
        findViewById<TextView>(R.id.textViewScore).text = "Pontuação: $pontuacao de 100"

        findViewById<Button>(R.id.buttonRestart).setOnClickListener {
            finish()
        }
    }
}

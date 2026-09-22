package com.example.trabalho1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {

    private var perguntaAtual = 0
    private var pontuacao = 0
    private val totalPerguntas = 5
    private val perguntasJogo = IntArray(5)
    private var respondida = false
    private var nomeUsuario = ""

    private val respostasCorretas = arrayOf(
        "Andorra", "Emirados Árabes", "Afeganistão", "Antígua e Barbuda", "Anguilla",
        "São Bartolomeu", "Bermudas", "Brunei", "Bolívia", "Países Baixos Caribenhos",
        "Brasil", "Equador", "Estônia", "Egito", "Saara Ocidental"
    )

    private val bandeiras = arrayOf(
        R.drawable.flag_ad, R.drawable.flag_ae,
        R.drawable.flag_af, R.drawable.flag_ag,
        R.drawable.flag_ai, R.drawable.flag_bl,
        R.drawable.flag_bm, R.drawable.flag_bn,
        R.drawable.flag_bo, R.drawable.flag_bq,
        R.drawable.flag_br, R.drawable.flag_ec,
        R.drawable.flag_ee, R.drawable.flag_eg,
        R.drawable.flag_eh
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val dados = intent.extras
        val nome = dados?.getString("userName")

        if (nome == null || nome.isEmpty()) {
            Toast.makeText(this, "Nome indisponível.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        nomeUsuario = nome

        sortQuestions()
        showQuestion()
    }

    private fun sortQuestions() {
        var posicao = 0
        while (posicao < totalPerguntas) {
            val numero = Random.nextInt(bandeiras.size)
            var repetida = false
            for (indiceAnterior in 0 until posicao) {
                if (perguntasJogo[indiceAnterior] == numero) {
                    repetida = true
                }
            }
            if (!repetida) {
                perguntasJogo[posicao] = numero
                posicao++
            }
        }
    }

    private fun showQuestion() {
        val indice = perguntasJogo[perguntaAtual]

        findViewById<TextView>(R.id.textViewQuestionNumber)
            .text = "${perguntaAtual + 1} de $totalPerguntas"

        findViewById<ImageView>(R.id.imageViewFlag)
            .setImageResource(bandeiras[indice])

        findViewById<EditText>(R.id.editTextAnswer).setText("")

        findViewById<TextView>(R.id.textViewCorrect)
            .visibility = View.INVISIBLE

        findViewById<TextView>(R.id.textViewIncorrect)
            .visibility = View.INVISIBLE

        findViewById<View>(R.id.buttonNext).visibility = View.GONE
        findViewById<View>(R.id.buttonAnswer).visibility = View.VISIBLE

        respondida = false
    }

    private fun recordAnswer(correta: Boolean) {
        if (respondida) return
        val indice = perguntasJogo[perguntaAtual]
        val mensagemCorreta = findViewById<TextView>(R.id.textViewCorrect)
        val mensagemIncorreta = findViewById<TextView>(R.id.textViewIncorrect)

        if (correta) {
            pontuacao += 20
            mensagemCorreta.text = "Correto!"
            mensagemCorreta.visibility = View.VISIBLE
        } else {
            mensagemIncorreta.text = "Incorreto! Resposta: ${respostasCorretas[indice]}"
            mensagemIncorreta.visibility = View.VISIBLE
        }

        respondida = true
        findViewById<View>(R.id.buttonAnswer).visibility = View.GONE
        findViewById<View>(R.id.buttonNext).visibility = View.VISIBLE
    }

    fun answerQuestion(visualizacao: View) {
        if (respondida) return

        val entradaResposta = findViewById<EditText>(R.id.editTextAnswer)
        val respostaDigitada = entradaResposta.text.toString()

        if (respostaDigitada.trim().isEmpty()) {
            Toast.makeText(this, "Informe o nome de um país.", Toast.LENGTH_SHORT).show()
            return
        }

        val indice = perguntasJogo[perguntaAtual]
        val paisCorreto = respostasCorretas[indice]

        val estaCorreta = respostaDigitada.trim().equals(paisCorreto, ignoreCase = true)

        recordAnswer(estaCorreta)
    }

    fun nextQuestion(visualizacao: View) {
        if (!respondida) return
        perguntaAtual++
        if (perguntaAtual < totalPerguntas) {
            showQuestion()
        } else {
            val proximaTela = Intent(this, ResultActivity::class.java)
            proximaTela.putExtra("userName", nomeUsuario)
            proximaTela.putExtra("score", pontuacao)
            startActivity(proximaTela)
            finish()
        }
    }
}

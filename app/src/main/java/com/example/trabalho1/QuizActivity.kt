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

    private var currentQuestion = 0
    private var score = 0
    private val totalQuestions = 5
    private val gameQuestions = IntArray(5)
    private var answered = false
    private var userName = ""

    private val correctAnswers = arrayOf(
        "Brasil", "Argentina", "França", "Japão", "Alemanha",
        "Itália", "Portugal", "Espanha", "Canadá", "México",
        "Chile", "Uruguai", "China", "Índia", "Austrália"
    )

    private val flags = arrayOf(
        R.drawable.flag_brasil, R.drawable.flag_argentina,
        R.drawable.flag_franca, R.drawable.flag_japao,
        R.drawable.flag_alemanha, R.drawable.flag_italia,
        R.drawable.flag_portugal, R.drawable.flag_espanha,
        R.drawable.flag_canada, R.drawable.flag_mexico,
        R.drawable.flag_chile, R.drawable.flag_uruguai,
        R.drawable.flag_china, R.drawable.flag_india,
        R.drawable.flag_australia
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

        val bundle = intent.extras
        val name = bundle?.getString("userName")

        if (name == null || name.isEmpty()) {
            Toast.makeText(this, "Nome indisponível.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        userName = name
        sortQuestions()
        showQuestion()
    }

    private fun sortQuestions() {
        var position = 0
        while (position < totalQuestions) {
            val number = Random.nextInt(flags.size)
            var repeated = false
            for (i in 0 until position) {
                if (gameQuestions[i] == number) {
                    repeated = true
                }
            }
            if (!repeated) {
                gameQuestions[position] = number
                position++
            }
        }
    }

    private fun showQuestion() {
        val index = gameQuestions[currentQuestion]

        findViewById<TextView>(R.id.textViewQuestionNumber)
            .text = "${currentQuestion + 1} de $totalQuestions"

        findViewById<ImageView>(R.id.imageViewFlag)
            .setImageResource(flags[index])

        findViewById<EditText>(R.id.editTextAnswer).setText("")

        findViewById<TextView>(R.id.textViewCorrect)
            .visibility = View.INVISIBLE

        findViewById<TextView>(R.id.textViewIncorrect)
            .visibility = View.INVISIBLE

        findViewById<View>(R.id.buttonNext).visibility = View.GONE
        findViewById<View>(R.id.buttonAnswer).visibility = View.VISIBLE

        answered = false
    }

    private fun recordAnswer(correct: Boolean) {
        if (answered) return
        val index = gameQuestions[currentQuestion]
        val good = findViewById<TextView>(R.id.textViewCorrect)
        val bad = findViewById<TextView>(R.id.textViewIncorrect)

        if (correct) {
            score += 20
            good.text = "Correto!"
            good.visibility = View.VISIBLE
        } else {
            bad.text = "Incorreto! Resposta: ${correctAnswers[index]}"
            bad.visibility = View.VISIBLE
        }

        answered = true
        findViewById<View>(R.id.buttonAnswer).visibility = View.GONE
        findViewById<View>(R.id.buttonNext).visibility = View.VISIBLE
    }

    fun nextQuestion(view: View) {
        if (!answered) return
        currentQuestion++
        if (currentQuestion < totalQuestions) {
            showQuestion()
        } else {
            val next = Intent(this, ResultActivity::class.java)
            next.putExtra("userName", userName)
            next.putExtra("score", score)
            startActivity(next)
            finish()
        }
    }
}
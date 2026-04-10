package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {

    private lateinit var inputMinutes: EditText
    private lateinit var timerText: TextView
    private lateinit var btnStart: Button
    private lateinit var btnReset: Button

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputMinutes = findViewById(R.id.inputMinutes)
        timerText = findViewById(R.id.timerText)
        btnStart = findViewById(R.id.btnStart)
        btnReset = findViewById(R.id.btnReset)


        btnStart.setOnClickListener {
            startTimer()
        }


        btnReset.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {

        if (isTimerRunning) {
            Toast.makeText(this, "Таймер уже работает!", Toast.LENGTH_SHORT).show()
            return
        }


        val minutesText = inputMinutes.text.toString()
        if (minutesText.isEmpty()) {
            Toast.makeText(this, "Введите минуты!", Toast.LENGTH_SHORT).show()
            return
        }

        val minutes = minutesText.toIntOrNull()
        if (minutes == null || minutes <= 0) {
            Toast.makeText(this, "Введите число больше 0", Toast.LENGTH_SHORT).show()
            return
        }


        timeLeftInMillis = minutes * 60 * 1000L


        startCountDown()
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                isTimerRunning = false
                timerText.text = "00:00"
                Toast.makeText(this@MainActivity, "⏰ Время вышло!", Toast.LENGTH_LONG).show()
                btnStart.text = "▶ СТАРТ"
            }
        }.start()

        isTimerRunning = true
        btnStart.text = "⏸ ПАУЗА"


        btnStart.setOnClickListener {
            pauseTimer()
        }
    }

    private fun pauseTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        btnStart.text = "▶ СТАРТ"


        btnStart.setOnClickListener {
            startTimer()
        }
    }

    private fun resetTimer() {

        countDownTimer?.cancel()

        isTimerRunning = false
        btnStart.text = "▶ СТАРТ"


        btnStart.setOnClickListener {
            startTimer()
        }


        val minutesText = inputMinutes.text.toString()
        if (minutesText.isNotEmpty()) {
            val minutes = minutesText.toIntOrNull() ?: 0
            timeLeftInMillis = minutes * 60 * 1000L
            updateTimerText()
        } else {
            timerText.text = "00:00"
        }
    }

    private fun updateTimerText() {
        val seconds = (timeLeftInMillis / 1000).toInt()
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        timerText.text = String.format("%02d:%02d", minutes, remainingSeconds)
    }
}
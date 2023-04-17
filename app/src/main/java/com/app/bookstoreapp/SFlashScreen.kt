package com.app.bookstoreapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SFlashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sflash_screen)

        GlobalScope.launch {
            delay(1000L)
            startActivity(Intent(this@SFlashScreen, MainActivity::class.java))
        }
    }
}

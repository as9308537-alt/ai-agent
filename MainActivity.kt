package com.example.aiagent

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEnable = findViewById<Button>(R.id.btnEnableService)
        val btnStart = findViewById<Button>(R.id.btnStartTask)

        btnEnable.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
            Toast.makeText(this, "List me 'AI Auto Agent' ko ON karein", Toast.LENGTH_LONG).show()
        }

        btnStart.setOnClickListener {
            val service = AgentAccessibilityService.instance
            if (service != null) {
                Toast.makeText(this, "Task shuru ho raha hai...", Toast.LENGTH_SHORT).show()
                service.startAutomatedSequence()
            } else {
                Toast.makeText(this, "Pehle Accessibility Service chalu karein!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

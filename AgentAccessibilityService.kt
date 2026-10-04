package com.example.aiagent

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import java.util.Locale

class AgentAccessibilityService : AccessibilityService(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private var isTtsReady = false
    private val mainHandler = Handler(Looper.getMainLooper())

    companion object {
        var instance: AgentAccessibilityService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        tts = TextToSpeech(this, this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Yahan live screen events listen kiye jaate hain
    }

    override fun onInterrupt() {
        alertUser("Agent interrupt ho gaya hai.")
    }

    fun startAutomatedSequence() {
        alertUser("Task shuru ho raha hai.")

        mainHandler.postDelayed({
            // Example step: Screen par button dhoondhna ya click karna
            val rootNode = rootInActiveWindow
            if (rootNode == null) {
                alertUser("Screen detect nahi ho rahi hai, kripya check karein!")
                return@postDelayed
            }

            // Click try karein (Example: center of screen tap)
            val clicked = tapAt(500f, 1000f)
            if (!clicked) {
                alertUser("Error: Step fail ho gaya hai, screen check karein.")
            } else {
                alertUser("Step complete ho gaya.")
            }
        }, 2000)
    }

    fun tapAt(x: Float, y: Float): Boolean {
        val clickPath = Path().apply { moveTo(x, y) }
        val stroke = GestureDescription.StrokeDescription(clickPath, 0, 100)
        val gesture = GestureDescription.Builder().addStroke(stroke).build()
        return dispatchGesture(gesture, null, null)
    }

    fun alertUser(text: String) {
        if (isTtsReady) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "AgentAlert")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("hi", "IN")
            isTtsReady = true
        }
    }

    override fun onDestroy() {
        instance = null
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}

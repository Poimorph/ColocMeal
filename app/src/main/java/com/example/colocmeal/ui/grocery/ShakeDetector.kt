package com.example.colocmeal.ui.grocery

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlin.math.sqrt

class ShakeDetector(
    private val onShake: () -> Unit,
) : SensorEventListener {
    private var lastShakeMs = 0L

    override fun onSensorChanged(event: SensorEvent?) {
        val (x, y, z) = event!!.values
        val gForce = sqrt(x*x + y*y + z*z)/ SensorManager.GRAVITY_EARTH
        if (gForce > SHAKE_THRESHOLD_G) {
            val now = System.currentTimeMillis()
            if(now-lastShakeMs < SHAKE_COOLDOWN_MS) return
            lastShakeMs = now
            onShake()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    companion object{
        const val SHAKE_THRESHOLD_G = 2.7f
        const val SHAKE_COOLDOWN_MS = 1000L

    }
}

@Composable
fun rememberShake(onShake: () -> Unit){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val detector = ShakeDetector(onShake)
        sm.registerListener(detector, accel, SensorManager.SENSOR_DELAY_GAME)
        onDispose { sm.unregisterListener(detector) }
    }
}
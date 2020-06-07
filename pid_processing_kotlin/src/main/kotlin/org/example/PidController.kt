package com.peheje.`fun`

class PidController(kp: Float, ki: Float, kd: Float) {
    var kp = 0.4f
    var ki = 0.21f
    var kd = 0.9f
    private var lastError = 0f
    private var derivative = 0f
    private var integral = 0f

    fun getControl(error: Float): Float {
        var error = error
        error *= -1f
        integral += error
        derivative = error - lastError
        lastError = error
        return kp * error + ki * integral + kd * derivative
    }

    init {
        this.kp = kp
        this.ki = ki
        this.kd = kd
    }
}
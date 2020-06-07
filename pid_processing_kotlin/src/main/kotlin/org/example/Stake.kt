package com.peheje.`fun`

import processing.core.PApplet
import java.util.concurrent.ThreadLocalRandom

class Stake : Cloneable {
    private val p: PApplet
    var position: Float private set
    private var lineLength: Float
    private var prePosition: Float
    private var velocity: Float
    private var acceleration: Float
    private var speed: Float

    constructor(p: PApplet, position: Float, velocity: Float, acceleration: Float, speed: Float) {
        this.p = p
        this.position = position
        this.prePosition = position
        this.velocity = velocity
        this.acceleration = acceleration
        this.speed = speed
        this.lineLength = speed / 2
    }

    fun control(controlAcceleration: Double) {
        acceleration += controlAcceleration.toFloat()
    }

    fun gravity(magnitude: Double) {
        // Added acceleration is proportional to current position
        acceleration += position * magnitude.toFloat()
    }

    fun disturb(magnitude: Float) {
        acceleration += magnitude
    }

    fun randomDisturb(magnitude: Float) {
        val ran = ThreadLocalRandom.current().nextDouble(-magnitude.toDouble(), magnitude.toDouble())
        acceleration += ran.toFloat()
    }

    fun update() {
        prePosition = position
        velocity += acceleration
        position += velocity
        acceleration = 0f
    }

    override fun toString(): String {
        return "Stake{" +
                "position=" + position +
                '}'
    }

    fun draw(order: Int, stakesSize: Int) {
        p.stroke(40f, 100f, 0f, 255f)
        p.strokeWeight(2f)
        val ageOfStake = stakesSize - order
        val moveAway = ageOfStake * speed
        val positionOffset = position + p.height / 2
        val prePositionOffset = prePosition + p.height / 2
        val verticalHalf = p.width / 2 - moveAway
        p.line(verticalHalf + lineLength, positionOffset, verticalHalf - lineLength, prePositionOffset)
    }

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): Any {
        return super.clone()
    }
}
package com.peheje.`fun`

import processing.core.PApplet
import processing.core.PConstants
import java.util.*

class Main : PApplet() {

    companion object Factory {
        fun run() {
            val main = Main()
            main.runSketch()
        }
    }

    private var curStake = Stake(this, 0.0f, 0.0f, 0.0f, 2f)
    private val maxHistory = 500
    private val controller = PidController(0.4f, 0.05f, 0.9f)
    private val stakes: MutableList<Stake> = ArrayList()
    private val tickYSpace = 50
    private val tickXSpace = 50
    private val tickLength = 10
    private val clickDisturb = 350f
    private lateinit var kpScroll: HScrollbar
    private lateinit var kdScroll: HScrollbar
    private lateinit var kiScroll: HScrollbar
    private lateinit var gravityScroll: HScrollbar
    private var gravityStrength = 0.2f

    private fun drawCoordinateSystem() {
        val h2 = height / 2
        val w2 = width / 2
        val tickDist2 = tickLength / 2
        strokeWeight(1f)
        stroke(0)
        line(0f, h2.toFloat(), width.toFloat(), h2.toFloat()) // X axis
        line(w2.toFloat(), 0f, w2.toFloat(), height.toFloat()) // Y axis

        // Draw ticklines X
        textAlign(PConstants.CENTER, PConstants.CENTER)
        var i = 0
        while (i <= width) {
            line(i.toFloat(), h2 + tickDist2.toFloat(), i.toFloat(), h2 - tickDist2.toFloat())
            text(i - w2, i.toFloat(), h2 + 10.toFloat())
            i += tickXSpace
        }

        // Draw ticklines Y
        i = 0
        while (i <= height) {
            line(w2 + tickDist2.toFloat(), i.toFloat(), w2 - tickDist2.toFloat(), i.toFloat())
            text(-(i - h2), w2 - 20.toFloat(), i - 1.toFloat())
            i += tickYSpace
        }
    }

    private var once = true
    private fun drawScrollbars() {
        val textOffLeft = 40
        val offsetTop = 30
        val height = 10
        val spacing = 20
        val length = 300
        if (once) {
            kpScroll = HScrollbar(this, 0.0f, offsetTop.toFloat(), length, height)
            kpScroll.pos = controller.kp * length
            kdScroll = HScrollbar(this, 0.0f, (height + offsetTop + spacing).toFloat(), length, height)
            kdScroll.pos = controller.kd * length
            kiScroll = HScrollbar(this, 0.0f, (2 * height + offsetTop + spacing * 2).toFloat(), length, height)
            kiScroll.pos = controller.ki * length
            gravityScroll = HScrollbar(this, 0.0f, (3 * height + offsetTop + spacing * 3).toFloat(), length, height)
            gravityScroll.pos = 0.2f * length
            once = false
        }
        kpScroll.update()
        kdScroll.update()
        kiScroll.update()
        gravityScroll.update()

        kpScroll.display()
        kdScroll.display()
        kiScroll.display()
        gravityScroll.display()

        controller.kp = map(kpScroll.scrollPos, 0f, length.toFloat(), 0f, 1.03f)
        controller.kd = map(kdScroll.scrollPos, 0f, length.toFloat(), 0f, 1.03f)
        controller.ki = map(kiScroll.scrollPos, 0f, length.toFloat(), 0f, 1.03f)
        gravityStrength = map(gravityScroll.scrollPos, 0f, length.toFloat(), 0f, 1.03f)
        text("kp: " + nf(controller.kp, 0, 2), textOffLeft + length.toFloat(), offsetTop.toFloat())
        text(
            "kd: " + nf(controller.kd, 0, 2),
            textOffLeft + length.toFloat(),
            height + offsetTop + spacing.toFloat()
        )
        text(
            "ki: " + nf(controller.ki, 0, 2),
            textOffLeft + length.toFloat(),
            2 * height + offsetTop + (spacing * 2).toFloat()
        )
        text(
            "gravity: " + nf(gravityStrength, 0, 2),
            textOffLeft + length.toFloat(),
            3 * height + offsetTop + (spacing * 3).toFloat()
        )
    }

    override fun settings() {
        size(1200, 800)
    }

    override fun setup() {
        frameRate(60f)
        fill(120f, 50f, 240f)
        curStake.disturb(0f)
        stakes.add(curStake)
    }

    override fun mousePressed() {
        curStake.disturb(-clickDisturb)
    }

    override fun draw() {
        try {
            // Clear everything
            background(255)
            drawCoordinateSystem()
            drawScrollbars()

            // Update & gravity
            curStake.update()
            curStake.gravity(gravityStrength.toDouble())

            // PID Controller
            val goal = 0f
            val error = curStake.position - goal
            val control = controller.getControl(error)
            curStake.control(control.toDouble())

            // Draw all the stakes
            for (j in 0 until stakes.size) {
                stakes[j].draw(j, stakes.size)
            }

            // Copy curStake so we may update it
            curStake = curStake.clone() as Stake
            stakes.add(curStake)

            // Delete old stakes
            if (stakes.size == maxHistory) {
                stakes.removeAt(0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun main() {
    Main.run()
}
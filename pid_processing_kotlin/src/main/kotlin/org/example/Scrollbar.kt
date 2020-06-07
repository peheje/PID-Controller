package com.peheje.`fun`

import processing.core.PApplet
import processing.event.MouseEvent
import kotlin.math.abs

internal class HScrollbar {
    private val p: PApplet
    private val scrollWidth: Int
    private val scrollHeight: Int
    private val xPos: Float
    private val yPos: Float
    var scrollPos: Float private set
    private var newPos: Float
    private val posMin: Float
    private val posMax: Float
    private val loose: Int
    private var locked = false
    private val ratio: Float

    constructor(processing: PApplet, xp: Float, yp: Float, scrollW: Int, scrollH: Int) {
        p = processing
        scrollWidth = scrollW
        scrollHeight = scrollH
        val widthHeightRatio = scrollW - scrollH
        ratio = scrollW.toFloat() / widthHeightRatio.toFloat()
        xPos = xp
        yPos = yp - scrollH / 2
        scrollPos = xPos + scrollW / 2 - scrollH / 2
        newPos = scrollPos
        posMin = xPos
        posMax = xPos + scrollW - scrollH
        loose = 10
    }

    fun update(wheel: Int) {
        val over = mouseOverSlider()
        if (p.mousePressed) {
            if (over) locked = true
        } else locked = false
        if (locked) {
            newPos = (p.mouseX - scrollHeight / 2.toFloat()).coerceIn(posMin, posMax)
        }
        if (over && wheel != 0) {
            newPos = (newPos + (wheel*3).toFloat()).coerceIn(posMin, posMax)
        }
        if (abs(newPos - scrollPos) > 1.0f) {
            scrollPos += (newPos - scrollPos) / loose
        }
    }

    private fun mouseOverSlider(): Boolean {
        return p.mouseX > xPos && p.mouseX < xPos + scrollWidth && p.mouseY > yPos && p.mouseY < yPos + scrollHeight
    }

    fun display() {
        p.noStroke()
        p.fill(204)
        p.rect(xPos, yPos, scrollWidth.toFloat(), scrollHeight.toFloat())
        if (mouseOverSlider() || locked) {
            p.fill(0f, 0f, 0f)
        } else {
            p.fill(102f, 102f, 102f)
        }
        p.rect(scrollPos, yPos, scrollHeight.toFloat(), scrollHeight.toFloat())
    }

    var pos
        get() = scrollPos * ratio
        set(pos) {
            scrollPos = pos
            newPos = pos
        }
}
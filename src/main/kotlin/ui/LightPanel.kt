package yt.yacht.ui

import java.awt.Color
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.event.ChangeListener

class LightPanel(private val lightColor: Color) : JPanel() {

    private val changeListeners = arrayListOf<ChangeListener>()

    fun addChangeListener(l: ChangeListener) {
        changeListeners.add(l)
    }

    fun removeChangeListener(l: ChangeListener) {
        changeListeners.remove(l)
    }

    fun callStateChanged() {
        for (listener in changeListeners) {
            listener.stateChanged(null)
        }
    }

    var count = 0
    var isLightFixed = false

    private var lightOnMouse = 0
    private var light1 = Light()
    private var light2 = Light()
    private var light3 = Light()
    private var light4 = Light()
    private var lights = arrayOf(light1, light2, light3, light4)
    private var lightMouseListener: MouseAdapter

    init {
        layout = null

        for (i in lights.indices) {
            lights[i].apply {
                bounds = Rectangle(35 * i, 0, 35, 35)
                addMouseListener(object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        lightOnMouse = i + 1
                    }
                })
            }.also { this.add(it) }
        }

        lightMouseListener = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                if (!isLightFixed) {
                    isLightFixed = true
                    count = lightOnMouse
                } else {
                    isLightFixed = false
                    count = 0
                }

                callStateChanged()
            }

            override fun mouseEntered(e: MouseEvent?) {
                if (!isLightFixed) setLightCount(lightOnMouse)
            }

            override fun mouseExited(e: MouseEvent?) {
                lightOnMouse = 0
                if (!isLightFixed) setLightCount(lightOnMouse)
            }
        }

        for (light in lights) {
            light.addMouseListener(lightMouseListener)
        }
    }

    fun setEditable(value: Boolean) {
        if (value) {
            for (light in lights) {
                if (lightMouseListener !in light.mouseListeners) {
                    light.addMouseListener(lightMouseListener)
                }
            }
        } else {
            for (light in lights) {
                light.removeMouseListener(lightMouseListener)
            }
        }
    }

    fun setFourthLightVisible(value: Boolean) {
        light4.isVisible = value
    }

    fun setLightCount(value: Int) {

        count = value

        for (i in lights.indices) {
            lights[i].apply {
                lightColor = if (i < value) {
                    this@LightPanel.lightColor
                } else defaultLightColor
            }.also { it.repaint() }
        }
    }
}
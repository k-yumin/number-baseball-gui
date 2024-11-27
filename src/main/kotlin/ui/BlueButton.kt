package yt.yacht.ui

import yt.yacht.fontFamily
import java.awt.Color
import java.awt.Rectangle
import javax.swing.JButton

class BlueButton : JButton() {
    init {
        bounds = Rectangle(123, 260, 100, 30)
        background = Color(0, 124, 255)
        font = fontFamily[1].deriveFont(14f)
        foreground = Color.WHITE
        isFocusPainted = false
    }
}
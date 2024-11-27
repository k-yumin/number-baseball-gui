package yt.yacht

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import yt.yacht.game.Computer
import yt.yacht.game.Human
import yt.yacht.ui.BlueButton
import yt.yacht.ui.LightPanel
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.event.ChangeListener
import kotlin.collections.ArrayList

private const val TITLE = "baseball"

private val playOptions = arrayOf("컴퓨터", "내")
private val modeOptions = arrayOf(3, 4)

private var selectedPlay = playOptions[0]
private var selectedMode = modeOptions[0]

lateinit var tabbedPane: JTabbedPane
lateinit var ruleTextArea: JTextArea
lateinit var gameRound: JLabel
lateinit var gameTurn: JLabel
lateinit var guessField: JTextField
lateinit var guessLabel: JLabel
lateinit var gameGuide: JLabel
lateinit var ballLabel: JLabel
lateinit var ballPanel: LightPanel
lateinit var strikeLabel: JLabel
lateinit var strikePanel: LightPanel
lateinit var gameButton: JButton

val fontFamily = ArrayList<Font>()

fun setupFontFamily() {
    val fontSizes = arrayOf("NotoSansKR-Medium", "NotoSansKR-Bold")
    for (fontSize in fontSizes) {
        val stream = object {}.javaClass.getResourceAsStream("/font/${fontSize}.ttf")
        val font = Font.createFont(Font.TRUETYPE_FONT, stream)
        fontFamily.add(font)
    }
}

fun main() {
    setupFontFamily()
    FlatMacDarkLaf.setup()

    val frame = JFrame(TITLE).apply {
        size = Dimension(360, 360)
        layout = null
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    }

    tabbedPane = JTabbedPane().apply {
        bounds = Rectangle(0, -33, 420, 453)
    }.also { frame.add(it) }

    /* *** */

    val mainTab = JPanel().apply { layout = null }

    JLabel("게임 옵션을 선택하세요", SwingConstants.CENTER).apply {
        bounds = Rectangle(0, 25, 346, 20)
        font = fontFamily[0].deriveFont(16f)
        foreground = Color.WHITE
    }.also { mainTab.add(it) }

    val start = 82

    val playOption = JComboBox(playOptions).apply {
        bounds = Rectangle(start, start, 100, 33)
        componentOrientation = ComponentOrientation.RIGHT_TO_LEFT
        font = fontFamily[1].deriveFont(18f)
        foreground = Color.WHITE
        addActionListener {
            selectedPlay = selectedItem as String
        }
    }.also { mainTab.add(it) }

    JLabel("가 생각한").apply {
        bounds = Rectangle(start+102, start+3, 120, 24)
        font = fontFamily[1].deriveFont(20f)
    }.also { mainTab.add(it) }

    JComboBox(modeOptions).apply {
        bounds = Rectangle(start, start+40, 60, 33)
        componentOrientation = ComponentOrientation.RIGHT_TO_LEFT
        font = fontFamily[1].deriveFont(18f)
        foreground = Color.WHITE
        addActionListener {
            selectedMode = selectedItem as Int
        }
    }.also { mainTab.add(it) }

    JLabel("자리 숫자를").apply {
        bounds = Rectangle(start+84, start+43, 120, 24)
        font = fontFamily[1].deriveFont(20f)
    }.also { mainTab.add(it) }

    val player = JLabel("내", SwingConstants.RIGHT).apply {
        bounds = Rectangle(start+2, start+83, 100, 24)
        font = fontFamily[1].deriveFont(20f)
        foreground = Color.WHITE
    }.also { mainTab.add(it) }

    JLabel("가 맞히기").apply {
        bounds = Rectangle(start+102, start+83, 120, 24)
        font = fontFamily[1].deriveFont(20f)
    }.also { mainTab.add(it) }

    playOption.addActionListener {
        if (playOption.selectedItem == playOptions[0]) {
            player.text = playOptions[1]
        } else {
            player.text = playOptions[0]
        }
    }

    BlueButton().apply {
        text = "게임 입장"
        addActionListener {
            readyGame()
            tabbedPane.selectedIndex = 1
        }
    }.also { mainTab.add(it) }

    /* *** */

    val ruleTab = JPanel().apply { layout = null }

    JLabel("게임 규칙", SwingConstants.CENTER).apply {
        bounds = Rectangle(0, 25, 346, 20)
        font = fontFamily[1].deriveFont(16f)
        foreground = Color.WHITE
    }.also { ruleTab.add(it) }

    ruleTextArea = JTextArea().apply {
        bounds = Rectangle(25, 50, 300, 200)
        font = fontFamily[0].deriveFont(12f)
        isEditable = false
        caretColor = Color(0, 0, 0, 0)
        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ESCAPE) {
                    tabbedPane.selectedIndex = 0
                }
            }
        })
    }.also { ruleTab.add(it) }

    BlueButton().apply {
        text = "게임 시작"
        addActionListener {
            for (listener in gameButton.actionListeners) {
                gameButton.removeActionListener(listener)
            }
            startGame()
            tabbedPane.selectedIndex = 2
        }
    }.also { ruleTab.add(it) }

    /* *** */

    val gameTab = JPanel().apply { layout = null }

    gameRound = JLabel("", SwingConstants.CENTER).apply {
        bounds = Rectangle(0, 12, 346, 18)
        font = fontFamily[1].deriveFont(14f)
    }.also { gameTab.add(it) }

    gameTurn = JLabel("", SwingConstants.CENTER).apply {
        bounds = Rectangle(0, 50, 346, 18)
        font = fontFamily[1].deriveFont(14f)
    }.also { gameTab.add(it) }

    guessField = JTextField().apply {
        bounds = Rectangle(50, 73, 246, 38)
        border = null
        font = fontFamily[1].deriveFont(26f)
        foreground = Color.WHITE
        horizontalAlignment = SwingConstants.CENTER
        addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                if (!e.keyChar.isDigit() || text.length >= selectedMode) e.consume()
            }
        })
    }.also { gameTab.add(it) }

    guessLabel = JLabel("", SwingConstants.CENTER).apply {
        bounds = Rectangle(0, 73, 346, 34)
        font = fontFamily[1].deriveFont(30f)
        foreground = Color.WHITE
    }.also { gameTab.add(it) }

    gameGuide = JLabel("", SwingConstants.CENTER).apply {
        bounds = Rectangle(0, 122, 346, 18)
        font = fontFamily[1].deriveFont(14f)
    }.also { gameTab.add(it) }

    ballLabel = JLabel("B").apply {
        bounds = Rectangle(83, 159, 35, 20)
        font = fontFamily[1].deriveFont(16f)
        foreground = Color.WHITE
    }.also { gameTab.add(it) }

    ballPanel = LightPanel(Color.GREEN).apply {
        bounds = Rectangle(103, 153, 140, 35)
    }.also { gameTab.add(it) }

    strikeLabel = JLabel("S").apply {
        bounds = Rectangle(83, 206, 35, 20)
        font = fontFamily[1].deriveFont(16f)
        foreground = Color.WHITE
    }.also { gameTab.add(it) }

    strikePanel = LightPanel(Color.YELLOW).apply {
        bounds = Rectangle(103, 200, 140, 35)
    }.also { gameTab.add(it) }

    gameButton = BlueButton().also { gameTab.add(it) }

    /* *** */

    with(tabbedPane) {
        add("main", mainTab)
        add("rule", ruleTab)
        add("game", gameTab)
    }

    with(frame) {
        setLocationRelativeTo(null)
        isResizable = false
        isVisible = true
    }
}

fun readyGame() {
    val computerRule =
        """
            다음 규칙에 따라 컴퓨터가 ${selectedMode}자리 숫자를 정합니다.
            - 각 자릿수의 숫자는 모두 다릅니다.
            - 숫자는 0으로 시작할 수 없습니다.
            
            사용자가 추측한 숫자를 입력하면,
            다음 조건에 해당하는 개수만큼 카운터에 표시됩니다.
            - 숫자는 맞지만 위치가 틀렸을 때는 볼(B)
            - 숫자와 위치가 모두 맞을 때는 스트라이크(S)
            - 숫자와 위치가 모두 다를 때는 표시되지 않습니다.(아웃)
            
            * 뒤로 가기 및 게임 종료는 ESC 키를 눌러주세요.
        """.trimIndent()

    val humanRule =
        """
            다음 규칙에 따라 임의의 ${selectedMode}자리 숫자를 정하세요.
            - 각 자릿수의 숫자는 모두 다릅니다.
            - 숫자는 0으로 시작할 수 없습니다.
            
            컴퓨터가 추측한 숫자를 제시하면,
            다음 조건에 해당하는 개수만큼 카운터에 표시하세요.
            - 숫자는 맞지만 위치가 틀렸을 때는 볼(B)
            - 숫자와 위치가 모두 맞을 때는 스트라이크(S)
            - 숫자와 위치가 모두 다를 때는 표시하지 않습니다.(아웃)
            
            * 뒤로 가기 및 게임 종료는 ESC 키를 눌러주세요.
        """.trimIndent()

    ruleTextArea.text = if (selectedPlay == playOptions[0]) {
        computerRule
    } else {
        humanRule
    }
}

fun startGame() {

    val posX = 83

    if (selectedMode == modeOptions[0]) {

        val posX3 = posX + 18

        ballLabel.location = Point(posX3, ballLabel.y)
        ballPanel.apply {
            location = Point(posX3+20, y)
            setFourthLightVisible(false)
        }
        strikeLabel.location = Point(posX3, strikeLabel.y)
        strikePanel.apply {
            location = Point(posX3+20, y)
            setFourthLightVisible(false)
        }
    } else {
        ballLabel.location = Point(posX, ballLabel.y)
        ballPanel.apply {
            location = Point(posX+20, y)
            setFourthLightVisible(true)
        }
        strikeLabel.location = Point(posX, strikeLabel.y)
        strikePanel.apply {
            location = Point(posX+20, y)
            setFourthLightVisible(true)
        }
    }

    with(ballPanel) {
        isLightFixed = false
        setLightCount(0)
    }

    with(strikePanel) {
        isLightFixed = false
        setLightCount(0)
    }

    if (selectedPlay == playOptions[0]) {
        gameTurn.text = "나의 추측"
        guessField.isVisible = true
        guessLabel.isVisible = false
        ballPanel.setEditable(false)
        strikePanel.setEditable(false)
        gameButton.isVisible = false

        startComputer()
    } else {
        gameTurn.text = "컴퓨터의 추측"
        guessField.isVisible = false
        guessLabel.isVisible = true
        gameGuide.text = ""
        ballPanel.setEditable(true)
        strikePanel.setEditable(true)
        gameButton.isVisible = true

        startHuman()
    }
}

fun startComputer() {
    val computer = Computer(selectedMode)

    var round = 1
    val maxRound = 9
    gameRound.text = "$round / $maxRound"

    val guide1 = "ENTER 키를 눌러 결과를 확인하세요"
    val guide2 = "ENTER 키를 눌러 다음 라운드로 이동하세요"
    gameGuide.text = guide1

    var endFlag = false
    var enterCount = 0

    with(guessField) {

        text = ""
        isEditable = true
        caretColor = null

        val gameListener = object : ActionListener {
            override fun actionPerformed(e: ActionEvent?) {
                // Ignore the input if the game is over or the number of digits is incorrect.
                if (endFlag || text.length != selectedMode) return

                enterCount++

                if (enterCount % 2 == 1) {
                    // When the human guessed a number and submitted it (enterCount is odd)
                    // Showing balls and strikes for human's guessed number
                    isEditable = false
                    caretColor = Color(0, 0, 0, 0)
                    gameGuide.text = guide2

                    val response = computer.respond(text)
                    ballPanel.setLightCount(response[0])
                    strikePanel.setLightCount(response[1])

                    if (response[1] == selectedMode) {
                        // When the number is guessed correctly
                        endFlag = true
                        gameGuide.text = ""
                        with(gameButton) {
                            text = "승리!"
                            background = Color(50, 199, 50)
                            isVisible = true
                        }
                    } else if (round == 9) {
                        // When the number is not guessed correctly within 9 rounds
                        endFlag = true
                        gameGuide.text = ""
                        with(gameButton) {
                            text = "패배!"
                            background = Color(255, 61, 51)
                            isVisible = true
                        }
                    }
                } else {
                    // Going to the next round (ballCount is even)
                    // Waiting for the human to input a number
                    round++
                    gameRound.text = "$round / $maxRound"
                    text = ""
                    isEditable = true
                    caretColor = null
                    gameGuide.text = guide1
                    ballPanel.setLightCount(0)
                    strikePanel.setLightCount(0)
                }
            }
        }

        // exit the game by pressing exit game button
        val escapeListener = object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ESCAPE) gameButton.doClick()
            }
        }

        addActionListener(gameListener)
        addKeyListener(escapeListener)

        // exit game button
        gameButton.addActionListener {
            removeActionListener(gameListener)
            removeKeyListener(escapeListener)
            tabbedPane.selectedIndex = 0 // go to the main screen
        }
    }
}

fun startHuman() {
    val human = Human(selectedMode)

    var round = 1
    val maxRound = 9
    gameRound.text = "$round / $maxRound"
    guessLabel.text = human.pop()

    val ballListener = ChangeListener {
        val sum = ballPanel.count + strikePanel.count
        if (sum > selectedMode) {
            with(ballPanel) {
                isLightFixed = false
                setLightCount(0)
            }
        }
        with(gameButton) {
            text = if (sum == 0) "아웃" else "다음"
            background = Color(0, 124, 255)
        }
    }

    with(ballPanel) {
        addChangeListener(ballListener)
        callStateChanged()
    }

    val strikeListener = ChangeListener {
        val sum = ballPanel.count + strikePanel.count
        if (sum > selectedMode) {
            with(strikePanel) {
                isLightFixed = false
                setLightCount(0)
            }
        }
        with(gameButton) {
            if (strikePanel.count == selectedMode) {
                text = "정답!"
                background = Color(255, 61, 51)
            } else {
                text = if (sum == 0) "아웃" else "다음"
                background = Color(0, 124, 255)
            }
        }
    }

    with(strikePanel) {
        addChangeListener(strikeListener)
        callStateChanged()
    }

    var endFlag = false

    with(gameButton) {
        val gameListener = ActionListener {
            if (endFlag) return@ActionListener

            if (strikePanel.count == selectedMode || round == 9) {
                endFlag = true
                ballPanel.setEditable(false)
                strikePanel.setEditable(false)
                doClick()
                return@ActionListener
            }

            round++
            gameRound.text = "$round / $maxRound"

            try {
                val response = human.respond(ballPanel.count, strikePanel.count)
                guessLabel.text = response

                with(ballPanel) {
                    isLightFixed = false
                    setLightCount(0)
                    callStateChanged()
                }
                with(strikePanel) {
                    isLightFixed = false
                    setLightCount(0)
                    callStateChanged()
                }
            } catch (e: IllegalArgumentException) {
                // In theory, the computer is guaranteed to guess correctly within 9 rounds.
                // If there are no candidates left, it is suspected that the user made an incorrect input.
                endFlag = true
                guessLabel.text = "?"
                gameGuide.text = "어디서부터 잘못된 걸까..."
                ballPanel.setEditable(false)
                strikePanel.setEditable(false)
                text = "게임 종료"
                background = Color(255, 61, 51)
            }
        }

        val escapeListener = object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                if (e.keyCode == KeyEvent.VK_ESCAPE) {
                    endFlag = true
                    doClick()
                }
            }
        }

        addActionListener(gameListener)
        addKeyListener(escapeListener)

        // exit game button (only when endFlag is true)
        addActionListener {
            if (!endFlag) return@addActionListener
            ballPanel.removeChangeListener(ballListener)
            strikePanel.removeChangeListener(strikeListener)
            removeActionListener(gameListener)
            removeKeyListener(escapeListener)
            tabbedPane.selectedIndex = 0 // go to the main screen
        }
    }
}
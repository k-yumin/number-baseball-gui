package yt.yacht.game

import kotlin.random.Random
import kotlin.random.nextInt

/** @param mode 3 or 4; 3-digit or 4-digit number to guess */
class Computer(private val mode: Int) {

    private var number = ""

    init {
        val random = Random(System.currentTimeMillis())

        // Generating a random number for the human to guess
        number = random.nextInt(1..9).toString()
        while (number.length != mode) {
            val n = random.nextInt(0..9).toString()
            if (n !in number) number += n
        }
    }

    /**
     * @param answer human's guessed number
     * @return an array of balls and strikes for the answer
     * */
    fun respond(answer: String): Array<Int> {
        val counter = arrayOf(0, 0)

        for (i in 0 until mode) {
            for (j in 0 until mode) {
                if (number[i] == answer[j]) {
                    if (i == j) counter[1]++ else counter[0]++
                }
            }
        }

        return counter
    }
}
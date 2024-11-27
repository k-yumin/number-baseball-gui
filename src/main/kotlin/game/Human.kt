package yt.yacht.game

import kotlin.math.pow
import kotlin.random.Random
import kotlin.random.nextInt

/** @param mode 3 or 4; 3-digit or 4-digit number to guess */
class Human(private val mode: Int) {

    private var candidates = arrayListOf<String>()
    private val used = arrayListOf<String>()

    init {
        val start = 10.0.pow(mode-1).toInt()
        val end = 10.0.pow(mode).toInt()

        // Filling all possible numbers that match the rules into the candidates
        loop@ for (i in start until end) {
            val s = i.toString()
            val hashSet = hashSetOf<Char>()
            for (c in s) if (!hashSet.add(c)) continue@loop
            candidates.add(s)
        }
    }

    /**
     * Pick a random number from the candidates, remove it from the candidates, and add it to the used list.
     * @return a number from the candidates
     * */
    fun pop(): String {
        val random = Random(System.currentTimeMillis())
        val pop = candidates[random.nextInt(0 until candidates.size)]
        candidates.remove(pop)
        used.add(pop)
        return pop
    }

    /**
     * Remove the candidates that do not match the conditions based on balls and strikes.
     * @param ball balls the human responded with for the candidate
     * @param strike strikes the human responded with for the candidate
     * @return a new number from the candidates
     * */
    fun respond(ball: Int, strike: Int): String {

        val last = used.last()

        // OUT
        if (strike == 0 && ball == 0) {
            val temp = hashSetOf<String>()
            for (candidate in candidates) {
                for (c in last) if (c in candidate) temp.add(candidate)
            }
            candidates.removeAll(temp)
        }

        // STRIKE
        if (strike != 0) {
            val temp = hashSetOf<String>()
            for (candidate in candidates) {
                var count = 0
                for (i in 0 until mode) {
                    if (candidate[i] == last[i]) count++
                }
                if (count != strike) temp.add(candidate)
            }
            candidates.removeAll(temp)
        }

        // BALL
        if (ball != 0) {
            val temp = hashSetOf<String>()
            for (candidate in candidates) {
                var count = 0
                for (i in 0 until mode) {
                    for (j in 0 until mode) {
                        if (i != j && candidate[i] == last[j]) {
                            count++
                        }
                    }
                }
                if (count != ball) temp.add(candidate)
            }
            candidates.removeAll(temp)
        }

        return pop()
    }
}
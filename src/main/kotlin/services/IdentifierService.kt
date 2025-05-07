package me.blueb.services

import java.lang.System

class IdentifierService {
    public var aidxCounter = 0
    public var aidxAlphabet = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")

    fun generate(): String {
        return this.generateAidx()
    }

    fun generateAidx(): String {
        var id: String = ""

        val ms: Int = System.currentTimeMillis().toInt()
        val ms2000: Int = 946684800000.toInt();

        var time: Int;
        time = (ms - ms2000)
        if (time < 0) time = 0

        aidxCounter++

        id += time.toString(36).padStart(8, '0')
        id += aidxAlphabet.random()
        id += aidxAlphabet.random()
        id += aidxAlphabet.random()
        id += aidxAlphabet.random()
        id += aidxCounter.toString(36).padStart(4, '0').slice(IntRange(0, id.length-4))

        return id
    }
}

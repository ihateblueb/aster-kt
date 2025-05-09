package me.blueb.service

import java.lang.System
import java.util.Date

class IdentifierService {
    public var aidxCounter = 0
    public var aidxAlphabet =
        listOf(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "i",
            "j",
            "k",
            "l",
            "m",
            "n",
            "o",
            "p",
            "q",
            "r",
            "s",
            "t",
            "u",
            "v",
            "w",
            "x",
            "y",
            "z",
        )

    fun generate(): String = this.generateAidx()

    fun generateAidx(): String {
        var id: String = ""

        val now = Date()
        val time2000 = Date(946684800000L)

        var time: Long
        time = (now.time - time2000.time)
        if (time < 0) time = 0

        aidxCounter++

        id += time.toString(36).padStart(8, '0')
        id += aidxAlphabet.random()
        id += aidxAlphabet.random()
        id += aidxAlphabet.random()
        id += aidxAlphabet.random()
        id += aidxCounter.toString(36).padStart(4, '0').takeLast(4)

        return id
    }
}

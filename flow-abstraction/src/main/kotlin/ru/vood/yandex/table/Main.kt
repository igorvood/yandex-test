package ru.vood.yandex.table

data class MatchResult(
    val commandName1: String,
    val commandName2: String,
    val commandGoal1: Int,
    val commandGoal2: Int,
)

fun parseStr(s: String): MatchResult {
    val split = s.split(" - ")
    val goals = split[2].split(":")
    return MatchResult(split[0], split[1], goals[0].toInt(), goals[1].toInt())
}

fun getSomeStr(n: Int, s: String): String = IntRange(0, n - 1).joinToString("") { s }

fun getMinusStr(n: Int) = getSomeStr(n, "-")
fun getMinusPlusStr(n: Int) = getSomeStr(n, "-+")

fun addSpace(s: String, maxSise: Int): String {
    val n = maxSise - s.length
    return s + getSomeStr(n, " ")
}

@JvmInline
value class CommandName(val value: String)

data class Game(
    val otherCommand: CommandName,
    val otherGoal: Int,
    val thisGoal: Int,
)


fun main() {
    val listOf = listOf(
        "Linux - Gentoo - 1:0",
        "Gentoo - Windows - 2:1",
        "Linux - Windows - 0:2"
    )
    extracted(listOf)
    println()
    println()

    val listOf1 = listOf(
        "Cplusplus - C - 1:0",
        "Cplusplus - Php - 2:0",
        "Java - Php - 1:0",
        "Java - C - 2:2",
        "Java - Perl - 1:1",
        "Java - Haskell - 1:1",
    )

    extracted(listOf1)
}

private fun extracted(listOf: List<String>) {
    val matchResults = listOf
        .map { s -> parseStr(s) }

    val allCommands: List<IndexedValue<String>> =
        matchResults.fold(setOf<String>()) { qw, ee -> qw.plus(setOf(ee.commandName1, ee.commandName2)) }
            .sorted()
            .withIndex()
            .toList()

//    val toMap = allCommands.associate { it.index to it.value }

    val maxNameLength = allCommands.maxOf { it.value.length }
    val sizeNumComand = allCommands.size.toString().length
    val head =
        "+" + getMinusStr(sizeNumComand) + "+" + getMinusStr(maxNameLength + 1) + "+" + getMinusPlusStr(allCommands.size + 2)


    val gamesMap = matchResults
        .flatMap {
            listOf(
                CommandName(it.commandName1) to Game(CommandName(it.commandName2), it.commandGoal2, it.commandGoal1),
                CommandName(it.commandName2) to Game(CommandName(it.commandName1), it.commandGoal1, it.commandGoal2),
            )
        }
        .groupBy { it.first }
        .map { it.key to it.value.toMap() }
        .toMap()

    println(head)
    allCommands
        .forEach { d ->
            println(comandStr(d, sizeNumComand, maxNameLength, allCommands, gamesMap))
            println(head)
        }
}

fun comandStr(
    indexedValue: IndexedValue<String>,
    sizeNumComand: Int,
    maxNameLength: Int,
    allCommands: List<IndexedValue<String>>,
    gamesMap: Map<CommandName, Map<CommandName, Game>>
): String {
    val begin = "|" + addSpace((indexedValue.index + 1).toString(), sizeNumComand) + "|" + addSpace(
        indexedValue.value,
        maxNameLength
    ) + " |"





    return begin
}

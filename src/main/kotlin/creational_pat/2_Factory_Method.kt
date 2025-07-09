package top.zimang.creational_pat

import java.lang.IllegalArgumentException


fun main() {
    val queen = createPiece("qa5fa") //quen是接口ChessPiece
    println(queen)
    println(queen is Queen)
}



/**
 * 请注意，Kotlin 中的接口可以声明属性，这是一个非常强大的特性。
 */
interface ChessPiece {
    val file: Char
    val rank: Char
}

data class Pawn(
    override val file: Char,
    override val rank: Char
) : ChessPiece


data class Queen(
    override val file: Char,
    override val rank: Char
) : ChessPiece

fun createPiece(notation: String): ChessPiece {
    //首先，toCharArray() 函数将字符串转换为字符数组。
    //  第一个字符（索引0）表示棋子类型，
    //  第二个（索引1）表示文件（垂直列），
    //  第三个（索引2）表示等级（水平行）。

    //其次，这段代码使用了解构声明。
    val (type, file, rank) = notation.toCharArray()

    //val type = notation.toCharArray()[0] 代码可以更复杂
    //val file = notation.toCharArray()[1]
    //val rank = notation.toCharArray()[2]

    //根据字母代表的类型，它会选择并实例化ChessPiece接口的实现之一。
    // 这是工厂方法设计模式的核心概念，其中对象的创建被委托给工厂方法。
    return when (type) {
        'q' -> Queen(file, rank)
        'p' -> Pawn(file, rank)
        // ...
        else -> throw IllegalArgumentException("Unknown piece: $type")
    }
}

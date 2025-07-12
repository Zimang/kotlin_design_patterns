package top.zimang.reactive_and_concurrent_apt

fun main() {

//    val next = counter()
//    println(next())
//    println(next())
//    println(next())

    //可变
    fun <T> removeFirst(list: MutableList<T>): T {
        return list.removeAt(0)
    }

    val list1 = mutableListOf(1, 2, 3)
    println(removeFirst(list1)) // Prints 1
    println(removeFirst(list1)) // Prints 2

    //不可变
    fun <T> withoutFirst(list: List<T>): T {
        return ArrayList(list).removeAt(0)
    }

    val list = mutableListOf(1, 2, 3)
    println(withoutFirst(list)) // It's 1
    println(withoutFirst(list)) // Still 1
}

fun counter(): () -> Int {
    var i = 0
    return { i++ }
}


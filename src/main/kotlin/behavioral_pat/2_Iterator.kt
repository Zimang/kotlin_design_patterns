package top.zimang.behavioral_pat


/**
 *
 */
fun main() {
    val platoon = Squad(
        Trooper(),
        Squad(
            Trooper(),
        ),
        Trooper(),
        Squad(
            Trooper(),
            Trooper(),
        ),
        Trooper()
    )

    val l = listOf<String>()

    l.iterator()

    // For loop range must have an iterator method
    for (trooper in platoon) {
        println(trooper)
    }
}


class TrooperIterator(private val units: List<Trooper>) : Iterator<Trooper> {
    //首先，我们需要为迭代器创建一个状态，用于记录上次返回的元素
    private var i = 0

    private var iterator: Iterator<Trooper> = this
    override fun hasNext(): Boolean {
        if (i >= units.size) {
            return false
        }
        if (i == units.size - 1) {
            if (iterator != this) {
                return iterator.hasNext()
            }
        }
        return true
    }

    override fun next(): Trooper {
        if (iterator != this) {
            if (iterator.hasNext()) {
                return iterator.next()
            } else {
                i++
                iterator = this
            }
        }

        return when (val e = units[i]) {
            is Squad -> {
                iterator = e.iterator()
                this.next()
            }
            else -> {
                i++
                e
            }
        }
    }
}

/**
 * 我们的排，采用了组合设计模式，不是一个扁平的数据结构。
 * 它可以包含包含其他对象的对象 - 小队可以包含士兵以及其他小队。
 * 然而，在这种情况下，我们希望抽象出这种复杂性，并将其视为士兵列表。
 * 迭代器模式正是如此 - 它将我们复杂的数据结构展平为简单的元素序列。
 * 元素的顺序以及要忽略哪些元素由迭代器决定。
 */
class Squad(private val units: List<Trooper>) : Trooper() {

    constructor(vararg units: Trooper) : this(units.toList())

    operator fun iterator(): Iterator<Trooper> {
        return TrooperIterator(units)
    }
}
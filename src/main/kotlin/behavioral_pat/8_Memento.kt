package top.zimang.behavioral_pat

/**
 * 自从迈克尔担任经理角色以来，要找到他解决问题变得有些困难。
 * 偶尔得到他的注意时，他总是匆忙回答完问题就匆匆离开去赴下一个约会。
 * 比如，就在昨天，我请教他对我们游戏中的新武器有何看法。
 * 他毫不犹豫地建议了一个“椰子炮”。
 * 然而，令我惊讶的是，当我今天展示这个功能时，他却显然感到沮丧。
 *
 * 经过一番来回讨论，他坚持说他提到过一个“菠萝发射器”！
 * 幸好他只是只金丝雀，否则我可能会面临更大的反弹。
 * 我常常希望能有一种记录我们互动的方式。
 * 这样，如果由于他注意力不集中导致会议不顺利，我就可以回放他的确切话语。
 *
 * 如果迈克尔一次想的事情超过两件，他会忘记最开始想的那件事情。
 *
 * 即使录音了，解读迈克尔的话仍然是一个挑战，主要是因为他没有提供清晰的反馈。
 * 而且，如果我要回放录音，迈克尔可能会辩称，虽然这可能是他说的话，但并不是他的本意。
 *
 * 介绍一下“备忘录”设计模式。
 * 这种模式通过保存对象的内部状态来解决这些问题。
 * **保存的状态对外部影响是不可变的，确保迈克尔无法否认他最初的陈述。**
 * 此外，只有对象本身才能利用这个保存的状态。
 *
 * 尽管“备忘录”设计模式在许多现代应用程序中并不常见，但在需要恢复到先前状态的特定情况下，它仍具有潜力。
 */
fun main() {
    val michael = Manager()
    michael.think("Need to implement Coconut Cannon")
    michael.think("Should get some coffee")
    val memento = michael.saveThatThought()
    /**
     * 值得一提的是我们使用的with标准函数，它有助于消除多次调用michael.think()时的冗余。
     * 每当你发现自己在代码块中多次引用同一个对象时，with可以是一个简洁的方式来简化流程并减少重复。
     */
    with(michael) {
        think("Or maybe tea?")
        think("No, actually, let's implement Pineapple Launcher")
    }
    michael.printThoughts()
    michael.recall(memento)
    michael.printThoughts()
}

/**
 * 在 Kotlin 中，可以通过使用内部类来有效实现这一点。让我们来探讨一下这是如何实现的：
 */
class Manager {
    private var thoughts = mutableListOf<String>()

    fun printThoughts() {
        println(thoughts)
    }

    /**
     * 在这里，我们介绍了一个关键字inner，它作为我们类的前缀。
     *
     * 如果没有这个关键字，我们的类将被称为嵌套类，类似于Java的静态嵌套类。
     *
     * Kotlin中内部类的主要优势之一是能够访问封闭类的私有字段。
     * 这意味着我们的Memory类可以无缝地修改Manager类的内部状态。
     *
     * 有了这个设置，我们可以通过创建当前状态的快照来准确捕捉Michael当前的陈述：
     */
    inner class Memory(private val mindState: List<String>) {
        fun restore() {
            thoughts = mindState.toMutableList()
        }
    }

    fun saveThatThought(): Memory {
        return Memory(thoughts.toList())
    }

    fun recall(memory: Memory) {
        memory.restore()
    }

    fun think(thought: String) {
        thoughts.add(thought)
        if (thoughts.size > 2) {
            thoughts.removeFirst()
        }
    }
}


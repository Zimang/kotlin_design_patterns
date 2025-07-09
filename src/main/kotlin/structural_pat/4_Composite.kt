package top.zimang.structural_pat

/**
 * 为组合设计模式单独设置一个部分可能看起来有些奇怪,这个设计模式难道不应该包含所有其他结构模式吗？
 * 毕竟Composite就意味着结构
 */
fun main() {
    val bobaFett = StormTrooper(Rifle(), RegularLegs())

    val squad = Squad(listOf(bobaFett.copy(), bobaFett.copy(), bobaFett.copy()))
    //Squad本身实现了Trooper意味着也可以传入构造函数之中,这里可以是为nestedComposite
    //只要层次结构内的所有对象遵循相同的接口，无论嵌套的深度如何，
    //  我们仍然保留着能够促使最顶层对象对其下的每个元素执行操作的能力。
    val squads = Squad(Squad(), bobaFett.copy(), bobaFett.copy())

    squad.attackRebel(1, 2)

    val secondSquad = Squad(
        bobaFett.copy(),
        bobaFett.copy(),
        bobaFett.copy()
    )
}

/**
 * 为了更好地协调，帝国决定为风暴军队引入小队的概念。
 * 一个小队应包含一名或多名任何类型的风暴军队士兵，当接到命令时，它应该像一个单一单位一样行动。
 * 为了让我们的小队像一个整体一样行动，我们将为其添加两个方法，分别是move()和attackRebel():
 *
 * 这两个功能将把接收到的所有命令重复发送给它们包含的所有单元。起初，这种方法似乎很有效。
 * 但是，如果我们通过添加新功能来更改我们的士兵接口会发生什么呢？请考虑以下代码：
 * ```
 * interface Trooper {
 *     fun move(x: Long, y: Long)
 *     fun attackRebel(x: Long, y: Long)
 *     fun retreat() {
 *     	println("Retreating!")
 *     }
 * }
 * ```
 *
 * 这个变化将迫使我们实施撤退功能，并使用override关键字标记另外两个函数
 * 现在，让我们简要探讨一种不同且更容易处理这种情况的方法——这种方法可以让我们创建相同的对象，同时生成一个更加用户友好的复合结构。
 */
class Squad(private val units: List<Trooper>) : Trooper {

    /**
     * 用到了可变参数
     *
     * 让我们试着了解一下这是如何在内部运作的。Kotlin编译器将可变参数（vararg）转换为相同类型的数组：
     *
     * ```
     * constructor(vararg units: Trooper) : this(units.toList())
     * constructor(units: Array<Trooper>) : this(units.toList())
     * ```
     */
    constructor(vararg units: Trooper) : this(units.toList())

    override fun move(x: Long, y: Long) {
        for (u in units) {
            u.move(x, y)
        }
    }

    override fun attackRebel(x: Long, y: Long) {
        for (u in units) {
            u.attackRebel(x, y)
        }
    }
}


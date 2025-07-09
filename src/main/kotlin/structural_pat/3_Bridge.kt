package top.zimang.structural_pat

/**
 * 桥接设计模式是一个很棒的工具，可以**避免滥用继承**并创建更灵活、易维护的代码的陷阱。
 * 它有助于将抽象与实现分离，从而实现更好的可扩展性。
 */
fun main() {
    val stormTrooper = StormTrooper(Rifle(), RegularLegs())
    val flameTrooper = StormTrooper(Flamethrower(), RegularLegs())
    val scoutTrooper = StormTrooper(Rifle(), AthleticLegs())

    println(listOf(stormTrooper, flameTrooper, scoutTrooper))
}

/**
 * 假设我们想要建立一个系统来管理银河帝国不同类型的士兵。
 * 我们首先要定义一个接口，来表示士兵的基本行为：
 */
interface Trooper {
    fun move(x: Long, y: Long)
    fun attackRebel(x: Long, y: Long)

    fun retreat() {
        println("Retreating!")
    }
}

/**
 * # without bridge
 * 通过这个界面，我们建立了所有士兵应该具备的基本行为。
 * 接下来，我们将开始实现不同类型的士兵：
 *
 * ```
 * open class StormTrooper : Trooper {
 *      override fun move(x: Long, y: Long) {
 *          // Move at normal speed
 *      }
 *      override fun attackRebel(x: Long, y: Long) {
 *          // Missed most of the time
 *      }
 * }
 * open class ShockTrooper : Trooper {
 *      override fun move(x: Long, y: Long) {
 *          // Moves slower than regular StormTrooper
 *      }
 *      override fun attackRebel(x: Long, y: Long) {
 *          // Sometimes hits
 *      }
 * }
 * ```
 * 我们建立了所有士兵应该具备的基本行为。接下来，我们将开始实现不同类型的士兵：
 * ```
 * class RiotControlTrooper : StormTrooper() {
 *     override fun attackRebel(x: Long, y: Long) {
 *     	// Has an electric baton, stay away!
 *     }
 * }
 * class FlameTrooper : ShockTrooper() {
 *     override fun attackRebel(x: Long, y: Long) {
 *     	// Uses flametrower, dangerous!
 *     }
 * }
 * class ScoutTrooper : ShockTrooper() {
 *     override fun move(x: Long, y: Long) {
 *     	// Runs faster
 *     }
 * }
 * ```
 *
 * 随着我们的士兵系统不断扩展并适应新需求，评估这些变化对我们现有设计的影响至关重要。
 * 其中一项增强功能是使冲锋队员能够喊出独特的口号。
 * 这可以通过以下界面实现：
 * ```
 * interface Trooper {
 *     fun move(x: Long, y: Long)
 *     fun attackRebel(x: Long, y: Long)
 *     fun shout(): String
 * }
 * ```
 * 然而，在Trooper接口中引入这个新的shout()方法导致了实现这个接口的每个类都出现了编译错误。
 *
 * **鉴于这类类的数量庞大，这个更新需要进行重大的代码更改。**
 *
 * 这意味着我们需要进行大量的工作来修改每个类以符合更新后的接口。然而，我们是否应该毫不犹豫地接受这项任务呢？
 *
 * # with bridge
 *
 * **桥接设计模式提供了一种管理复杂层次结构和正交属性的方式，避免出现脆弱基类问题。**
 * 让我们探讨一下桥接模式如何通过处理武器类型和移动速度作为构造函数参数来简化我们的士兵系统：
 *
 * 通过在我们的士兵类构造函数中接受武器和速度参数，我们简化了类层次结构，创建了一个更灵活的系统。
 * 这种方法遵循桥接设计模式将抽象与实现分离的原则，使我们能够轻松修改和扩展系统，而不会在整个代码库中造成连锁反应。
 *
 * 在现实生活中，这种模式经常与帮助组装部件的工具一起使用。
 * 例如，这样可以让我们在不改变代码工作方式的情况下，从使用真实数据库切换到虚拟数据库。
 * 这使得启动我们的代码更简单，更快地检查它是否正常运行。
 * 依赖注入可以简单地看作是桥接设计模式的一种实现。
 */
data class StormTrooper(
    private val weapon: Weapon,
    private val legs: Legs
) : Trooper {
    override fun move(x: Long, y: Long) {
        legs.move(x, y)
    }

    override fun attackRebel(x: Long, y: Long) {
        println("Attacking")
        weapon.attack(x, y)
    }
}

/**
 * 在Stormtrooper类的属性中使用接口是遵循桥接设计模式并为以后选择实现提供灵活性的好方法：
 * 请注意，这些方法返回的是 Meters 和 PointsOfDamage，而不是简单地返回 Long 和 Int。这个特性被称为类型别名。
 */
typealias PointsOfDamage = Long
typealias Meters = Int

interface Weapon {
    fun attack(x: Long, y: Long): PointsOfDamage
}

interface Legs {
    fun move(x: Long, y: Long): Meters
}

/**
 * 在 Kotlin 中，常量和类型别名一样，都是在编译时被替换的值。它们的有效性在于在编译时就已知。
 *
 * const 关键字告诉编译器，变量的值应被视为编译时常量，对常量的引用将在编译时被实际值替换。使用时无需访问内存.
 *
 * 1. 被替换
 *
 * 2. 存在于`顶层文件级别`&`object对象`&`companion object`
 *
 * 3. 只能是基本类型或者String
 */
const val RIFLE_DAMAGE = 3L
const val REGULAR_SPEED: Meters = 1

class Rifle : Weapon {
    override fun attack(x: Long, y: Long) = RIFLE_DAMAGE
}

class Flamethrower : Weapon {
    override fun attack(x: Long, y: Long) = RIFLE_DAMAGE * 2
}


class Batton : Weapon {
    override fun attack(x: Long, y: Long) = RIFLE_DAMAGE * 3
}

class RegularLegs : Legs {
    override fun move(x: Long, y: Long) = REGULAR_SPEED
}

class AthleticLegs : Legs {
    override fun move(x: Long, y: Long) = REGULAR_SPEED * 2
}


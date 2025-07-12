package top.zimang.behavioral_pat

/**
 * 策略设计模式的目标是允许对象在运行时改变其行为。
 *
 * 让我们回顾一下我们在第3章“理解结构模式”中设计的平台游戏，当时我们讨论了外观设计模式。
 * 在我们的小独立游戏开发公司中担任游戏设计师的Canary Michael提出了一个很棒的想法。
 * 如果我们给我们的英雄一系列武器来保护我们免受那些可怕的食肉蜗牛的侵害呢？
 */
fun main() {
    val hero = OurHero()
    hero.shoot()
    hero.currentWeapon = Weapons::banana
    hero.shoot()
}

/**
 * 正如你所看到的，我们并没有实现一个接口，而是有多个函数接收相同的参数并返回相同的对象。
 * 最有趣的部分是我们的英雄。现在OurHero类包含两个值，它们都是函数
 *
 * 这种方法显著减少了我们需要编写的类的数量，同时保持了相同的功能性。
 * 当你的可互换算法不涉及状态时，可以用一个简单的函数来替换它。
 *
 * 如果涉及状态，可以引入一个接口，然后每个策略模式可以实现它。
 */
class OurHero {
    private var direction = Direction.LEFT
    private var x: Int = 42
    private var y: Int = 173

    var currentWeapon = Weapons::peashooter

    val shoot = fun() {
        currentWeapon(x, y, direction)
    }

}

/**
 * 武器都会朝着我们的英雄所面对的方向发射抛射物（你可不想离那些危险的蜗牛太近）。
 */
enum class Direction {
    LEFT, RIGHT
}

/**
 * 在第2章《使用创建型模式》中，当我们探讨原型设计模式时，强调了首选创建不可变数据类，并在必要时通过使用复制构造函数来改变对象的状态。
 * 然而，为了这个例子的目的，让我们假设我们的游戏需要高性能，并且在处理可变状态时我们正在采取一切必要的预防措施。
 *
 * 所有抛射物都应该有一对坐标（记住我们的游戏是2D的）和一个方向：
 */
data class Projectile(
    private var x: Int,
    private var y: Int,
    private var direction: Direction
)

interface Weapon {
    fun shoot(
        x: Int,
        y: Int,
        direction: Direction
    ): Projectile
}

/**
 * 首先，我们将通过使用一个对象为所有武器创建一个容器。
 * 这并非强制性的，但有助于保持事物井然有序。
 * 接着，我们将每个武器表示为一个函数，而不是使用类
 */
object Weapons {
    // Flies straight
    fun peashooter(x: Int, y: Int, direction: Direction): Projectile {
        println("It's a peashooter")
        return Projectile(x, y, direction)
    }

    // Returns back after reaching end of the screen
    fun banana(x: Int, y: Int, direction: Direction): Projectile {
        println("It's a banana")
        return Projectile(x, y, direction)
    }

    // Explodes on contact
    fun pomegranate(x: Int, y: Int, direction: Direction): Projectile {
        println("It's a pomegranate")
        return Projectile(x, y, direction)
    }
}



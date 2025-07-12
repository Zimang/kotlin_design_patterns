package top.zimang.behavioral_pat

/**
 * 你可以将状态设计模式视为我们在本章前面讨论的策略模式的更具主见的版本。
 *
 * 然而，两者之间有一个关键区别：策略模式通常是由客户端从外部进行更改，而状态可以仅基于接收到的输入在内部进行更改。
 *
 * 考虑一下客户端与策略模式之间的对话：
 *
 * 客户端：“这是一个新的事情要做，从现在开始做吧。”
 *
 * 策略：“好的，没问题。”
 *
 * 客户端：“我喜欢你的地方在于你从不和我争论。”
 *
 * 现在，再与这段对话进行比较：
 *
 * 客户端：“这是我为你准备的一些新输入。”
 *
 * 状态：“哦，我不知道。也许我会开始做一些不同的事情。也可能不会。”
 *
 * 客户端也应该准备好状态可能拒绝一些输入：
 *
 * 客户端：“这是给你思考的东西，状态。”
 *
 * 状态：“我不知道这是什么！你看不见我很忙吗？拿这个去烦一些策略吧！”
 *
 * 那么，为什么客户端能容忍状态的这种不可预测行为呢？嗯，这是因为状态擅长保持控制并根据需要管理情况。
 */
fun main() {
    val snail = Snail()
    snail.seeHero()
    snail.getHit(1)
    snail.getHit(10)
}

/**
 * 让我们看看状态设计模式如何帮助我们模拟演员的变化行为 -
 *      在我们的情况下，是平台游戏中的敌人。
 *
 * 默认情况下，蜗牛应该保持静止以保存蜗牛的能量。但当英雄靠近时，它应该向他们猛冲过去。
 * 如果英雄设法伤害它，它应该撤退舔舐伤口。
 * 然后，它会重复攻击，直到其中一个死亡。
 */
interface WhatCanHappen {
    fun seeHero()

    fun getHit(pointsOfDamage: Int)

    fun calmAgain()
}

/**
 * 我们的蜗牛实现了这个接口，以便在发生任何可能影响它的事情时得到通知并做出相应的反应：
 *
 * 先前的方法包含了大部分与我们上下文相关的逻辑。随着你的上下文增长，你可能会发现其他方法也能很好地运作。
 * 在其中一种方法中，蜗牛类将被简化，状态管理将转移到一个单独的类中：
 *
 * ```
 * class Snail {
 *     internal var mood: Mood = Still(this)
 *     private var healthPoints = 10
 *         // That's all!
 * }
 *
 * sealed class Mood : WhatCanHappen
 *
 * class Still(private val snail: Snail) : Mood() {
 *     override fun seeHero() {
 *     	snail.mood = Aggressive
 *     }
 *     override fun getHit(pointsOfDamage: Int) {
 *     	// Same logic from before
 *     }
 *     override fun calmAgain() {
 *     	// Return to Still state
 *     }
 * }
 * ```
 *
 *请注意，我们的状态对象现在在构造函数中接收到它们的上下文的引用。
 * 如果您的状态中的代码量相对较小，请使用第一种方法。
 * 如果变体差异很大，请使用第二种方法。
 *
 * 现实世界中广泛使用这种模式的一个例子是 Kotlin 的协程机制。
 * 我们将在第6章“线程和协程”中详细讨论这一点。
 *
 * 在探讨了状态设计模式的复杂性以及它如何允许游戏中的角色动态改变行为之后，自然而然地考虑到我们如何以灵活和解耦的方式执行这些行为或动作。
 * 这将引出我们的下一个主题：命令设计模式。命令模式赋予我们将动作封装为对象的能力，使我们能够对其进行参数化、排队甚至记录，为健壮且模块化的代码奠定基础。因此，让我们看看命令设计模式如何改进我们游戏的架构。
 */
class Snail : WhatCanHappen {
    private var healthPoints = 10
    private var mood: Mood = Still

    override fun seeHero() {
        mood = when (mood) {
            is Still -> {
                println("Aggressive")
                Aggressive
            }
            else -> {
                println("No change")
                mood
            }
        }
    }

    override fun getHit(pointsOfDamage: Int) {
        println("Hit for $pointsOfDamage points")
        healthPoints -= pointsOfDamage

        println("Health: $healthPoints")
        mood = when {
            (healthPoints <= 0) -> {
                println("Dead")
                Dead
            }
            mood is Aggressive -> {
                println("Retreating")
                Retreating
            }
            else -> {
                println("No change")
                mood
            }
        }
    }

    override fun calmAgain() {
    }
}

/*
sealed class Mood {
    // Some abstract methods here, like draw(), for example
}

data object Still : Mood()

data object Aggressive : Mood()

data object Retreating : Mood()

data object Dead : Mood()*/

//封闭类本质上是抽象的，不能直接实例化。使用它们的优势很快就会变得明显。
// 但首先，让我们定义其他状态，更准确地说，是状态：
/**
 * 由于Mood类不封装任何状态，我们可以将其转换为封闭的整数。
 *
 * 封闭接口的功能类似于封闭类，但更加灵活。它们允许不同模块或文件中的类或对象进行实现。当您希望拥有受限制的类型层次结构，但又不能或不想强制所有实现者在同一文件中时，封闭接口尤为有用。
 */
sealed interface Mood {
    // Some abstract methods here, like draw(), for example
}



data object Still : Mood {}

data object Aggressive : Mood

data object Retreating : Mood

data object Dead : Mood
package top.zimang.behavioral_pat

/**
 * 我们的游戏开发团队面临着独特的挑战，并且这些挑战并不完全是与代码相关的。
 * 简单说一下：我们的独立运营团队只有我和迈克尔，一个戴着产品经理帽子的金丝雀，以及一对猫设计师。
 * 这些猫咪创意人才虽然经常在打盹，但偶尔也会给我们带来高质量的模型。
 *
 * 我们最明显的疏忽是什么呢？我们缺少一个专门的质量保证（QA）团队。
 * 这或许可以解释我们的游戏为何会频繁崩溃。
 *
 * 另一方面，值得一提的是，迈克尔最近向我介绍了肯尼，一只鹦鹉，事实证明，它擅长质量保证工作：
 *
 * 中介者设计模式类似于一个通信监督者。它不喜欢对象之间的直接交互。
 *
 * 事实上，如果发生直接通信，它可能会感到相当激动。
 *
 * 它的指导原则是什么？所有通信都应该通过中介者进行。
 *
 * 为什么要这样的政策？这种方法极大地减少了对象之间的相互依赖性。
 *
 * 对象不需要了解许多同行，而只需熟悉一个实体：中介者。
 *
 * 基于这一点，我选择了迈克尔来担任这一关键角色。他现在将协调和调解所有这些复杂的过程
 *
 *
 * 迈克尔迅速崭露头角。所有人都只通过他进行互动，他成为他们互动的唯一管理者。
 *
 * 有可能他会变成一个上帝对象——一个无所不知、全能的实体，这是我们将在第9章“成语和反模式”中讨论的反模式。
 *
 * 尽管他很重要，但至关重要的是要明确这个中介者的责任范围，也许更重要的是要明确它不承担的责任。
 *
 * 如果你对现实生活中中介者模式的一些例子感兴趣，
 *
 * Android的Model-View-Presenter（MVP）架构中的Presenter类就是一个显著的例子。
 *
 * 它充当View和Model之间的中间人，处理用户交互，从Model中检索和格式化数据以在View中显示，并管理应用程序的导航和流程。
 *
 * Presenter的使用将应用程序逻辑与UI代码解耦，提高了可测试性和可维护性。
 *
 * 此外，它独立于Android特定类，简化了测试中的模拟和Android生命周期事件的处理。
 *
 * 随着我们探索各种设计模式，很明显，在软件开发中保持对象状态而不损害其封装性是一个经常出现的需求。
 *
 * 这引出了备忘录设计模式，这是一个专门设计用来捕获对象内部状态并在以后恢复的解决方案。让我们在接下来的部分来探讨它。
 */
fun main() {
    val productManager = Michael
    val company = MyCompany(productManager)
    company.taskCompleted(true)
}

interface ProductManager {
    fun isAllGood(majorRelease: Boolean): Boolean
}

object Michael : ProductManager {
    private val kenny = Kenny(this)
    private val brad = Brad(this)

    override fun isAllGood(majorRelease: Boolean): Boolean {
        if (!kenny.isEating() && !kenny.isSleeping()) {
            println(kenny.doesMyCodeWork())
        } else if (!brad.isEating() && !brad.isSleeping()) {
            println(brad.doesMyCodeWork())
        }
        return true
    }
}


interface QA {
    fun doesMyCodeWork(): Boolean
}

interface Parrot {
    fun isEating(): Boolean
    fun isSleeping(): Boolean
}


class Kenny(private val productManager: ProductManager) : QA, Parrot {
    override fun isSleeping(): Boolean {
        return false
    }

    override fun isEating(): Boolean {
        return false
    }

    override fun doesMyCodeWork(): Boolean {
        return true
    }
}

class Brad(private val productManager: ProductManager) : QA, Parrot {
    override fun isSleeping(): Boolean {
        return false
    }

    override fun isEating(): Boolean {
        return false
    }

    override fun doesMyCodeWork(): Boolean {
        return true
    }
}

object Me
class MyCompany(private val productManager: ProductManager) {
    fun taskCompleted(isMajorRelease: Boolean) {
        println(productManager.isAllGood(isMajorRelease))
    }
}

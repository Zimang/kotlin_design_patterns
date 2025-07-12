package top.zimang.behavioral_pat

/**
 * 这一章节的亮点之一，这种设计模式为我们提供了通往后续专注于函数式编程的章节的桥梁。那么，观察者模式到底是什么呢？
 *
 * 你有一个发布者，也可以称为主题，可能有许多订阅者，也被称为观察者。
 *
 * 每当发布者发生有趣的事情时，所有订阅者都应该得到更新。
 *
 * 这看起来很像中介者设计模式，但有一个小变化。订阅者应该能够在运行时注册或取消注册自己。
 * 在经典实现中，所有订阅者/观察者都需要实现一个特定的接口，以便发布者更新它们。
 *
 * 但由于 Kotlin 具有高阶函数，我们可以省略这一部分。
 *
 * 发布者仍然需要提供一种方式让观察者能够订阅和取消订阅。这可能听起来有点复杂，所以让我们看一下以下示例。
 */
fun main() {
    val catTheConductor = Cat()

    val bat = Bat()
    val dog = Dog()
    val turkey = Turkey()

    catTheConductor.joinChoir(bat::screech)
    catTheConductor.joinChoir(dog::howl)
    catTheConductor.joinChoir(dog::bark)
    catTheConductor.joinChoir(turkey::gobble)

    catTheConductor.conduct()
    catTheConductor.conduct()
}

class Bat {
    fun screech() {
        println("Eeeeeee")
    }
}

class Turkey {
    fun gobble() {
        println("Gob-gob")
    }
}

class Dog {
    fun bark() {
        println("Woof")
    }

    fun howl() {
        println("Auuuu")
    }
}

class Cat {
    private val participants = mutableMapOf<() -> Unit, () -> Unit>()

    fun joinChoir(whatToCall: () -> Unit) {
        participants[whatToCall] = whatToCall
    }

    fun leaveChoir(whatNotToCall: () -> Unit) {
        participants.remove(whatNotToCall)
    }

    fun conduct() {
        for (p in participants.values) {
            p()
        }
    }
}

typealias Times = Int

enum class SoundPitch { HIGH, LOW }
interface Message {
    val repeat: Times
    val pitch: SoundPitch
}


data class LowMessage(override val repeat: Times) : Message {
    override val pitch = SoundPitch.LOW
}

data class HighMessage(override val repeat: Times) : Message {
    override val pitch = SoundPitch.HIGH
}


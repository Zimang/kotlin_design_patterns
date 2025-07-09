package top.zimang.structural_pat


import java.util.stream.Stream

/**
 * 适配器设计模式旨在将一个接口转换为另一个接口，就像我们在日常生活中遇到的电源插头适配器或USB适配器等物理世界的例子一样。
 *
 * 想象一下，深夜你身处一家酒店房间，手机电量只剩下7%。
 * 你的手机充电器在城市的另一端的办公室里，你只有一个带有Mini USB线的欧洲插头充电器。
 * 然而，你的手机需要USB-C接口，因为你不得不升级。
 * 更棘手的是，纽约的插座是USB-A接口。在这绝望的情况下，你迫切地寻找Mini USB转USB-C适配器，并希望你也带了欧洲转美国的插头转换器。
 * 时间紧迫，因为你的电量只剩下5%。正如适配器在物理世界中帮助我们一样，我们也可以在代码中应用同样的原则，实现不同接口之间的兼容性。
 *
 */

fun main() {
    // This code won't work
    //在我们尝试将手机、充电器和美国电源插座功能结合在一起时，由于USPlug、EUPlug、UsbMini和UsbTypeC接口彼此不兼容，我们遇到了类型错误。
    // 这就是适配器设计模式发挥作用的地方。
    // 通过创建将一个接口转换为另一个接口的适配器，我们可以弥合不同接口之间的差异，使它们变得兼容。
    // 这将使我们能够无缝地使用美国电源插座为手机充电，克服我们遇到的类型错误。
    /*
    cellPhone(
        // Type mismatch: inferred type is UsbMini but UsbTypeC was expected
        charger(
            // Type mismatch: inferred type is USPlug but EUPlug was expected
            usPowerOutlet()
        )
    )*/

    cellPhone(
        charger(
            usPowerOutlet().toEUPlug()
        ).toUsbTypeC()
    )


    //您可能已经遇到了许多适配器设计模式的用法。
    // 通常这些用于在概念和实现之间进行适配。
    // 例如，让我们来看一下 JVM 集合的概念和 JVM 流的概念。
    val l = listOf("a", "b", "c")

    //流是元素的惰性集合。即使这样做可能有道理，但你不能简单地将一个集合传递给接收流的函数
    //许多其他 Kotlin 对象都有适配器方法，通常以“to”作为前缀。
    // 例如，toTypedArray() 将列表转换为数组。这里的l.stream()用的就是适配器方法
    streamProcessing(l.stream())

    //它永远不会完成，因为Stream.generate()会生成一个无限的整数列表，可能导致OutOfMemoryError。
    // 因此，请谨慎行事，并明智地采用这种设计模式。
    val s = (Stream.generate { 42 }).toList()
    println(s)

    // Using an adapter in a wrong way may cause your program to never stop!
    // For example:
    /*
    println("Collecting elements")
    collectionProcessing(s.toList())
    */
}

fun <T> collectionProcessing(c: Collection<T>) {
    for (e in c) {
        println(e)
    }
}

fun <T> streamProcessing(stream: Stream<T>) {
    // Do something with stream
}

/**
 * Kotlin的扩展函数提供了一种简洁而优雅的方式来创建适配器，无需额外的类。
 * 让我们定义一个扩展函数，将美国插头适配为可以使用欧洲插座的形式
 *
 * 在扩展函数内部，关键字this指的是该函数正在扩展的对象，就好像我们在类定义内部实现方法一样。
 */
fun USPlug.toEUPlug(): EUPlug {
    val hasPower = if (this.hasPower == 1) "YES" else "NO"
    return object : EUPlug {
        // Transfer power
        override val hasPower = hasPower
    }
}

fun UsbMini.toUsbTypeC(): UsbTypeC {
    val hasPower = this.hasPower == PowerState.TRUE
    return object : UsbTypeC {
        override val hasPower = hasPower
    }
}

/**
 * 适配器将确保电源以适当的格式传输到我们的手机，使其与UsbTypeC接口兼容。
 * 让我们声明USPowerOutlet函数，在我们的代码中代表一个美国电源插座。
 * 这将是一个返回USPlug的函数
 *
 * 在 Kotlin 中，object 关键字有不同的用途。
 *
 * 1. 它可以在全局范围内创建单例对象，
 *
 * 2. 或者当与 companion 关键字一起在类内部使用时，提供一个定义静态函数的地方。
 *
 * 3. 此外，它还可以用于生成匿名类，这些类是临时创建的，通常用于以临时方式实现接口。
 *
 *
 */
// Power outlet exposes USPlug interface
fun usPowerOutlet(): USPlug {
    return object : USPlug {
        override val hasPower = 1
    }
}

// Charger accepts EUPlug interface and exposes UsbMini interface
fun charger(plug: EUPlug): UsbMini {
    return object : UsbMini {
        override val hasPower = if (plug.hasPower == "YES")
            PowerState.TRUE else PowerState.FALSE
    }
}

/**
 * 我们的目标是将美国电源插座的电能转换并传递到我们的手机上，这将由以下函数表示：
 */
fun cellPhone(chargeCable: UsbTypeC) {
    if (chargeCable.hasPower) {
        println("I've Got The Power!")
    } else {
        println("No power")
    }
}

/**
 * 让我们通过接口来探讨如何实现这一点。
 * 我们从USPlug接口开始，该接口假定电源表示为一个整数，其中1表示有电，任何其他值表示没有电：
 */
interface USPlug {
    val hasPower: Int
}

/**
 * 与美国插头相比，欧盟插头接口将电源视为字符串，可以是“YES”或“NO”，分别表示有电源或无电源。
 */
interface EUPlug {
    val hasPower: String // "YES" or "NO"
}

/**
 * 对于UsbMini接口，电源以名为PowerState的枚举表示，该枚举表示不同的电源状态：
 */
interface UsbMini {
    val hasPower: PowerState
}

enum class PowerState {
    TRUE, FALSE
}

interface UsbTypeC {
    val hasPower: Boolean
}

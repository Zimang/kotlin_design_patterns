# 了解结构模式

本章探讨了 Kotlin 中的结构模式，重点关注对象在更大结构中的组合和排列方式。

这些模式旨在简化系统中不同组件之间的交互和关系。

1. 我们将讨论如何增强对象的功能，而不会创建复杂的类层次结构。
2. 此外，我们还将探讨如何适应未来的变化并纠正过去所做的设计决策。
3. 此外，我们还将研究减少程序内存占用的技术。



## info

在各种编程语言中，包括 Kotlin，在操作数类型不同的情况下，运算符会展现出不同的行为。例如，+ 运算符对于数字类型执行加法运算，对于字符串类型执行字符串连接，这为定义不同类型的加法操作提供了灵活性，并增强了代码的表现力：

```kotlin
println(1 + 1) // Prints 2
println("1" + "1") // Prints 11
```

为了更好地理解operator关键字的意义，让我们来看一下以下两行代码：

```kotlin
val concatenatedList = listOf("a") + listOf("b")
println(concatenatedList) // [a, b]
```

像Scala这样的语言提供了非凡的灵活性，允许任何字符集作为运算符。这种赋权使用户能够为其类型定制运算符行为，从而产生简洁而富有表现力的代码。然而，定义任何运算符的能力引入了创建难以理解代码的潜力。当用户有自由使用各种字符作为运算符时，可能会导致代码难以理解，特别是对于不熟悉这些运算符的特定约定或含义的人来说。

这种无限制的灵活性虽然强大，但需要仔细考虑以保持代码的可读性和清晰度。

相反地，Java不支持运算符重载，这会阻止用户为运算符定义自定义行为。语言中缺乏运算符重载可能会限制，因为它限制了根据特定类型或功能定制运算符的能力。这种限制可能导致代码表达力不足且冗长，因为开发人员被迫依赖方法名称，而不是更自然和直观的运算符来执行某些操作。

运算符重载，当谨慎使用时，可以通过允许开发人员在逻辑上有意义的上下文中使用熟悉的符号来增强代码的可读性和可维护性，最终促进更自然和直观的编码体验。Kotlin在Java和Scala之间取得了平衡。**它允许对某些众所周知的操作进行重载，但对可以和不可以重载的内容施加了限制。**支持的运算符重载列表详尽，并可在官方Kotlin文档https://kotlinlang.org/docs/operator-overloading.html中找到。在重新审视提取的接口时，我们注意到其描述了有关数组/映射访问和赋值的基本操作。Kotlin引入了一项便利功能，称为运算符重载，为与自定义类型和类交互提供了语法糖，使其看起来像是本地类型一样。

装饰者设计模式非常强大，因为它允许我们动态地组合对象并添加新功能，而无需修改原始类。**在 Kotlin 中，使用 by 关键字可以简化装饰者模式的实现。然而，我们需要注意一些限制。首先，在使用装饰者时，我们只能访问对象的公共 API。使用装饰者隐藏了包装对象的身份和状态，使客户端代码无需关心包装对象的内部细节，从而确保客户端代码与装饰者及其附加行为进行交互。**

例如，如果你想高效地读取文件，你可以使用 BufferedReader。它接收另一个 Reader 作为构造函数参数，以添加缓冲功能：

```kotlin
val reader = BufferedReader(FileReader("/some/file"))
```

在这个例子中，BufferedReader 作为装饰器，包装了原始的 FileReader。它添加了缓冲行为以提高读取性能。这样，装饰器模式允许在不改变涉及对象的核心行为的情况下灵活地组合功能。



```kotlin
class RiotControlTrooper : StormTrooper() {
    override fun attackRebel(x: Long, y: Long) {
    	// Has an electric baton, stay away!
    }
}
class FlameTrooper : ShockTrooper() {
    override fun attackRebel(x: Long, y: Long) {
    	// Uses flametrower, dangerous!
    }
}
class ScoutTrooper : ShockTrooper() {
    override fun move(x: Long, y: Long) {
    	// Runs faster
    }
}

interface Trooper {
    fun move(x: Long, y: Long)
    fun attackRebel(x: Long, y: Long)
    fun shout(): String
}
```

### type aliasing

Kotlin允许我们为我们已知的类型提供不同的名称。这些特殊名称被称为别名。

要创建一个别名，我们使用一个新的关键字：typealias。

这样做之后，当我们使用move()方法时，我们可以使用Meters代替之前的Int。类型别名并不是新类型。当Kotlin程序被编译时，Meters会转换为Int，PointsOfDamage会转换为Long。

使用它们有两个好处：

1. 第一个好处是更清晰（就像在我们的情况中）。我们可以准确理解我们得到的值的含义。
2. 第二个好处是更简洁。类型别名帮助我们隐藏复杂的类型。

我们将在接下来的部分详细讨论这一点。让我们回到我们的StormTrooper类。现在是为Weapon和Legs接口提供一些实现的时候了。

### constants

在 Kotlin 中，常量和类型别名一样，都是在编译时被替换的值。它们的有效性在于在编译时就已知。

const 关键字告诉编译器，变量的值应被视为编译时常量，对常量的引用将在编译时被实际值替换。 

```kotlin
interface Trooper {
    fun move(x: Long, y: Long)
    fun attackRebel(x: Long, y: Long)
    fun retreat() {
    	println("Retreating!")
    }
}
```

### Secondary constructors

我们的代码确实实现了其目标。然而，如果我们能够通过允许直接将冲锋队员传递给构造函数来增强它，而不是像我们目前所做的那样将它们封装在列表中，那将是有利的。

```kotlin
val squad = Squad(bobaFett.copy(), bobaFett.copy(),
bobaFett.copy())
```

为了实现这一点，我们可以向我们的Squad类添加辅助构造函数。直到现在，我们一直在使用类的主构造函数。那就是在类名后面声明的构造函数。但是我们可以为一个类定义多个构造函数。我们可以在类体内使用构造函数关键字定义类的辅助构造函数：

```kotlin
class Squad(private val units: List<Trooper>): Trooper {
    constructor(): this(listOf())
    constructor(t1: Trooper): this(listOf(t1))
    constructor(t1: Trooper, t2: Trooper): this(listOf(t1,t2))
}
```

**与Java不同，每个构造函数无需重复类名。**这也意味着如果决定重命名类，需要进行的更改较少。请注意，

**每个次要构造函数必须调用主构造函数，类似于在Java中使用super关键字。**这种方法有其局限性，因为预测其他人可能希望提供的额外元素的确切数量变得具有挑战性。为了更优雅地解决这个问题，让我们看看Kotlin中的函数如何适应可变数量的参数。

### The varargs keyword

如果你熟悉Java，你可能考虑过“可变参数函数”的概念，能够接受任意数量相同类型的参数。在Java中，你会用省略号来表示参数：Trooper... units。在Kotlin中，通过vararg关键字提供了相应的功能。当我们将次要构造函数与varargs结合起来时，我们得到了下面这段代码片段，非常优雅：

```kotlin
class Squad(private val units: List<Trooper>): Trooper {
    constructor(vararg units: Trooper):
    this(units.toList())
    ...
}
```

### Lazy delegation

你可能会好奇，当两个线程同时尝试初始化 "图像 "时会发生什么。默认情况下，lazy() 函数带有同步功能。这意味着只有一个线程取得胜利，其他线程则耐心等待图像就绪。



1. 如果你觉得两个线程同时执行 lazy 代码块是可以接受的（也许这不会占用太多资源），你可以选择使用` lazy(LazyThreadSafetyMode.PUBLICATION)`。
2. 对于性能至关重要的情况，且您完全确定两个线程永远不会同时执行相同的代码块，您可以使用`LazyThreadSafetyMode.NONE`，它不提供任何线程安全机制。代理和委托是解决许多复杂挑战的非常有效的方法。

在接下来的章节中，我们将深入探讨这些概念，进一步阐明它们的实用性。

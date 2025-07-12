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

The combination of immutable objects, pure functions, and closures provides us with a powerful
tool for performance optimization. Just remember: nothing is free. We trade one resource, CPU
time, for another resource, which is memory. And it’s up to you to decide which resource is more
expensive in each case. Not only is the trade-off between CPU and memory important, but also
the better readability/understandability for the reader of the code. The next topic we’ll discuss
should help us with that further.

```kotlin
class Snail {
    internal var mood: Mood = Still(this)
    private var healthPoints = 10
        // That's all!
}

sealed class Mood : WhatCanHappen

class Still(private val snail: Snail) : Mood() {
    override fun seeHero() {
    	snail.mood = Aggressive
    }
    override fun getHit(pointsOfDamage: Int) {
    	// Same logic from before 
    }
    override fun calmAgain() {
    	// Return to Still state
    }
}
```



# 响应式和并发模式

本节重点介绍现代设计模式的方法，强调响应式和并发模式，以及更广泛的函数式编程范围。我们首先介绍函数式编程的基本原则以及这些概念在 Kotlin 中的实现方式。接下来，我们将研究 Kotlin 的并发原语，特别关注协程。一旦我们建立了对函数式编程和协程的深刻理解，我们将探讨如何将它们结合起来，从而能够创建并发数据结构。这些结构是精确控制数据流并支持改善并发代码结构的设计模式的关键。

## 介绍函数式编程

本章将讨论函数式编程的基本原则以及它们如何融入 Kotlin 编程语言。正如你将发现的那样，本章涵盖的一些概念在本书的早些部分已经探讨过了，因为在没有涉及数据不可变性和函数作为值等函数式编程概念之前，很难讨论语言的优势。

我们将从不同角度看待这些特性，并了解它们如何融入更广泛的函数式编程范式。在本章中，我们将涵盖以下主题：

*  函数式方法背后的原理 
* 不可变性 
* 函数作为值 
* 
* 递归 

完成本章后，您将了解函数式编程概念如何嵌入 Kotlin 语言以及何时使用它们。

### 函数式方法背后的原理 

函数式编程已经存在了很长时间，就像其他编程范式一样，比如过程式编程和面向对象编程。然而，在过去的15年里，它的流行度显著增加。其中一个重要的推动因素是CPU速度的停滞。

由于我们无法像过去那样提高CPU速度（随着晶The combination of immutable objects, pure functions, and closures provides us with a powerful
tool for performance optimization. Just remember: nothing is free. We trade one resource, CPU
time, for another resource, which is memory. And it’s up to you to decide which resource is more
expensive in each case. Not only is the trade-off between CPU and memory important, but also
the better readability/understandability for the reader of the code. The next topic we’ll discuss
should help us with that further.体管缩小到原子尺度，诸如量子隧道效应等量子效应开始干扰其功能），替代方案就是并行化我们的软件。事实证明，函数式编程特别擅长处理并行任务。多核处理器的演变本身就是一个引人入胜的话题，但我们在这里只会简要介绍一下。

自从至少上世纪80年代以来，工作站就拥有多个处理器，以支持并行运行来自不同用户的任务。由于工作站在这个时代非常庞大，它们不需要担心将所有东西塞进一个芯片中。它们有物理上分离的CPU。但是当多处理器在2005年左右进入消费市场时，有必要拥有一个可以并行工作的物理单元。这就是为什么我们的个人电脑或笔记本电脑中有一个芯片中的多个核心。

并行化并不是采用函数式编程的唯一原因。

以下是一些额外的好处：

1. 函数式编程倾向于纯函数，通常更容易理解和测试。
2. 采用函数式结构的代码通常采用更声明性的风格，侧重于“做什么”而不是“怎么做”，这可能是有利的。

Kotlin与函数式编程原则的契合使其成为这种编程风格的自然选择。它强调不可变对象和变量，用val表示，与函数式编程对不可变状态的关注相呼应，导致代码更安全、更可预测。在Kotlin中，函数被视为一等公民，允许它们被存储在变量中、作为参数传递，或从其他函数返回，这是函数式编程灵活性的关键方面。此外，Kotlin对高阶函数的支持，能够接受或返回其他函数，在各种函数式编程技术中起着至关重要的作用，包括组合和柯里化。

### 不可变性 

函数式编程的基石之一是不可变性的概念。这意味着一个对象在函数接收它作为输入时保持不变，直到函数生成输出为止。也许你会想，对象怎么可能会改变呢？为了澄清这一点，让我们来看一个简单的例子：

```kotlin
fun <T> printAndClear(list: MutableList<T>) {
    for (e in list) {
        println(e)
        list.remove(e)
    }
}
printAndClear(mutableListOf("a", "b", "c"))
```

代码最初会输出a，但随后会发生`ConcurrentModificationException`。这是因为for-each循环使用了一个迭代器（这是我们在上一章讨论过的一个主题）。在循环内修改列表会干扰其操作。这让我们思考：从一开始就防范这种运行时异常岂不是太棒了？接下来，我们将讨论不可变集合如何为我们提供这种保护水平。

#### 不可变集合

在第1章《Kotlin入门》中，我们强调了Kotlin的集合按惯例是不可变的，这使其与许多其他语言不同。举个例子，创建新列表的最简单方式是listOf()，它创建的列表是不可变的。我们在前一节遇到的问题源于未遵守单一责任原则。这个原则建议函数应专注于一个单一任务并有效地执行它。在我们的情况下，该函数试图同时从列表中删除元素并打印它们，导致了复杂性。将参数类型从`MutableList`切换为`List`使我们无法使用`remove()`函数，从而有效地解决了手头的问题。

在函数式编程中，通常建议避免不返回值的函数，因为这通常意味着存在副作用。此外，这类函数的影响不容易进行测试，可能会导致问题，比如在模拟调用上进行验证，有时甚至使函数根本无法进行测试。

但拥有不可变的集合类型并不是全部。集合内部的内容也应该是不可变的。为了探讨这个概念，让我们考虑一个简单的类：

```kotlin
data class Player(var score: Int) 
val scores = listOf(Player(0), Player(0))
scores[0].score++
scores[0].score = 2
```

尽管我们无法添加或移除玩家，但我们仍然可以改变每个特定玩家的分数，尽管这很有用，但可能并不理想，我们将在下一节中看到。

#### 共享可变状态的陷阱

如果你对线程不熟悉，别担心，我们会在第6章《线程和协程》中详细讨论它们。你现在需要知道的是，线程允许代码并发运行。在使用并发代码和利用多个CPU的代码时，函数式编程确实很有帮助。你可能会发现，任何不涉及并发的例子看起来都相当复杂或人为。

```kotlin
val threads = List(2) {
    thread {
        for (i in 1..1000) {
        	scores[0].score++
        }
    }
}
for (t in threads) {
	t.join()
}
println(scores[0].score) // Less than 2000 for sure

```

需要注意的是，这个问题并不是由使用递增（++）运算符引起的。我们使用更明确的长记法来递增数值，但是多次运行代码仍然会产生不正确的结果。这种不一致性的根本原因在于加法和赋值操作都不是原子操作。

该操作包括多个步骤：

1. 读取变量：将变量的当前值读入CPU中的临时寄存器。
2. 增加数值：临时寄存器中的值增加一。
3. 写回数值：增加后的值被写回到变量的内存位置。

这意味着两个线程可能会干扰彼此的操作，导致增量数量不足。在这个例子中，我们使用了一个只有一个元素的简化情况。在现实场景中，你通常会处理包含多个元素的集合。例如，你可能会追踪多个玩家的得分或管理成千上万玩家的排名系统，使情况变得更加复杂。

**这里的关键是不可变集合可以包含可变对象。这些可变对象本质上缺乏线程安全性。**在第6章《线程和协程》中，我们将探讨安全递增计数器的替代方法，研究原子对象的使用。接下来，让我们探讨元组，它们是不可变对象的一个很好的例子。

#### 元组

在函数式编程中，元组是一种数据结构，在创建后无法更改。在 Kotlin 中，最基本的元组之一是 pair：

```kotlin
val pair = "a" to 1
```

这对有两个属性，分别称为第一个和第二个，并且它们是不可变的：

```kotlin
pair.first = "b" // Doesn't work
pair.second = 2  // Still doesn't
```

我们可以使用解构声明将一对值解构为两个单独的值：

```kotlin
val (key, value) = pair
println("$key => $value")
```

除了成对，还有一个三元组，它包含第三个值：

```kotlin
val firstEdition = Triple("Design Patterns with Kotlin", 310, 2018)
val secondEdition = Triple("Design Patterns with Kotlin, 2nd Edition",330, 2020)
```

通常，数据类通常适合用于实现元组，因为它们提供了描述性的命名。例如，在上面的例子中，很难立即看出310和330代表的是页数。然而，并非所有数据类都适合用作元组。必须确保其所有成员都是不可变值，而不是可变变量。此外，其中的任何嵌套集合或类也应该是不可变的。接下来，让我们将注意力转向函数式编程的另一个重要方面：将函数视为语言的一等公民。

### 函数作为值 

我们在早前关于设计模式的章节中提到了Kotlin的函数式特性。例如，策略模式和命令模式在很大程度上依赖于Kotlin处理函数的能力，比如将函数作为参数接受、从其他函数返回函数、将函数存储为值，甚至将函数包含在集合中。

在本节中，我们将探讨Kotlin中函数式编程的其他方面，包括函数纯度和柯里化等概念。正如我们之前讨论过的，Kotlin中函数可以返回另一个函数。让我们看下面这个简单函数，以深入理解这种语法：

```kotlin
fun generateMultiply(): (Int, Int) -> Int {
    return fun(x: Int, y: Int): Int {
    	return x * y
    }
}
```

在这里，我们的 generateMultiply 函数返回另一个没有名称的函数。没有名称的函数被称为匿名函数。

#### 标准库中的高阶函数

在使用 Kotlin 时，你每天都会与集合打交道。正如我们在第一章《Kotlin 入门》中简要提到的那样，集合支持高阶函数。例如，在之前的章节中，为了逐个打印集合的元素，我们使用了一个单调乏味的 for-each 循环：

```kotlin
val dwarfs = listOf("Dwalin", "Balin", "Kili", "Fili", "Dori", "Nori","Ori", "Oin", "Gloin", "Bifur", "Bofur", "Bombur", "Thorin")
for (d in dwarfs) {
	println(d)
}
```

也许有些人看到这段糟糕的代码会叹气。但我希望这个例子不会让你停止阅读这本书！当然，还有另一种在许多编程语言中常见的实现相同目标的方法：使用forEach函数：

```kotlin
dwarfs.forEach { d The combination of immutable objects, pure functions, and closures provides us with a powerful
tool for performance optimization. Just remember: nothing is free. We trade one resource, CPU
time, for another resource, which is memory. And it’s up to you to decide which resource is more
expensive in each case. Not only is the trade-off between CPU and memory important, but also
the better readability/understandability for the reader of the code. The next topic we’ll discuss
should help us with that further.->
	println(d)
}
fun <T> Iterable<T>.forEach(action: (T) -> Unit)
```

#### 闭包

在面向对象的编程范式中，状态始终存储在对象内部。但在函数式编程中，情况并非一定如此。让我们以以下函数为例：

```kotlin
fun counter(): () -> Int {
    var i = 0
    return { i++ }
}
```

前面的例子显然是一个高阶函数，你可以通过它的返回类型看出来。它返回一个没有参数的函数，该函数产生一个整数。让我们按照我们已经学过的方法，将它存储在一个变量中，并多次调用它：

正如你所看到的，该函数能够保持状态，比如一个计数器的值，即使它不是对象的一部分。这被称为闭包。Lambda 函数可以访问包裹它的函数的所有局部变量，并且只要对 lambda 的引用被保留，这些局部变量就会持续存在。闭包的使用是函数式编程工具箱中的另一个工具，它减少了需要定义许多仅用于包装带有一些状态的单个函数的类的需求。

#### 纯函数

一个纯函数是指没有副作用的函数。在这个语境中，副作用指的是任何修改或访问外部状态的行为。

这个外部状态可以是非局部变量（即使是闭包中的变量在这种情况下也被视为非局部变量），或者任何形式的输入/输出操作，比如从文件读取或写入，或者利用网络资源。一个纯函数应该对相同的输入返回相同的结果。

举个例子，在闭包部分我们刚讨论过的 lambda 函数并不被视为纯函数，因为当它被多次调用时，可能会针对相同的输入返回不同的输出。

不纯的函数通常很难测试和理解，因为它们返回的结果可能取决于执行顺序或我们无法控制的因素（如网络问题）。要记住的一点是，即使是记录日志或打印到控制台，也涉及IO并受到相同一组问题的影响。让我们看看以下简单函数：

```kotlin
The combination of immutable objects, pure functions, and closures provides us with a powerful
tool for performance optimization. Just remember: nothing is free. We trade one resource, CPU
time, for another resource, which is memory. And it’s up to you to decide which resource is more
expensive in each case. Not only is the trade-off between CPU and memory important, but also
the better readability/understandability for the reader of the code. The next topic we’ll discuss
should help us with that further.fun sayHello() = println("Hello")

fun hello() = "Hello"
fun testHello(): Boolean {
	return "Hello" == hello()
}
```





```kotlin
//可变
fun <T> removeFirst(list: MutableList<T>): T {
	return list.removeAt(0)
}

val list = mutableListOf(1, 2, 3)
println(removeFirst(list)) // Prints 1
println(removeFirst(list)) // Prints 2

//不可变
fun <T> withoutFirst(list: List<T>): T {
	return ArrayList(list).removeAt(0)
}

val list = mutableListOf(1, 2, 3)
println(withoutFirst(list)) // It's 1
println(withoutFirst(list)) // Still 1

//日志
//1
//2
//1
//1
```

正如你所看到的，在这个例子中，我们使用了一个不可变接口 `List<T>`，它通过防止输入的变异可能性来帮助我们。当与我们在前一节讨论过的不可变值结合使用时，纯函数可以通过提供可预测的结果和算法的并行化来更容易地进行测试。利用纯函数的系统更容易推理，因为它不依赖于任何外部因素 - 你看到的就是你得到的。

#### 柯里

柯里化是一种技术，它将接受多个参数的函数转换为一系列只接受一个参数的函数。如果这听起来有点抽象，让我们用一个简单的例子来澄清一下：

```kotlin
fun subtract(x: Int, y: Int): Int {
	return x - y
}
println(subtract(50, 8))
```

这是一个接受两个参数作为输入并返回它们之间差异的函数。然而，有些语言允许我们以以下语法调用这个函数：

```kotlin
subtract(50)(8)
```

这就是柯里化的样子。柯里化允许我们将一个具有多个参数（在我们的例子中是两个）的函数转换为一组函数，其中每个函数只接受一个参数。让我们看看在 Kotlin 中如何实现这一点。我们已经看到了如何从另一个函数中返回一个函数：

```
fun subtract(x: Int): (Int) -> Int {
    return fun(y: Int): Int {
    	return x - y
    }
}
//shorter
fun subtract(x: Int) = fun(y: Int): Int {
	return x - yThe combination of immutable objects, pure functions, and closures provides us with a powerful
tool for performance optimization. Just remember: nothing is free. We trade one resource, CPU
time, for another resource, which is memory. And it’s up to you to decide which resource is more
expensive in each case. Not only is the trade-off between CPU and memory important, but also
the better readability/understandability for the reader of the code. The next topic we’ll discuss
should help us with that further.
}
//even shorter
fun subtract(x: Int) = {y: Int -> x - y}
```

虽然单独来看可能用处不大，但这个概念还是很有趣的。如果你是一名寻找新工作的JavaScript开发者，务必要彻底理解它，因为几乎每次面试都会问到这个问题。

在现实世界中，你可能想要使用柯里化的一个场景是日志记录。日志函数通常看起来像这样：

```
enum class LogLevel {
	ERROR, WARNING, INFO
}
fun log(level: LogLevel, message: String) = println("$level: $message")

fun createLogger(level: LogLevel): (String) -> Unit {
    return { message: String ->
    	log(level, message)
    }
}

val infoLogger = createLogger(LogLevel.INFO)
infoLogger("Log something")
```

有趣的是，这种方法与我们在第2章“使用创建型模式”中讨论的工厂设计模式有很大相似之处。像 Kotlin 这样的现代语言的功能可以减少创建多个自定义类来实现类似功能的需求。现在，让我们继续探讨另一种有用的技术，可以避免我们反复执行相同的计算。

#### 备忘录技术

如果我们的函数对于相同的输入总是返回相同的输出，我们可以轻松地将其输入映射到输出，并在过程中缓存结果。这种技术称为备忘录。在开发不同类型的系统或解决问题时，一个常见的任务是找到一种方法来避免多次重复相同的计算。假设我们收到多个整数列表，并且对于每个列表，我们想要打印其总和：

```kotlin
fun summarizer(): (Set<Int>) -> Double {
    val resultsCache = mutableMapOf<Set<Int>, Double>()

    return { numbers: Set<Int> ->
        resultsCache.computeIfAbsent(numbers, ::sum)
    }
}

fun sum(numbers: Set<Int>): Double {
    println("Computing")
    return numbers.sumOf { it.toDouble() }
}

fun main() {
    val input = listOf(
        setOf(1, 2, 3),
        setOf(3, 1, 2),
        setOf(2, 3, 1),
        setOf(4, 5, 6)
    )

    val summarizer = summarizer()

    input.forEach {
        println(summarizer(it))
    }
}
```

不可变对象、纯函数和闭包的结合为我们提供了一个强大的性能优化工具。只要记住：没有免费的午餐。我们用一种资源，即CPU时间，来换取另一种资源，即内存。在每种情况下，由你决定哪种资源更昂贵。不仅CPU和内存之间的权衡重要，对于代码读者来说，更好的可读性/理解性也很重要。我们将讨论的下一个主题应该进一步帮助我们解决这个问题。

### 使用表达式而非语句 

语句是一块不返回任何内容的代码。相反，表达式会返回一个新值。

由于语句不产生结果，它们有用的唯一方式是改变状态，无论是改变变量、改变数据结构，还是执行某种IO操作。函数式编程尽可能避免改变状态。

理论上，我们越依赖表达式，我们的函数就会更纯净，享有函数式纯度的所有好处。这也提高了可测试性。我们已经多次使用了if表达式，因此它的一个好处应该很明显：它比其他语言的if语句更简洁，因此更不容易出错。现在让我们看看if语句的替代方案，称为模式匹配。

```kotlin
fun main() {
    println(getSound(Cat()))
    println(getSound(Dog()))

    try {
        println(getSound(Crow()))
    }
    catch (e: IllegalStateException) {
        println(e)
    }
}

fun getSound(animal: Animal): String {
    var sound: String? = null;

    if (animal is Cat) {
        sound = animal.purr();
    } else if (animal is Dog) {
        sound = animal.bark();
    }

    if (animal is Cat) {
        sound = animal.purr();
    } else if (animal is Dog) {
        sound = animal.bark();
    checkNotNull(sound)

    return sound;
}

class Cat : Animal {
    fun purr(): String {
        return "Purr-purr";
    }
}

class Dog : Animal {
    fun bark(): String {
        return "Bark-bark";
    }
}

class Crow : Animal {
    fun caw(): String {
        return "Caw! Caw!";
    }
}

interface Animal 
```

自从有了表达式，我们完全避免了之前所拥有的中间变量的声明。此外，使用模式匹配的代码不需要任何类型检查或强制转换。现在我们已经学会了如何用更加函数式的when表达式替换命令式的if语句，让我们看看如何通过递归来替换代码中的命令式循环。 

```kotlin
fun getSound(animal: Animal): String = when(animal) {
	is Cat -> animal.purr()
	is Dog -> animal.bark()
	else -> throw RuntimeException("Unknown animal")
}
```



### 递归

递归是指函数用新参数调用自身。许多著名的算法，比如深度优先搜索，都依赖于递归。以下是一个非常低效的函数示例，它使用递归来计算给定列表中所有数字的总和：

```kotlin
fun sumRec(i: Int, sum: Long, numbers: List<Int>): Long {
    return if (i == numbers.size) {
    	return sum
    } else {
    	sumRec(i+1, numbers[i] + sum, numbers)
    }
}
```

我们经常尽量避免递归，因为如果我们的调用栈太深，可能会收到堆栈溢出错误。你可以用包含一百万个数字的列表调用这个函数来演示这一点：

```kotlin
tailrec fun sumRec(i: Int, sum: Long, numbers: List<Int>): Long {
    return if (i == numbers.size) {
        return sum
    } else {
        sumRec(i + 1, numbers[i] + sum, numbers)
    }
}
```

现在，编译器将优化我们的调用并完全避免异常。然而，如果存在多个递归调用，比如在归并排序算法中，这种优化就不起作用了。让我们来看一下下面这个函数，它是归并排序算法中的排序部分：

```kotlin
tailrec fun mergeSort(numbers: List<Int>): List<Int> {
    return when {
        numbers.size <= 1 -> numbers
        numbers.size == 2 -> {
            return if (numbers[0] < numbers[1]) {
            	numbers
            } else {
            	listOf(numbers[1], numbers[0])
            }
        }else -> {
            val left = mergeSort(numbers.slice(0..numbers.size / 2))
            val right = mergeSort(numbers.slice((numbers.size / 2 + 1) ..< numbers.size))
            return merge(left, right)
        }
    }
}
```

请注意这里有两个递归调用而不是一个。Kotlin编译器会发出以下警告：

> "A function is marked as tail-recursive but no tail calls are found"

此外，从命令式编程到声明式编程的范式转变需要一定的学习曲线，且在与命令式代码库集成时可能会面临挑战。

我们介绍了 Kotlin 如何支持闭包，允许函数访问其周围函数的变量，从而在多次运行之间保留状态。

这有助于实现柯里化和记忆化等技术，使我们能够设置默认函数参数并缓存先前计算的函数值，避免冗余计算。

我们提到了 Kotlin 使用尾递归关键字，为尾递归函数启用编译器优化。

我们讨论了高阶函数、表达式和语句之间的区别以及模式匹配等主题。这些特性共同有助于创建不仅更易于测试而且更不容易出现并发相关问题的代码。在接下来的章节中，我们将在实际环境中应用这些概念，探讨响应式编程如何利用函数式编程的基础来开发可扩展和健壮的系统。

## 线程与协程

这一章将会非常精彩，因为我们将深入探讨 Kotlin 中并发的领域。你可能还记得在上一章中，我们提到了如何让我们的应用程序能够高效地处理成千上万的请求。为了说明不可变性的重要性，我们向你介绍了使用两个线程来展示竞态条件的概念。在这一章中，我们将扩展这一理解，并探讨以下内容：

1. 深入探讨线程：Kotlin中线程是如何工作的，使用线程的优缺点是什么？
2. 介绍协程：什么是协程，挂起函数如何促进协程？
3. 启动协程：如何启动新协程，有哪些不同的方式？
4. 任务：了解协程背景下的任务是什么，以及它们如何帮助管理并发操作。
5. 协程内部机制：Kotlin编译器如何处理协程，字节码级别会发生什么？
6. 调度器：了解调度器在决定协程运行在哪个线程上的作用。
7. 结构化并发：什么是结构化并发，如何帮助防止资源泄漏？

到本章结束时，您将对Kotlin的并发原语有扎实的了解，并且知道如何在应用程序中有效地利用它们。

### 深入探讨线程

 在Java虚拟机（JVM）中，线程作为并发的主要单元，允许代码并发执行以最大化CPU核心利用率。相比进程，线程更轻量级，一个进程可以生成大量线程。虽然基于线程的数据共享相对于进程更简单，但也引入了后续将要探讨的独特挑战。

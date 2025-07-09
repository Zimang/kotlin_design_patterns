package top.zimang.structural_pat
import java.lang.IllegalStateException

/**
 * 装饰者设计模式非常强大，因为它允许我们动态地组合对象并添加新功能，而无需修改原始类。
 *
 * **在 Kotlin 中，使用 by 关键字可以简化装饰者模式的实现。**
 *
 * 然而，我们需要注意一些限制。首先，在使用装饰者时，我们只能访问对象的公共 API。使用装饰者隐藏了包装对象的身份和状态，使客户端代码无需关心包装对象的内部细节，从而确保客户端代码与装饰者及其附加行为进行交互。**
 *
 * 例如，如果你想高效地读取文件，你可以使用 BufferedReader。它接收另一个 Reader 作为构造函数参数，以添加缓冲功能：
 *
 * ```kotlin
 * val reader = BufferedReader(FileReader("/some/file"))
 * ```
 *
 * 在这个例子中，BufferedReader 作为装饰器，包装了原始的 FileReader。它添加了缓冲行为以提高读取性能。这样，装饰器模式允许在不改变涉及对象的核心行为的情况下灵活地组合功能。
 */
fun main() {
    val starTrekRepository = DefaultStarTrekRepository()
    starTrekRepository()
    println(starTrekRepository[""])
    val withValidating = ValidatingAdd(starTrekRepository)
    val withLoggingAndValidating = LoggingGetCaptain(withValidating)

    withLoggingAndValidating["USS Enterprise"]

//    try {
//        // Throws an exception: Kathryn Janeway name is longer than 15 characters!
//        withLoggingAndValidating["USS Voyager"] = "Kathryn Janeway"
//    } catch (e: IllegalStateException) {
//        println(e)
//    }

    println(withLoggingAndValidating is LoggingGetCaptain) // This is our top level decorator, no problem here
    println(withLoggingAndValidating is StarTrekRepository) // This is the interface we implement, still no problem
    println(withLoggingAndValidating is ValidatingAdd) // We wrap this class, but compiler cannot validate it
    println(withLoggingAndValidating is DefaultStarTrekRepository) // We wrap this class, but compiler cannot validate it
}

interface StarTrekRepository {
    /**
     * 在 Kotlin 中，我们已经在函数定义前添加了 operator 关键字。
     * 这个关键字非常重要，因为它表示我们正在重载一个操作符，允许我们为其定义自定义行为。
     * 这个改变使我们能够使用类似数组的语法来访问和赋值我们自定义数据结构中的值，使我们的代码更易读和简洁。
     */
    operator fun get(starshipName: String): String
    operator fun set(starshipName: String, captainName: String)
}

/**
 * 让我们考虑一个简单的类，它记录了《星际迷航》宇宙中所有舰队舰长及其舰船的信息
 */
class DefaultStarTrekRepository : StarTrekRepository {

    private val starshipCaptains = mutableMapOf("USS Enterprise" to "Jean-Luc Picard")

    /**
     * 如果不用Elvis
     * ```
     * open fun getCaptain(starshipName: String): String {
     *      return if (starshipCaptains[starshipName] != null) {
     *          starshipCaptains[starshipName]
     *      } else {
     *          "Unknown"
     *      }
     * }
     * ```
     */
    override fun get(starshipName: String): String {
        //如果我们将 Elvis 运算符顺时针旋转90度，它看起来有点像一个波波头发型。
        return starshipCaptains[starshipName] ?: "Unknown"
    }

    /**
     * 操作符重载
     * ```
     * a[i] = b
     * a.set(i, b)
     * ```
     */
    override fun set(starshipName: String, captainName: String) {
        starshipCaptains[starshipName] = captainName
    }

    operator fun invoke(){
        println("invoke default ")
    }
}

/**
 * # 非decorator
 * 有一天，你的队长——抱歉，Scrum Master——找到你，提出了一个紧急需求。
 * 他们要求每当有人搜索队长时，你必须将这些信息记录到控制台中。
 *
 * 然而，这个看似简单的任务有一个难题：**你不能直接修改StarTrekRepository类。** 假设这个类StarTrekRepository是DefaultStarTrekRepository的open版
 *
 * 这是因为这个类还有其他使用者，并不需要这种记录日志的行为。
 *
 * 由于我们的类和方法被声明为开放的，我们可以轻松地扩展该类并覆盖我们需要的特定函数。
 * **这使我们能够引入所需的日志记录行为**，而无需直接修改原始的StarTrekRepository类，这可能会影响其他不需要此额外功能的消费者。
 * 通过使用继承和方法覆盖，我们可以实现新的行为，同时保持原始类对代码库其他部分的完整性。
 *
 * 扩展这个类很简单，但类名变得明显更长了。
 *
 * 此外，扩展后的名字暗示可能违反了单一职责原则，即一个类应该只负责一个职责，确保它只因一个原因而发生变化。
 *
 * # decorator
 * 我们不再扩展具体实现，而是使用关键字 by 来实现接口，这是装饰者设计模式的一个关键方面。
 *
 * 通过 by，我们将装饰对象的行为委托给另一个对象，有效地组合我们的对象。这样可以动态地添加新功能，而无需修改原始类。
 * 通过关键字简化了将接口实现委托给另一个对象的过程，消除了LoggingGetCaptain类需要显式实现接口中声明的函数的需要。
 * 这些函数会被实例包装的对象自动执行。
 *
 * 对于装饰器设计模式，需要具备以下能力：
 *
 * 1. 接收被装饰的对象。
 *
 * 2. 保持对对象的引用。
 *
 * 3. 在装饰器调用期间决定是否改变被包装对象的行为或委托调用。
 *
 * 4. 可选择提取接口或使用库作者提供的接口。
 *
 * 在应用装饰者模式时，不再需要使用super关键字，因为我们现在是在实现一个接口。
 * 相反，我们访问被包装接口的引用。
 */
class LoggingGetCaptain(private val repository: StarTrekRepository) : StarTrekRepository by repository {
    //a[i] <====> a.get(i)  这里存在一个符号重载
    override fun get(starshipName: String): String {
        println("Getting captain for $starshipName")
        return repository[starshipName]
    }
}

/**
 * # 非decorator
 * 扩展这个类很简单，但类名变得明显更长了。
 * 此外，在这种新情况下，你的老板（抱歉，Scrum Master）要求添加另一个功能。
 * 当添加一个队长时，需要进行验证检查，以确保他们的名字不超过15个字符。
 * 虽然这可能对一些克林贡人造成问题，但你决定无论如何实现这个功能。
 *
 *
 * 额外的验证功能不应与我们之前开发的日志记录功能相关联。这意味着我们应该有灵活性根据需要分别使用日志记录和验证功能。
 * 这就是我们新类的样子：
 * ```
 * class ValidatingAddCaptainStarTrekRepository :StarTrekRepository() {
 *      override fun addCaptain(starshipName: String,captainName: String) {
 *          if (captainName.length > 15) {
 *              throw RuntimeException("$captainName is longer than 15 characters!")
 *          }
 *          super.addCaptain(starshipName, captainName)
 *      }
 * }
 * ```
 *
 * 随着新需求不断出现，很明显类名变得相当繁琐。
 * 不得不在类名中包含多个行为是一个明显的迹象，表明我们可能会受益于采用设计模式来动态处理这些变化。
 *
 * 装饰者设计模式在这种情况下可以非常有用。它的目的是动态地为对象添加新的行为。在我们的情况下，我们有两种行为，即日志记录和验证，有时我们想应用它们，有时则不想。
 *
 * # decorator
 *
 * 在之前的示例和ValidatingAddCaptainStarTrekRepository的实现之间的主要区别在于使用require()函数而不是if表达式。
 *
 * 使用require()通常会导致更易读的代码，并且如果提供的表达式求值为false，它还具有抛出IllegalArgumentException的额外好处。
 */
class ValidatingAdd(private val repository: StarTrekRepository) : StarTrekRepository by repository {
    private val maxNameLength = 15
    override fun set(starshipName: String, captainName: String) {
        require(captainName.length < maxNameLength) {
            "$captainName name is longer than $maxNameLength characters!"
        }

        repository[starshipName] = captainName
    }
}


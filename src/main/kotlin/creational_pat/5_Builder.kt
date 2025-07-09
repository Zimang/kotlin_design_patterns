package top.zimang.creational_pat

/**
 * 编程中对象创建的复杂性可以有很大的变化。
 * 有些情况下，对象很简单，只有一个构造函数，可以是空的或带参数的。
 * 然而，也有一些情况下对象的创建变得复杂，涉及多个参数。
 * 对于更复杂的情况，我们可以转向建造者设计模式。
 * 这种模式在处理需要大量参数的对象时特别有用，其中一些参数可能是可选的。
 * 它提供了一种清晰灵活的方式来构建这样复杂的对象。
 * 为了说明这一点，让我们以设计一个表示电子邮件的类为例。
 * 在这种情况下，我们不会讨论发送电子邮件的实际机制，而是专注于构建一个电子邮件对象的细微之处，
 *      这个对象可能包括收件人、主题、正文、附件等各种字段。
 *
 * 一封电子邮件可能具有以下属性：
 * 1. 地址（至少一个是必需的）
 * 2. 抄送（可选）
 * 3. 标题（可选）
 * 4. 正文（可选）
 * 5. 重要标志（可选）
 * 我们可以在系统中将电子邮件描述为一个数据类：
 * ```
 * data class RegularMail(
 *  val to: List<String>,
 *  val cc: List<String>?,
 *  val title: String?,
 *  val message: String?,
 *  val important: Boolean,
 * )
 * ```
 */


fun main() {
    withoutBuilderExample()
    builderExample()
    fluentExample()
    applyExample()
    defaultsExample()
}

fun defaultsExample() {
    val mail = MailDefaults(
        title = "Hello",
        message = "There",
        important = true,
        to = listOf("my@dear.cat")
    )
}

fun applyExample() {
    val mail = MailApply(listOf("manager@company.com")).apply {
        message = "You’ve been promoted"
        title = "Come to my office"
    }
}

fun fluentExample() {
    val mail = MailFluent(
        listOf("manager@company.com")
    ).message("Ping")
}

fun builderExample() {
    val mail = MailBuilder()
        .recepients(listOf("hello@world.com"))
        .message("Hello")
        .build()

    println(mail.to)
    println(mail.cc)
    println(mail.message)
}

fun withoutBuilderExample() {
    val mail = RegularMail(
        listOf("manager@company.com"), // To
        null,                      // CC
        "Ping",                   // Title
        null,                 // Message,
        true                  // Important
    )
}

/**
 * 一封电子邮件可能具有以下属性：
 * 1. 地址（至少一个是必需的）
 * 2. 抄送（可选）
 * 3. 标题（可选）
 * 4. 正文（可选）
 * 5. 重要标志（可选）
 * 我们可以在系统中将电子邮件描述为一个数据类
 *
 * 看一下前面代码片段中最后一个参数的定义。后面的逗号是故意的，被称为尾随逗号。
 * 尾随逗号是在 Kotlin 1.4 中引入的，以便更轻松地操作参数列表，比如重新排序参数。
 * 此外，当添加新属性时，它们有助于减少 Git 提交中的差异大小，使版本控制更清晰。
 * 有关此约定的更多详细信息，请参考 Kotlin 的编码指南：https://kotlinlang.org/docs/coding-conventions.html#trailing-commas
 *
 * ```
 * val mail = RegularMail(
 * listOf("manager@company.com"), // To
 * null,// CC
 * "Ping",// Title
 * null,// Message,
 * true// Important
 * )
 * ```
 * 请注意，我们已将抄送字段（由缩写CC表示）定义为可空，允许其接收电子邮件列表或空值。
 * 另一种选择是将其定义为List<String>，并要求代码显式传递listOf()。
 * 考虑到我们的构造函数接收了许多参数，我们已添加了注释以避免混淆。
 * 然而，如果我们现在需要修改这个类，例如添加另一个字段，会发生什么呢？
 *  首先，我们的代码将无法编译通过。
 *  其次，我们必须确保相应更新注释。
 *  总之，具有长参数列表的构造函数很快变得混乱且难以管理。
 */
data class RegularMail(
    val to: List<String>,
    val cc: List<String>?,
    val title: String?,
    val message: String?,
    val important: Boolean,
)

/**
 * 我们的构建器类具有与我们的结果类相同的属性，但这些属性都是可变的。
 *
 * 虽然这个建造者模式很有效，但也存在一些局限性：
 *
 * • 最终的邮件类的属性需要在邮件构建者中复制，导致代码重复。
 *
 * • 每个属性都需要一个单独的设置函数。
 *
 * • 邮件构建者内的可变变量引发了线程安全性和意外修改这些属性的担忧，超出了预期的建造者模式。
 *
 * Kotlin幸运地提供了两种替代方法，可能更有益，并且可以减轻一些这些不利因素。
 *
 * 通过在Kotlin中将默认参数与命名参数结合起来，简化了创建复杂对象的过程。
 * 由于这种便利性，你会发现在Kotlin中，对于Builder设计模式的需求大大降低了。
 * 在现实世界的应用中，当构建服务器实例时，通常会采用Builder设计模式。
 * 例如，服务器可能接受可选参数，如主机和端口，一旦设置了所有必要的参数，就可以通过调用listen方法来启动服务器。
 * Builder模式的这种用法可以更灵活、更容易地配置和初始化服务器对象。
 */
class MailBuilder {
    private var recepients: List<String> = listOf()
    private var cc: List<String> = listOf()
    private var title: String = ""
    private var message: String = ""
    private var important: Boolean = false

    /**
     * 重要的是要注意在Mail类的构造函数上使用了内部可见性修饰符。 internal
     * 这个修饰符是Kotlin中的一个独特特性，在Java中找不到。它表示Mail类只能在同一个模块内访问。
     *
     * data class 默认生成：
     *
     *     equals() / hashCode() / toString()
     *
     *     componentN() 解构函数
     *
     *     copy(...) ✅ 重点
     *
     * 而你构造函数是 internal 的，但生成的 copy() 方法默认是 public —— 所以你在用代码隐藏构造时，却留下了一个可公开访问的“入口”。
     *
     * 这就会带来两个问题：
     * 1. API 暴露不一致（你想封装它，但 copy 又开放了）
     *
     * 2. 将来 Kotlin 编译器可能调整 copy() 的默认可见性以保持一致性: 现在是 warning，将来可能变 error 或行为改变
     *
     * 解决问题的方法
     * 1. 添加 @ConsistentCopyVisibility 作用：告知编译器你已确认构造器和 copy 的可见性不一致，是你有意为之，抑制 warning。虽然 copy() 仍是 public，但这是目前官方认可的安全压制 warning做法。 推荐 ✅。
     * 2. 使用编译器 flag（作用全局）
     *      ```
     *      kotlin {
     *          compilerOptions {
     *              freeCompilerArgs += "-Xconsistent-data-class-copy-visibility"
     *          }
     *      }
     *      ```
     * 3. @ExposedCopyVisibility（不推荐）:这个注解是为了兼容旧代码而设置的，它告诉 Kotlin 保留当前行为（public copy()），但官方不推荐继续使用。
     */
    data class Mail internal constructor(
        val to: List<String>,
        val cc: List<String>?,
        val title: String?,
        val message: String?,
        val important: Boolean
    )

    /**
     * ```
     * val mail = MailBuilder()
     *      .recepients(listOf("hello@world.com"))
     *      .message("Hello")
     *      .build()
     * ```
     */
    fun build(): Mail {
        if (recepients.isEmpty()) {
            throw RuntimeException("To property is empty")
        }

        return Mail(recepients, cc, title, message, important)
    }

    fun message(message: String = ""): MailBuilder {
        this.message = message
        return this
    }

    fun recepients(recepients: List<String>): MailBuilder {
        this.recepients = recepients
        return this
    }

    // More functions to be implemented here
}

/**
 * 利用流畅的设置器方法更为简洁。
 * 在这种方法中，我们无需创建额外的类。
 * 相反，我们的数据类构造函数将仅包括必填字段。
 * 其余字段将是私有的，我们将为这些字段提供设置器
 *
 * 在 Kotlin 中，使用下划线作为私有变量是一种常见的约定。
 * 这样可以避免重复写 this.message = message 和类似 message = message 这样的错误。
 */
data class MailFluent(
    val to: List<String>,
    private var _message: String? = null,
    private var _cc: List<String>? = null,
    private var _title: String? = null,
    private var _important: Boolean? = null
) {
    /**
     * 在提供的代码示例中，我们使用了apply()函数。
     * 这个函数是Kotlin中可用的作用域函数家族的一部分。
     * apply()函数执行一段代码并返回对象的引用。
     * 基本上，它充当了前面示例中展示的setter函数的简短版本。
     * ```
     * fun message(message: String): MailBuilder {
     *      this.message = message
     *      return this
     * }
     * ```
     */
    fun message(message: String) = apply {
        _message = message
    }

    // Pattern repeats for every other field
    //...
}

/**
 * 我们可能根本不需要setter。
 * 相反，我们可以直接在对象本身上使用apply()函数，就像之前提到的那样。
 * 这个函数是Kotlin中每个对象都可以使用的扩展函数之一。
 * 然而，这种方法只有在所有可选字段都是变量而不是值的情况下才有效，就像这样：
 * ```
 * val mail = MailApply(listOf("manager@company.com")).apply {
 *      message = "You've been promoted"
 *      title = "Come to my office"
 * }
 * ```
 *
 * 这确实是一个方便的方法，实现时需要的代码更少。然而，这种方法有一些需要考虑的缺点：
 *
 * 1. 我们不得不将所有可选参数设为可变的。通常最好尽可能使用不可变字段，因为它们是线程安全的，更容易理解和推理。
 *
 * 2. 所有可选参数都是可空的。在 Kotlin 中，这是一种空安全的语言，我们必须每次访问它们时检查它们的值是否已设置。
 *
 * 3. 语法变得相当冗长。对于每个字段，我们需要重复相同的模式。现在，让我们继续讨论解决这个问题的最终方法。
 */
data class MailApply(
    val to: List<String>,
    var message: String? = null,
    var cc: List<String>? = null,
    var title: String? = null,
    var important: Boolean? = null
)

/**
 *默认参数是在类型声明之后使用 = 运算符分配的。
 * 这意味着即使我们的构造函数仍然包含所有参数，我们也不必为它们提供值。
 * 因此，如果您希望创建一个没有指定正文的电子邮件，您可以使用以下语法实现：
 *
 * ```
 * val mail = MailDefaults(listOf("manager@company.com"))
 * ```
 *
 * 然而，值得注意的是，在前面的例子中，我们不得不明确指定一个空列表来表示我们不想在抄送字段中添加任何人，这可能有些不方便。
 * 现在，让我们考虑这样一种情况：我们想发送一封只被标记为重要的电子邮件。我们该如何实现呢？
 * 然而，Kotlin提供了另一个有用的功能，称为命名参数，它允许我们通过它们的名称来指定参数。
 * 这使我们能够使用以下语法创建一封只被标记为重要的电子邮件：
 * ```
 * val mail = MailDefaults(
 *  title = "Hello",
 *  message = "There",
 *  important = true,
 *  to = listOf("my@dear.cat")
 * )
 * ```
 */
data class MailDefaults(
    val to: List<String>,
    val cc: List<String> = listOf(),
    val title: String = "",
    val message: String = "",
    val important: Boolean = false,
)
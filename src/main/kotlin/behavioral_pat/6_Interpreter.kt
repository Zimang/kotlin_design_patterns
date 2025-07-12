package top.zimang.behavioral_pat

/**
 * Kotlin提供了一套优雅的功能，用于创建既可读又类型安全的DSL。
 * 然而，值得注意的是，解释器设计模式属于较具挑战性的模式之一。
 *
 * 如果你不是立刻理解，不要犹豫，退后一步并调试所提供的代码。在每个关键点理解this表达式的角色和上下文至关重要。
 *
 * 此外，区分在对象上调用函数和调用方法之间的差异是关键。
 */
fun main() {

    //关于解释器模式
    val sql = select("name", "age") {
        from("users") {
            where("age > 25")
        } // Closes from
    } // Closes select

    println(sql) // "SELECT name, age FROM users WHERE age > 25"


    //关于DslMarker的实验
    form {
        title = "Login"

        field {
            input("username")
            submit()
            //这里要是没有DslMarker就会编译通过
//            title = "Oops" // ❌ 修改了外层 Form 的 title！容易误操作
        }
    }

}

/**
 * 扩展内运算符:`*`,这个运算符实质上允许你将数组视为一系列参数，这在数据已经以数组形式收集，但你要调用的函数设计为接受可变数量参数的情况下非常方便。
 *
 * 等价代码
 * ```
 * val selectClause = SelectClause(*columns)
 * from(selectClause)
 * return selectClause
 * ```
 */
fun select(vararg columns: String, from: SelectClause.() -> Unit):SelectClause {
    return SelectClause(*columns).apply(from)
}

@SqlDslMarker
class SelectClause(private vararg val columns: String) {

    private lateinit var from: FromClause
    fun from(
        table: String,
        where: FromClause.() -> Unit
    ): FromClause {
        this.from = FromClause(table)
        return this.from.apply(where)
    }

    override fun toString() = "SELECT ${columns.joinToString(separator = ", ")} $from"
}

/**
 * 这是第一次将lateinit关键字
 *
 * 重要的是要理解Kotlin的编译器对空安全非常严格。如果不使用lateinit，编译器将要求用默认值初始化变量。
 * 通过使用lateinit，我们实际上告诉编译器我们有信心稍后赋值，让它暂时放松其通常的严格性。
 *
 * 请注意，如果我们没有兑现承诺并忘记初始化变量，当我们第一次访问它时，就会出现UninitializedPropertyAccessException。
 *
 * lateinit关键字有利有弊，因此必须谨慎使用。
 * 重新审视我们之前的代码片段，以下是它所执行的操作概述：
 *
 * 1. 实例化了一个FromClause的实例。
 *
 * 2. 将这个FromClause实例存储为SelectClause的成员。
 *
 * 3. 然后将FromClause实例传递给where lambda。
 *
 * 4. 最终返回一个FromClause的实例。
 */
@SqlDslMarker
class FromClause(private val table: String) {
    private lateinit var where: WhereClause

    fun where(conditions: String) = this.apply {
        where = WhereClause(conditions)
    }

    override fun toString() = "FROM $table $where"
}

@SqlDslMarker
class WhereClause(private val conditions: String) {
    override fun toString() = "WHERE $conditions"
}

/**
 * Kotlin中的@DslMarker注解旨在管理DSL中的范围和交互。
 * **它限制了接收器类型的可见性，从而减少了不同DSL组件之间的冲突和意外交互。**
 *
 * 通过将@DslMarker应用于DSL的标记接口或注解，Kotlin将隐式接收器在扩展函数和属性中的作用范围限定为带有此注解的最近接收器。
 *
 * 因此，在嵌套的DSL结构中，它可以防止意外访问被标记为相同@DslMarker的外部作用域中的函数或属性。
 */
@DslMarker
annotation class SqlDslMarker


@SqlDslMarker
class Form {
    var title: String = ""
    fun field(block: Field.() -> Unit) = Field().apply(block)
}

@SqlDslMarker
class Field {
    fun input(name: String) {
        println("input: $name")
    }
    fun submit() {
        println("submit clicked")
    }
}

fun form(block: Form.() -> Unit) = Form().apply(block)

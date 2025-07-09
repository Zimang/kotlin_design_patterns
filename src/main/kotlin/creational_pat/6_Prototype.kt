package top.zimang.creational_pat

/**
 * 原型设计模式专注于定制和创建类似但具有轻微变化的对象。
 *
 * 原型的概念是为了方便对象的克隆。
 * 你可能想这样做至少有两个原因：
 *
 * 1. 在创建对象耗费资源或昂贵的情况下，这是有益的。
 *          例如，如果对象需要从数据库中获取或涉及复杂的初始化过程。
 *
 * 2. 当你需要创建类似但有细微差异的对象时，这是有用的。
 *          通过克隆原型对象，你可以避免重复相似部分，并根据需要定制克隆的对象。
 *
 * 通过利用原型设计模式，你可以有效地解决这些情况。
 *
 * 使用原型设计模式还有更高级的原因。例如，JavaScript 使用原型来实现类似继承的行为，而无需使用类。
 */
fun main() {
    val originalUser = User("admin1", Role.ADMIN, setOf("READ", "WRITE", "DELETE"))
    allUsers += originalUser

    createUser("admin2", Role.ADMIN)

    println(allUsers)
}

/**
 * 枚举类提供了一种方便的方法来表示一组常量。
 * 与使用字符串表示角色相比，这种方法具有优势，因为它允许在编译时检查确保这些对象的存在。
 * 此外，使用枚举允许创建when块，而无需else子句。
 */
enum class Role {
    ADMIN,
    SUPER_ADMIN,
    REGULAR_USER
}

/**
 * 每个用户都必须拥有一个角色，而每个角色都有一组权限。
 */
data class User(
    val name: String,
    val role: Role,
    val permissions: Set<String>,
) {
    fun hasPermission(permission: String) = permission in permissions
}

/**
 * 在创建新用户的情况下，我们可以为他们分配类似于具有相同角色的另一个用户的权限。
 * 这可以通过利用原型设计模式来实现，其中我们克隆现有用户对象并进行必要的调整：
 */
private val allUsers = mutableListOf<User>()

/**
 * Kotlin通过提供更强大的替代方案，有效地解决了Java的clone()方法的局限性。
 * 与Java不同，Java中的clone()方法需要实现Clonable接口并处理潜在的异常，而Kotlin简化了这个过程。
 * 它直接将功能集成到其数据类中，使对象复制变得更加简单，减少错误的可能性。
 * Kotlin的数据类配备了内置的copy()方法。该方法有助于创建现有数据类的新实例，并可以修改其中一些属性。
 */
fun createUser(userName: String, role: Role) {
    for (u in allUsers) {
        if (u.role == role) {
            /**
             * 就像我们在建造者设计模式中观察到的那样，Kotlin中的命名参数允许我们以任何所需的顺序指定属性。
             * 此外，我们只需要指定我们想要修改的属性，因为剩余的数据将自动为我们复制，包括私有属性。
             * Kotlin中的数据类是另一个例子，它是如此普遍，以至于已经成为语言语法的一个组成部分。
             */
            allUsers += u.copy(name = userName)
            return
        }
    }
    // Handle case that no other user with such a role exists
}
package top.zimang.creational_pat

import java.lang.IllegalArgumentException

fun main() {
    val environment = Parser.server(listOf("port: 8080", "environment: production"))
    println(environment)
}

/**
 * 本质上，抽象工厂模式是工厂中的工厂。它涉及到一个可以创建多个相关类的工厂，使其成为一个封装了多个工厂方法的类。
 *
 * 抽象工厂（Abstract Factory）模式提供一个创建相关对象的家族的接口，而无需指定它们的具体类。
 *
 * 工厂方法：创建一个对象（比如 property() 创建一个 Property）
 *
 * 抽象工厂：创建一组相关对象（比如 server() 创建一组 Property 并封装成一个 ServerConfiguration）
 *
 */
class Parser {

    companion object {
        fun server(propertyStrings: List<String>): ServerConfiguration {
            val parsedProperties = mutableListOf<Property>()
            for (p in propertyStrings) {
                parsedProperties += property(p)
            }

            return ServerConfigurationImpl(parsedProperties)
        }

        fun property(prop: String): Property {
            val (name, value) = prop.split(":")
            return when (name) {
                "port" -> IntProperty(name, value.trim().toInt())
                "environment" -> StringProperty(name, value.trim())
                else -> throw IllegalArgumentException("Unknown property: $name")
            }
        }
    }
}

/**
 * 我们不使用数据类，而是为Property和ServerConfiguration返回接口。
 *
 * 直接用Any赋值
 * ```
 * val port: Int = portProperty.value as Int // 非安全cast
 * val port: Int? = portProperty.value as? Int // 安全cast
 * ```
 */
interface Property {

    val name: String

    val value: Any

}

/**
 * 为了更好地理解设计模式的工作原理，让我们考虑一个场景，我们有一个在YAML文件中指定的服务器配置：
 *
 *      server:
 *          port: 8080
 *          environment:production
 */
interface ServerConfiguration {

    val properties: List<Property>

}

/**
 * 不要只依赖于强制转换，让我们探索另一种方法。
 * 我们可以使用两种单独的实现：IntProperty 和 StringProperty，而不是使用一个值为 Any 类型的单一实现。
 *
 */
data class ServerConfigurationImpl(

    override val properties: List<Property>

) : ServerConfiguration

data class StringProperty(

    override val name: String,

    override val value: String

) : Property

data class IntProperty(

    override val name: String,

    override val value: Int

) : Property
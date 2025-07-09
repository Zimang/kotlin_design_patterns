package top.zimang.structural_pat

import java.io.FileNotFoundException
import kotlin.io.path.Path

/**
 * 在设计模式中使用的术语“facade”灵感来自建筑设计。
 * 在建筑学中，facade指的是建筑物的吸引人正面，通常比建筑物的其他部分更具吸引力。
 * 在编程中，facade就像是帮手，隐藏了实现的复杂内部工作。
 */
fun main() {
    try {
        val server = Server.withPort(0).startFromConfiguration("/path/to/config")
    } catch (e: FileNotFoundException) {
        println("If there was a file and a parser, it would have worked")
    }
}

/**
 * 配置服务器时，他们需要至少与三个不同的接口进行交互：
 * 1. 一个 JSON 解析器（在第2章“使用创建型模式”中的抽象工厂部分讨论）。
 * 2. 一个 YAML 解析器（也在第2章的抽象工厂部分讨论）。
 * 3. 一个服务器工厂（在第2章的工厂方法部分介绍）。
 *
 * 一个更可取的情景涉及到一个单一函数startFromConfiguration()，
 *  它接受一个配置文件的路径，解析它，如果没有错误发生，就启动服务器。
 * 我们正在引入一个门面来简化用户与这组类交互的方式。
 *
 * 虽然在许多编程语言中一个常见的方法是创建一个新的类来**封装**这个逻辑，
 * 在Kotlin中，我们有一个更优雅的选项，它采用了我们在本章前面已经讨论过的一种技术：扩展函数。
 *
 * 可以看出，这种实现方式与适配器设计模式完全相同。唯一不同的是最终目标。
 * 在适配器设计模式中，我们的目标是让原本不可用的类变得可用。
 * 请记住，Kotlin 语言的目标之一就是尽可能地重复使用。**而 Facade 设计模式的目标则是让一组复杂的类变得易于使用。**
 */
fun Server.startFromConfiguration(fileLocation: String) {
    val path = Path(fileLocation)

    val lines = path.toFile().readLines() //又是一个adapter的例子

    val configuration = try {
        JsonParser().server(lines)
    } catch (e: RuntimeException) {
        YamlParser().server(lines)
    }

    Server.withPort(configuration.port)
}

class Server private constructor(port: Int) {
    companion object {
        fun withPort(port: Int): Server {
            return Server(port)
        }
    }
}

interface Parser {
    fun property(prop: String): Property
    fun server(propertyStrings: List<String>): ServerConfiguration
}

class YamlParser : Parser {
    // Implementation specific to YAML files
    override fun property(prop: String): Property {
        TODO("Not yet implemented")
    }

    override fun server(propertyStrings: List<String>): ServerConfiguration {
        TODO("Not yet implemented")
    }
}

class JsonParser : Parser {

    // Implementation specific to JSON files
    override fun property(prop: String): Property {
        TODO("Not yet implemented")
    }

    override fun server(propertyStrings: List<String>): ServerConfiguration {
        TODO("Not yet implemented")
    }
}

class Property

class ServerConfiguration {
    var port: Int = 8080
}
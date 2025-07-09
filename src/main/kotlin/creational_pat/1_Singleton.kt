package top.zimang.creational_pat


class `1_Singleton` {
}

/**
 * 单例模式有两个主要要求：
 * 1. 我们的系统中应该只有一个特定类的实例（副本）。
 * 2. 这个实例应该可以在我们的范围内的任何地方访问。范围可以是整个程序或其中的一个模块。
 */
fun main() {
    val myFavoriteMovies = listOf("Black Hawk Down", "Blade Runner")


    val myMovies = NoMoviesList
    val yourMovies = NoMoviesList

    println(myMovies === yourMovies)

    /**
     * 然而，值得注意的是，Kotlin 已经提供了 emptyList() 函数。
     * 这个函数的实用性在于，无论传入什么类型参数，它都会返回相同的不可变空列表实例。
     * 例如，emptyList<String>() 和 emptyList<Int>() 在内部都指向同一个空列表实例。
     */
    println("Are they same? ${emptyList<String>() === listOf<String>()}")

    println("object vs data object:\n" +
            " $NoMoviesList vs $NoMoviesListDataObject")
    Logger.log("Hello World")
}

data object NoMoviesListDataObject
object NoMoviesList : List<String> {

    private val empty = emptyList<String>()
    override val size: Int
        get() = empty.size

    override fun get(index: Int) = empty[index]

    override fun isEmpty() = empty.isEmpty()

    override fun iterator() = empty.iterator()

    override fun listIterator() = empty.listIterator()

    override fun listIterator(index: Int) = empty.listIterator(index)

    override fun subList(fromIndex: Int, toIndex: Int) = empty.subList(fromIndex, toIndex)

    override fun lastIndexOf(element: String) = empty.lastIndexOf(element)

    override fun indexOf(element: String) = empty.indexOf(element)

    override fun containsAll(elements: Collection<String>) = empty.containsAll(elements)

    override fun contains(element: String) = empty.contains(element)

}


object Logger {
    /**
     * Kotlin对象与类的一个关键区别在于它不能有构造函数。
     * 如果您的单例需要初始化，比如在首次访问时从配置文件加载数据，您应该使用init块。
     * 这个块允许进行初始化过程，而无需构造函数：
     */
    init {
        println("I was accessed for the first time")
    }

    fun log(message: String) {
        println("Logging $message")
    }
}

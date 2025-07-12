package top.zimang.behavioral_pat

fun main() {
    runSchedule(afterLunch = fun() {
        println("Discuss my lunch with boss' secretary")
        println("Read something not related to work")
    }, beforeLunch = {
        println("Look for my next trip destination")
        println("Read StackOverflow")
    }
//        , bossHook = { println("Boss: Can we talk privately?") }
    )
}

/**
 * 这些都是声明本地函数的有效方式。
 * 无论你如何定义它们，它们都以相同的方式被调用。
 *
 * 本地函数只能被声明在其中的父函数访问，是提取常见逻辑的好方法，而无需将其暴露出来。
 */
fun runSchedule(
    beforeLunch: () -> Unit,
    afterLunch: () -> Unit,
    bossHook: (() -> Unit)? = fun() { println() }
) {
    fun arriveToWork() {
        println("How are you all?")
    }

    val drinkCoffee = { println("Did someone left the milk out?") }

    fun goToLunch() = println("I would like something italian")

    val goHome = fun() {
        println("Finally some rest")
    }

    arriveToWork()
    drinkCoffee()
    beforeLunch()
    goToLunch()
    afterLunch()
    bossHook?.let { it() }
    goHome()
}
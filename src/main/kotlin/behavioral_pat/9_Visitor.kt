package top.zimang.behavioral_pat

/**
 * 这种模式通常与组合设计模式相辅相成，我们在第3章“理解结构模式”中讨论过。
 *
 * 访问者设计模式具有多功能性，可以从复杂的树状配置中提取数据，也可以为树中的每个节点引入行为。
 *
 * 这让人想起装饰者设计模式如何增强单个对象。
 *
 * 作为一个高效的软件架构师，我必须承认事情一直运转顺利。
 * 我从责任链集成的请求-响应系统效率很高，让我有足够的时间悠闲地喝咖啡休息。
 * 然而，我感觉开发人员中有一种越来越强烈的怀疑，他们可能认为我在走捷径。
 *
 * 为了让他们无法追踪，我正在制定策略，每周发送一封电子邮件，里面装满了最新、最热门的文章链接。
 * 当然，我并不打算自己阅读这些文章；我的目的纯粹是从知名的科技平台中策划精选它们。
 */

fun main() {

    val page = Page(
        Container(
            Image,
            Link,
            Image
        ),
        Table,
        Link,
        Container(
            Table,
            Link
        ),
        Container(
            Image,
            Container(
                Image,
                Link
            )
        )
    )

    println(collectLinks(page))
}

fun collectLinks(page: Page): List<String> {
    // No need for intermediate variable there
    return LinksCrawler().run {
        page.accept(this)
        this.links
    }
}


class LinksCrawler {
    private var _links = mutableListOf<String>()

    val links
        get() = _links.toList()

    fun visit(page: Page) {
        visit(page.elements)
    }

    fun visit(container: Container) = visit(container.elements)

    private fun visit(elements: List<HtmlElement>) {
        for (e in elements) {
            when (e) {
                is Container -> e.accept(this)
                is Link -> _links.add(e.href)
                is Image -> _links.add(e.src)
                else -> {
                }
            }
        }
    }
}

private fun Container.accept(feature: LinksCrawler) {
    feature.visit(this)
}

// Same as above but shorter
private fun Page.accept(feature: LinksCrawler) = feature.visit(this)


class Page(val elements: MutableList<HtmlElement> = mutableListOf()) {
    constructor(vararg elements: HtmlElement) : this(mutableListOf()) {
        for (s in elements) {
            this.elements.add(s)
        }
    }
}


sealed class HtmlElement

class Container(val elements: MutableList<HtmlElement> = mutableListOf()) : HtmlElement() {

    constructor(vararg units: HtmlElement) : this(mutableListOf()) {
        for (u in units) {
            this.elements.add(u)
        }
    }
}

data object Image : HtmlElement() {
    val src: String
        get() = "https://some.image"
}

data object Link : HtmlElement() {
    val href: String
        get() = "https://some.link"
}

data object Table : HtmlElement()
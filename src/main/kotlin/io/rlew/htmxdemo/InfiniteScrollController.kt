package io.rlew.htmxdemo

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private const val PAGE_SIZE = 10

@RestController
class InfiniteScrollController {

    @GetMapping("/infinite-scroll", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Infinite Scroll", "/infinite-scroll") {
        h2 { +"Infinite Scroll" }
        p {
            +"Scroll down to load more contacts. When the last row enters the viewport, "
            +"the next page is automatically fetched and appended."
        }
        article {
            table {
                thead {
                    tr {
                        th { +"#" }
                        th { +"First Name" }
                        th { +"Last Name" }
                        th { +"Email" }
                    }
                }
                tbody {
                    id = "scroll-tbody"
                    scrollRows(0)
                }
            }
        }
        details {
            summary { +"View source pattern" }
            pre {
                code {
                    +"""
<!-- Last row in each page acts as the trigger -->
<tr hx-get="/infinite-scroll/page?page=1"
    hx-trigger="revealed"
    hx-swap="afterend"
    hx-target="closest tr">
  <td>...</td>
</tr>

<!-- Server returns the next batch of rows,
     with the last row carrying the next trigger -->
                    """.trimIndent()
                }
            }
        }
    }

    @GetMapping("/infinite-scroll/page", produces = [TEXT_HTML_UTF8])
    fun loadPage(@RequestParam page: Int): String {
        val contacts = DataStore.getPage(page, PAGE_SIZE)
        if (contacts.isEmpty()) return ""
        return createHTML().tbody {
            scrollRowsForPage(contacts, page)
        }
    }

    private fun TBODY.scrollRows(page: Int) {
        val contacts = DataStore.getPage(page, PAGE_SIZE)
        scrollRowsForPage(contacts, page)
    }

    private fun TBODY.scrollRowsForPage(contacts: List<Contact>, page: Int) {
        contacts.forEachIndexed { index, contact ->
            val isLast = index == contacts.size - 1
            val hasMore = DataStore.getPage(page + 1, PAGE_SIZE).isNotEmpty()
            tr {
                if (isLast && hasMore) {
                    attributes["hx-get"] = "/infinite-scroll/page?page=${page + 1}"
                    attributes["hx-trigger"] = "revealed"
                    attributes["hx-swap"] = "afterend"
                }
                td { +"${contact.id}" }
                td { +contact.firstName }
                td { +contact.lastName }
                td { +contact.email }
            }
        }
    }
}

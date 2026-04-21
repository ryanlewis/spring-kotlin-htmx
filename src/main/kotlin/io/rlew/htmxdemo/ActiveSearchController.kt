package io.rlew.htmxdemo

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ActiveSearchController {

    @GetMapping("/active-search", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Active Search", "/active-search") {
        h2 { +"Active Search" }
        p {
            +"Start typing to search contacts in real-time. Results update after a 300ms pause in typing. "
            +"The loading indicator appears during the request."
        }
        article {
            input {
                type = InputType.search
                name = "q"
                placeholder = "Search contacts..."
                attributes["hx-get"] = "/active-search/results"
                attributes["hx-trigger"] = "input changed delay:300ms, search"
                attributes["hx-target"] = "#search-results"
                attributes["hx-indicator"] = "#search-spinner"
            }
            span {
                id = "search-spinner"
                attributes["class"] = "htmx-indicator"
                attributes["aria-busy"] = "true"
                +" Searching..."
            }
            table {
                thead {
                    tr {
                        th { +"First Name" }
                        th { +"Last Name" }
                        th { +"Email" }
                    }
                }
                tbody {
                    id = "search-results"
                    contactRows(DataStore.search(""))
                }
            }
        }
        details {
            summary { +"View source pattern" }
            pre {
                code {
                    +"""
<input type="search" name="q"
       hx-get="/active-search/results"
       hx-trigger="input changed delay:300ms, search"
       hx-target="#search-results"
       hx-indicator="#search-spinner" />
<span id="search-spinner" class="htmx-indicator">Searching...</span>
                    """.trimIndent()
                }
            }
        }
    }

    @GetMapping("/active-search/results", produces = [TEXT_HTML_UTF8])
    fun searchResults(@RequestParam q: String?): String = createHTML().tbody {
        id = "search-results"
        contactRows(DataStore.search(q ?: ""))
    }

    private fun TBODY.contactRows(contacts: List<Contact>) {
        if (contacts.isEmpty()) {
            tr {
                td { colSpan = "3"; +"No contacts found." }
            }
        } else {
            contacts.forEach { contact ->
                tr {
                    td { +contact.firstName }
                    td { +contact.lastName }
                    td { +contact.email }
                }
            }
        }
    }
}

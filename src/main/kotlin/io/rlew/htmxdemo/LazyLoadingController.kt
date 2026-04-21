package io.rlew.htmxdemo

import kotlinx.coroutines.delay
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LazyLoadingController {

    @GetMapping("/lazy-loading", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Lazy Loading", "/lazy-loading") {
        h2 { +"Lazy Loading" }
        p {
            +"The statistics panel below loads after the page renders. "
            +"A spinner is shown while the server prepares the data. "
            +"This pattern is useful for deferring expensive computations."
        }

        article {
            header { h3 { +"Contact Statistics" } }
            div {
                attributes["hx-get"] = "/lazy-loading/stats"
                attributes["hx-trigger"] = "load"
                attributes["hx-swap"] = "innerHTML"
                p {
                    attributes["aria-busy"] = "true"
                    +"Loading statistics..."
                }
            }
        }

        article {
            header { h3 { +"Recent Contacts" } }
            div {
                attributes["hx-get"] = "/lazy-loading/recent"
                attributes["hx-trigger"] = "load"
                attributes["hx-swap"] = "innerHTML"
                p {
                    attributes["aria-busy"] = "true"
                    +"Loading recent contacts..."
                }
            }
        }

        details {
            summary { +"View source pattern" }
            pre {
                code {
                    +"""
<div hx-get="/lazy-loading/stats"
     hx-trigger="load"
     hx-swap="innerHTML">
  <p aria-busy="true">Loading statistics...</p>
</div>

<!-- Server returns the actual content after
     simulating an expensive operation -->
                    """.trimIndent()
                }
            }
        }
    }

    @GetMapping("/lazy-loading/stats", produces = [TEXT_HTML_UTF8])
    suspend fun stats(): String {
        // Simulate an expensive operation
        delay(1500)

        val contacts = DataStore.contacts.values
        val total = contacts.size
        val active = contacts.count { it.active }
        val inactive = total - active
        val domains = contacts.map { it.email.substringAfter("@") }.distinct().size

        return createHTML().div {
            div {
                attributes["class"] = "grid"
                statCard("Total Contacts", total.toString())
                statCard("Active", active.toString())
                statCard("Inactive", inactive.toString())
                statCard("Email Domains", domains.toString())
            }
        }
    }

    @GetMapping("/lazy-loading/recent", produces = [TEXT_HTML_UTF8])
    suspend fun recent(): String {
        // Simulate another expensive operation
        delay(2000)

        val recent = DataStore.contacts.values.sortedByDescending { it.id }.take(5)
        return createHTML().div {
            table {
                thead {
                    tr {
                        th { +"Name" }
                        th { +"Email" }
                        th { +"Status" }
                    }
                }
                tbody {
                    recent.forEach { contact ->
                        tr {
                            td { +"${contact.firstName} ${contact.lastName}" }
                            td { +contact.email }
                            td {
                                if (contact.active) +"Active" else +"Inactive"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FlowContent.statCard(label: String, value: String) {
        article {
            header { strong { +label } }
            p {
                style = "font-size: 2rem; text-align: center; margin: 0;"
                +value
            }
        }
    }
}

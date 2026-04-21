package io.rlew.htmxdemo

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlinx.html.stream.createHTML

data class NavItem(val href: String, val label: String)

val NAV_ITEMS = listOf(
    NavItem("/", "Home"),
    NavItem("/active-search", "Active Search"),
    NavItem("/click-to-edit", "Click to Edit"),
    NavItem("/bulk-update", "Bulk Update"),
    NavItem("/infinite-scroll", "Infinite Scroll"),
    NavItem("/delete-row", "Delete Row"),
    NavItem("/lazy-loading", "Lazy Loading"),
)

fun page(pageTitle: String, currentPath: String, bodyContent: MAIN.() -> Unit): String {
    val doc = createHTMLDocument().html {
        head {
            meta { charset = "utf-8" }
            meta { name = "viewport"; content = "width=device-width, initial-scale=1" }
            title { +"$pageTitle — HTMX + Kotlin Demo" }
            link {
                rel = "stylesheet"
                href = "https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css"
            }
            script {
                src = "https://unpkg.com/htmx.org@2.0.4"
                integrity = "sha384-HGfztofotfshcF7+8n44JQL2oJmowVChPTg48S+jvZoztPfvwD79OC/LTtG6dMp+"
                attributes["crossorigin"] = "anonymous"
            }
            style {
                unsafe {
                    +"""
                    .htmx-indicator { display: none; }
                    .htmx-request .htmx-indicator, .htmx-request.htmx-indicator { display: inline; }
                    tr.htmx-swapping { opacity: 0; transition: opacity 0.5s ease-out; }
                    .fade-me-out.htmx-swapping { opacity: 0; transition: opacity 0.5s ease-out; }
                    nav ul { flex-wrap: wrap; }
                    """.trimIndent()
                }
            }
        }
        body {
            nav {
                attributes["class"] = "container-fluid"
                ul {
                    li {
                        strong { +"HTMX + Kotlin" }
                    }
                }
                ul {
                    NAV_ITEMS.forEach { item ->
                        li {
                            a {
                                href = item.href
                                if (item.href == currentPath) {
                                    attributes["aria-current"] = "page"
                                }
                                +item.label
                            }
                        }
                    }
                }
            }
            main {
                attributes["class"] = "container"
                bodyContent()
            }
        }
    }
    return doc.serialize()
}

fun fragment(block: FlowContent.() -> Unit): String = createHTML().div { block() }

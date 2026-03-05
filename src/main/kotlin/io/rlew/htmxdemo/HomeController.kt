package io.rlew.htmxdemo

import kotlinx.html.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Home", "/") {
        hGroup {
            h1 { +"Spring Boot + kotlinx.html + HTMX" }
            p { +"A showcase of common HTMX patterns, built with Kotlin's type-safe HTML DSL and Spring WebFlux." }
        }

        p {
            +"This demo shows how to build interactive web applications "
            strong { +"without any JavaScript" }
            +" and "
            strong { +"without any template engine" }
            +". HTMX handles the interactivity through HTML attributes, "
            +"and kotlinx.html generates all the markup in pure Kotlin."
        }

        div {
            attributes["class"] = "grid"
            patternCard(
                "Active Search",
                "/active-search",
                "Search contacts in real-time as you type. Demonstrates hx-trigger with delay, hx-indicator for loading state, and partial page updates."
            )
            patternCard(
                "Click to Edit",
                "/click-to-edit",
                "View a contact, click to edit inline, then save. Shows hx-get/hx-put swapping between view and edit modes with outerHTML replacement."
            )
            patternCard(
                "Bulk Update",
                "/bulk-update",
                "Select multiple contacts and toggle their status in bulk. Uses hx-include to gather checkbox values and hx-target for targeted updates."
            )
        }
        div {
            attributes["class"] = "grid"
            patternCard(
                "Infinite Scroll",
                "/infinite-scroll",
                "A contact list that loads more rows as you scroll down. Leverages hx-trigger=\"revealed\" to fetch the next page when the sentinel row enters the viewport."
            )
            patternCard(
                "Delete Row",
                "/delete-row",
                "Delete contacts from a table with a confirmation dialog and smooth fade-out animation. Combines hx-confirm, hx-delete, and CSS transitions."
            )
            patternCard(
                "Lazy Loading",
                "/lazy-loading",
                "Content that loads after the page renders, showing a spinner in the meantime. Uses hx-trigger=\"load\" to defer expensive operations."
            )
        }
    }

    private fun FlowContent.patternCard(title: String, href: String, description: String) {
        article {
            header { h3 { a { this.href = href; +title } } }
            p { +description }
        }
    }
}

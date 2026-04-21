package io.rlew.htmxdemo

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.web.bind.annotation.*

@RestController
class DeleteRowController {

    @GetMapping("/delete-row", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Delete Row", "/delete-row") {
        h2 { +"Delete Row" }
        p {
            +"Click the delete button to remove a contact. A browser confirmation dialog appears first, "
            +"then the row fades out smoothly before being removed from the DOM."
        }
        article {
            table {
                thead {
                    tr {
                        th { +"Name" }
                        th { +"Email" }
                        th { +"" }
                    }
                }
                tbody {
                    id = "delete-tbody"
                    DataStore.contacts.values.sortedBy { it.id }.take(10).forEach { contact ->
                        deleteRow(contact)
                    }
                }
            }
        }
        details {
            summary { +"View source pattern" }
            pre {
                code {
                    +"""
<tr class="fade-me-out">
  <td>Joe Smith</td>
  <td>joe@example.com</td>
  <td>
    <button hx-delete="/delete-row/1"
            hx-confirm="Delete Joe Smith?"
            hx-target="closest tr"
            hx-swap="outerHTML swap:500ms">
      Delete
    </button>
  </td>
</tr>

<!-- CSS for smooth transition -->
<style>
  tr.htmx-swapping { opacity: 0; transition: opacity 0.5s ease-out; }
</style>
                    """.trimIndent()
                }
            }
        }
    }

    @DeleteMapping("/delete-row/{id}", produces = [TEXT_HTML_UTF8])
    fun delete(@PathVariable id: Long): String {
        DataStore.delete(id)
        return "" // Return empty string to remove the row
    }

    private fun TBODY.deleteRow(contact: Contact) {
        tr {
            attributes["class"] = "fade-me-out"
            td { +"${contact.firstName} ${contact.lastName}" }
            td { +contact.email }
            td {
                button {
                    attributes["class"] = "secondary outline"
                    attributes["hx-delete"] = "/delete-row/${contact.id}"
                    attributes["hx-confirm"] = "Delete ${contact.firstName} ${contact.lastName}?"
                    attributes["hx-target"] = "closest tr"
                    attributes["hx-swap"] = "outerHTML swap:500ms"
                    +"Delete"
                }
            }
        }
    }
}

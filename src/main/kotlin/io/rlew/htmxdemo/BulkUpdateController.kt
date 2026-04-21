package io.rlew.htmxdemo

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.web.bind.annotation.*

@RestController
class BulkUpdateController {

    @GetMapping("/bulk-update", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Bulk Update", "/bulk-update") {
        h2 { +"Bulk Update" }
        p {
            +"Select contacts using the checkboxes, then click \"Activate\" or \"Deactivate\" "
            +"to change their status in bulk. The table body re-renders with updated state."
        }
        form {
            id = "bulk-form"
            div {
                attributes["class"] = "grid"
                button {
                    type = ButtonType.button
                    attributes["hx-put"] = "/bulk-update/activate"
                    attributes["hx-include"] = "#bulk-form"
                    attributes["hx-target"] = "#bulk-tbody"
                    +"Activate Selected"
                }
                button {
                    type = ButtonType.button
                    attributes["class"] = "secondary"
                    attributes["hx-put"] = "/bulk-update/deactivate"
                    attributes["hx-include"] = "#bulk-form"
                    attributes["hx-target"] = "#bulk-tbody"
                    +"Deactivate Selected"
                }
            }
            table {
                thead {
                    tr {
                        th { +"Select" }
                        th { +"Name" }
                        th { +"Email" }
                        th { +"Status" }
                    }
                }
                tbody {
                    id = "bulk-tbody"
                    bulkRows(DataStore.contacts.values.sortedBy { it.id }.take(10))
                }
            }
        }
        details {
            summary { +"View source pattern" }
            pre {
                code {
                    +"""
<form id="bulk-form">
  <button hx-put="/bulk-update/activate"
          hx-include="#bulk-form"
          hx-target="#bulk-tbody">Activate Selected</button>

  <tbody id="bulk-tbody">
    <tr>
      <td><input type="checkbox" name="ids" value="1" /></td>
      <td>Joe Smith</td>
      <td>Active</td>
    </tr>
  </tbody>
</form>
                    """.trimIndent()
                }
            }
        }
    }

    @PutMapping("/bulk-update/activate", produces = [TEXT_HTML_UTF8])
    fun activate(@RequestParam(required = false) ids: List<Long>?): String {
        ids?.forEach { DataStore.setActive(it, true) }
        return renderBulkTbody()
    }

    @PutMapping("/bulk-update/deactivate", produces = [TEXT_HTML_UTF8])
    fun deactivate(@RequestParam(required = false) ids: List<Long>?): String {
        ids?.forEach { DataStore.setActive(it, false) }
        return renderBulkTbody()
    }

    private fun renderBulkTbody(): String = createHTML().tbody {
        id = "bulk-tbody"
        bulkRows(DataStore.contacts.values.sortedBy { it.id }.take(10))
    }

    private fun TBODY.bulkRows(contacts: List<Contact>) {
        contacts.forEach { contact ->
            tr {
                td {
                    input {
                        type = InputType.checkBox
                        name = "ids"
                        value = contact.id.toString()
                    }
                }
                td { +"${contact.firstName} ${contact.lastName}" }
                td { +contact.email }
                td {
                    if (contact.active) {
                        ins { +"Active" }
                    } else {
                        del { +"Inactive" }
                    }
                }
            }
        }
    }
}

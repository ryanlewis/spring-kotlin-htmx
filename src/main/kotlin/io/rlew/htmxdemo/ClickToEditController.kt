package io.rlew.htmxdemo

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.springframework.web.bind.annotation.*

@RestController
class ClickToEditController {

    @GetMapping("/click-to-edit", produces = [TEXT_HTML_UTF8])
    fun index(): String = page("Click to Edit", "/click-to-edit") {
        h2 { +"Click to Edit" }
        p {
            +"Click the \"Edit\" button to switch a contact card to an inline edit form. "
            +"Save replaces the form with the updated view. Cancel restores the original."
        }
        div {
            id = "contact-view"
            viewMode(DataStore.contacts.values.first())
        }
        details {
            summary { +"View source pattern" }
            pre {
                code {
                    +"""
<!-- View mode -->
<div hx-target="this" hx-swap="outerHTML">
  <p>Joe Smith (joe@example.com)</p>
  <button hx-get="/click-to-edit/1/edit">Edit</button>
</div>

<!-- Edit mode (returned by server) -->
<form hx-put="/click-to-edit/1" hx-target="this" hx-swap="outerHTML">
  <input name="firstName" value="Joe" />
  <input name="lastName" value="Smith" />
  <input name="email" value="joe@example.com" />
  <button type="submit">Save</button>
  <button hx-get="/click-to-edit/1/view">Cancel</button>
</form>
                    """.trimIndent()
                }
            }
        }
    }

    @GetMapping("/click-to-edit/{id}/view", produces = [TEXT_HTML_UTF8])
    fun view(@PathVariable id: Long): String {
        val contact = DataStore.get(id) ?: return fragment { p { +"Contact not found." } }
        return createHTML().div {
            viewMode(contact)
        }
    }

    @GetMapping("/click-to-edit/{id}/edit", produces = [TEXT_HTML_UTF8])
    fun edit(@PathVariable id: Long): String {
        val contact = DataStore.get(id) ?: return fragment { p { +"Contact not found." } }
        return createHTML().form {
            attributes["hx-put"] = "/click-to-edit/$id"
            attributes["hx-target"] = "this"
            attributes["hx-swap"] = "outerHTML"
            div {
                attributes["class"] = "grid"
                label {
                    +"First Name"
                    input {
                        type = InputType.text
                        name = "firstName"
                        value = contact.firstName
                        required = true
                    }
                }
                label {
                    +"Last Name"
                    input {
                        type = InputType.text
                        name = "lastName"
                        value = contact.lastName
                        required = true
                    }
                }
                label {
                    +"Email"
                    input {
                        type = InputType.email
                        name = "email"
                        value = contact.email
                        required = true
                    }
                }
            }
            div {
                attributes["class"] = "grid"
                button {
                    type = ButtonType.submit
                    +"Save"
                }
                button {
                    type = ButtonType.button
                    attributes["class"] = "secondary"
                    attributes["hx-get"] = "/click-to-edit/$id/view"
                    attributes["hx-target"] = "closest form"
                    attributes["hx-swap"] = "outerHTML"
                    +"Cancel"
                }
            }
        }
    }

    @PutMapping("/click-to-edit/{id}", produces = [TEXT_HTML_UTF8])
    fun update(
        @PathVariable id: Long,
        @RequestParam firstName: String,
        @RequestParam lastName: String,
        @RequestParam email: String
    ): String {
        val contact = DataStore.update(id, firstName, lastName, email)
            ?: return fragment { p { +"Contact not found." } }
        return createHTML().div {
            viewMode(contact)
        }
    }

    private fun FlowContent.viewMode(contact: Contact) {
        div {
            attributes["hx-target"] = "this"
            attributes["hx-swap"] = "outerHTML"
            article {
                header { h3 { +"${contact.firstName} ${contact.lastName}" } }
                p {
                    strong { +"Email: " }
                    +contact.email
                }
                footer {
                    button {
                        attributes["hx-get"] = "/click-to-edit/${contact.id}/edit"
                        +"Edit"
                    }
                }
            }
        }
    }
}

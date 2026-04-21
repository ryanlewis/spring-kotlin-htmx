package io.rlew.htmxdemo

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

data class Contact(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val active: Boolean = true
)

object DataStore {
    private val idCounter = AtomicLong(0)
    val contacts = ConcurrentHashMap<Long, Contact>()

    init {
        seed()
    }

    fun seed() {
        contacts.clear()
        idCounter.set(0)
        val people = listOf(
            Triple("Joe", "Smith", "joe@example.com"),
            Triple("Angie", "MacDowell", "angie@example.com"),
            Triple("Fuqua", "Tarkenton", "fuqua@example.com"),
            Triple("Kim", "Yee", "kim@example.com"),
            Triple("Kumar", "Patel", "kumar@example.com"),
            Triple("Donna", "Ellis", "donna@example.com"),
            Triple("Larry", "Fine", "larry@example.com"),
            Triple("Moe", "Howard", "moe@example.com"),
            Triple("Curly", "Howard", "curly@example.com"),
            Triple("Shemp", "Howard", "shemp@example.com"),
            Triple("Ada", "Lovelace", "ada@example.com"),
            Triple("Grace", "Hopper", "grace@example.com"),
            Triple("Alan", "Turing", "alan@example.com"),
            Triple("Linus", "Torvalds", "linus@example.com"),
            Triple("Margaret", "Hamilton", "margaret@example.com"),
            Triple("Dennis", "Ritchie", "dennis@example.com"),
            Triple("Ken", "Thompson", "ken@example.com"),
            Triple("Barbara", "Liskov", "barbara@example.com"),
            Triple("Bjarne", "Stroustrup", "bjarne@example.com"),
            Triple("James", "Gosling", "james@example.com"),
        )
        people.forEach { (first, last, email) ->
            val id = idCounter.incrementAndGet()
            contacts[id] = Contact(id, first, last, email)
        }
    }

    fun search(query: String): List<Contact> {
        if (query.isBlank()) return contacts.values.sortedBy { it.id }
        val q = query.lowercase()
        return contacts.values
            .filter {
                it.firstName.lowercase().contains(q) ||
                    it.lastName.lowercase().contains(q) ||
                    it.email.lowercase().contains(q)
            }
            .sortedBy { it.id }
    }

    fun getPage(page: Int, size: Int = 10): List<Contact> {
        val sorted = contacts.values.sortedBy { it.id }
        val start = page * size
        if (start >= sorted.size) return emptyList()
        return sorted.subList(start, minOf(start + size, sorted.size))
    }

    fun get(id: Long): Contact? = contacts[id]

    fun update(id: Long, firstName: String, lastName: String, email: String): Contact? {
        return contacts.computeIfPresent(id) { _, existing ->
            existing.copy(firstName = firstName, lastName = lastName, email = email)
        }
    }

    fun delete(id: Long): Boolean = contacts.remove(id) != null

    fun setActive(id: Long, active: Boolean): Contact? {
        return contacts.computeIfPresent(id) { _, existing ->
            existing.copy(active = active)
        }
    }
}

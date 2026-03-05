# Spring Boot + kotlinx.html + HTMX Pattern Showcase

A showcase of common [HTMX](https://htmx.org/) patterns built with
[Spring Boot](https://spring.io/projects/spring-boot),
[Kotlin](https://kotlinlang.org/), and
[kotlinx.html](https://github.com/Kotlin/kotlinx.html).

- **No template engine** — kotlinx.html generates all markup in pure Kotlin
- **No JavaScript** — HTMX handles interactivity through HTML attributes
- **No database** — an in-memory data store keeps things simple

## Patterns Demonstrated

| Pattern | Route | What it shows |
|---------|-------|---------------|
| **Active Search** | `/active-search` | Real-time search-as-you-type with `hx-trigger` delay and loading indicators |
| **Click to Edit** | `/click-to-edit` | Inline editing with `hx-get`/`hx-put` and `outerHTML` swaps |
| **Bulk Update** | `/bulk-update` | Batch status changes with `hx-include` and targeted `hx-target` |
| **Infinite Scroll** | `/infinite-scroll` | Automatic pagination with `hx-trigger="revealed"` |
| **Delete Row** | `/delete-row` | Confirmation dialogs with `hx-confirm` and CSS fade-out transitions |
| **Lazy Loading** | `/lazy-loading` | Deferred content loading with `hx-trigger="load"` and spinners |

Each pattern page includes a "View source pattern" section showing the key HTMX attributes used.

## Running

```bash
# Linux / macOS
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

Then visit http://localhost:8080/ in your browser.

## Tech Stack

- Spring Boot 3.5 with WebFlux
- Kotlin 2.0
- kotlinx.html 0.12 (type-safe HTML DSL)
- HTMX 2.0
- Pico CSS 2 (classless CSS framework)

## License

See [LICENSE](LICENSE).

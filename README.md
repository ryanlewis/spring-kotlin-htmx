# Spring Boot + kotlinx.html + HTMX Pattern Showcase

A showcase of common [HTMX](https://htmx.org/) patterns built with
[Spring Boot](https://spring.io/projects/spring-boot),
[Kotlin](https://kotlinlang.org/), and
[kotlinx.html](https://github.com/Kotlin/kotlinx.html).

- **No template engine** — kotlinx.html generates all markup in pure Kotlin
- **No JavaScript** — HTMX handles interactivity through HTML attributes
- **No database** — an in-memory data store keeps things simple

![Home page](docs/screenshots/home.png)

## Patterns Demonstrated

### Active Search

Real-time search-as-you-type with `hx-trigger` delay and loading indicators.

![Active Search](docs/screenshots/active-search.png)

### Click to Edit

Inline editing with `hx-get`/`hx-put` and `outerHTML` swaps between view and edit modes.

![Click to Edit](docs/screenshots/click-to-edit.png)

### Bulk Update

Select multiple contacts and toggle their status in batch using `hx-include` and targeted `hx-target`.

![Bulk Update](docs/screenshots/bulk-update.png)

### Infinite Scroll

Automatic pagination that loads more rows as you scroll, using `hx-trigger="revealed"`.

![Infinite Scroll](docs/screenshots/infinite-scroll.png)

### Delete Row

Confirmation dialogs with `hx-confirm` and smooth CSS fade-out transitions on `hx-delete`.

![Delete Row](docs/screenshots/delete-row.png)

### Lazy Loading

Deferred content loading with `hx-trigger="load"` and spinners for expensive operations.

![Lazy Loading](docs/screenshots/lazy-loading.png)

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

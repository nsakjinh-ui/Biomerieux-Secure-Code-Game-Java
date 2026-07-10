# Level 5 - Model solution

This code is vulnerable to **Cross-Site Scripting (XSS)**.

Learn more about XSS: https://portswigger.net/web-security/cross-site-scripting

### Why is the application vulnerable?

At first glance the input looks sanitized:

```java
String sanitizedPlanet = planet.replaceAll("[<>{}\\[\\]]", "");
```

But what if all HTML start/end tags were pruned away — what could still go wrong?
On top of that, a keyword blocklist rejects anything containing `"script"`.
However, other tags such as `img` can still exploit an XSS bug, as follows:

**Exploit:**
```
&ltimg src="x" onerror="document.body.innerHTML = 'Website is hacked';"&gt
```

**Explanation** — several things go wrong at once:

1. The regex doesn't cover `(` and `)`, which are necessary for a JavaScript function call — not the main issue here, but worth noting.
2. The sanitization doesn't touch the `&lt` / `&gt` HTML entities (no trailing `;` needed for a browser to recognize legacy entity references).
3. `details.html` renders the planet name with `th:utext` (Thymeleaf's "unescaped text"), the equivalent of Jinja2's `|safe` filter. This is always a risky decision — it disables Thymeleaf's automatic HTML-escaping.
4. `details.html` **reuses** the already-rendered planet name and re-injects it as HTML at a second location:
   ```html
   <script>
     document.getElementById("planet").innerHTML = document.getElementById("name").textContent;
   </script>
   ```
   `textContent` returns the *decoded* text (real `<`/`>` characters), and assigning it via `innerHTML` makes the browser **re-parse it as real markup** — creating an actual `<img>` element whose `onerror` handler fires because `src="x"` fails to load.

### How do we fix it?

1. **Never reuse content rendered in "unescaped" mode as HTML somewhere else.** It's, by definition, unescaped.
2. **Don't hand-roll HTML sanitization** with a handful of blocked characters. Use `th:text` instead of `th:utext` wherever you're rendering plain user-supplied text — Thymeleaf will then properly HTML-escape `<`, `>`, `&`, `"`, and `'` for you automatically.
3. If you truly need to render user-controlled HTML (rare, and best avoided), use an allow-list HTML sanitizer library (e.g. OWASP Java HTML Sanitizer) instead of ad hoc regex stripping.
4. Delete the second, JavaScript-based reflection entirely — it's an unnecessary DOM XSS sink and doesn't serve the feature (search-in-Google link) it was meant for; build that link server-side instead, safely encoded.

```java
// Fixed controller: no special-casing needed beyond passing the raw value,
// because the template will now escape it correctly.
model.addAttribute("planet", planet);
model.addAttribute("info", getPlanetInfo(planet));
return "details";
```

```html
<!-- Fixed template: th:text instead of th:utext, and no unsafe JS reflection -->
<p>Planet name: <span id="name" th:text="${planet}"></span></p>
<p>Planet info: <span th:text="${info}"></span></p>
```

### What else can XSS do?

- Steal cookies and session information
- Redirect to malicious/phishing websites
- Modify website content
- Keylogging

### How to prevent XSS in general?

- Escape/encode output by context (HTML body, HTML attribute, JS string, URL, ...) — let your templating engine do this by default, don't disable it.
- Use a Content Security Policy (CSP).
- Use `HttpOnly` cookies so client-side JavaScript can't read session cookies even if XSS occurs.
- Validate and sanitize input, but treat it as *defense in depth*, not your only line of defense — output encoding is what actually stops XSS.

# Level 2 - Model solution

```java
package com.biomerieux.level2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TaxPayer {

    private final String username;
    private final String password;
    private final Path baseDir;

    public TaxPayer(String username, String password) {
        this.username = username;
        this.password = password;
        this.baseDir = /* resolved assets folder, see original class */ null;
    }

    public String getProfPicture(String path) throws IOException {
        if (path == null) {
            return null;
        }

        // Resolve the user-supplied path against baseDir, then verify the
        // normalized result is still contained inside baseDir.
        Path profPicturePath = baseDir.resolve(path).normalize();
        if (!profPicturePath.startsWith(baseDir)) {
            return null;
        }

        byte[] picture = Files.readAllBytes(profPicturePath);
        return profPicturePath.toString();
    }

    public String getTaxFormAttachment(String path) throws IOException {
        if (path == null) {
            throw new IllegalStateException("Error: Tax form is required for all users");
        }

        Path taxFormPath = baseDir.resolve(path).normalize();
        if (!taxFormPath.startsWith(baseDir)) {
            return null;
        }

        byte[] taxData = Files.readAllBytes(taxFormPath);
        return taxFormPath.toString();
    }
}
```

## Solution explanation

**Path Traversal vulnerability.**

A form of injection attack where attackers escape the intended target directory and
manage to access parent directories. In `getProfPicture` and `getTaxFormAttachment`,
the path wasn't properly sanitized: `getProfPicture` used a **blocklist**
(`path.startsWith("/") || path.startsWith("..")`), and `getTaxFormAttachment` had no
check at all.

Blocklists are fragile: the check above misses `"./../secret.txt"`, which doesn't
start with `/` or `..` but still escapes the base directory once resolved.

**Proposed fix:** rather than trying to enumerate every "bad" input (an infinite,
creative search space for attackers), rely on `Path.resolve()` / `Path.normalize()`
to compute the final, canonical location, then verify with `Path.startsWith(baseDir)`
that the result is still contained inside the intended directory. This is an
**allow-list** approach: only paths that resolve inside the sandboxed directory are
accepted, regardless of how they're spelled.

Implementation note: use `baseDir.resolve(path)` rather than manually concatenating
strings (e.g. `Paths.get(baseDir.toString(), path)`). `Path.resolve()` correctly
handles both relative inputs (appended to `baseDir`) and already-absolute inputs
(returned as-is, to then be validated against `baseDir`) — a plain string join does
not.

We covered this flaw in a blog post about OWASP's Top 10 proactive controls:
https://github.blog/2021-12-06-write-more-secure-code-owasp-top-10-proactive-controls/

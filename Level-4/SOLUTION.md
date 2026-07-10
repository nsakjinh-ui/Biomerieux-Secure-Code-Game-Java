# Level 4 - Model solution

```java
package com.biomerieux.level4;

import org.springframework.security.crypto.bcrypt.BCrypt;
import java.security.SecureRandom;

public class RandomGenerator {

    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // FIXED: SecureRandom is a cryptographically strong random number generator
    private final SecureRandom random = new SecureRandom();

    public String generateToken(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    // FIXED: let bcrypt generate its own cryptographically sound salt
    public String generateSalt(int rounds) {
        return BCrypt.gensalt(rounds);
    }
}
```

```java
package com.biomerieux.level4;

public final class Constants {
    // FIXED: read from the environment / a secret manager instead of hardcoding
    public static final String PRIVATE_KEY = System.getenv("PRIVATE_KEY");
    public static final String PUBLIC_KEY = System.getenv("PUBLIC_KEY");
    public static final String SECRET_KEY = System.getenv("SECRET_KEY");

    // FIXED: use the modern, salted hasher
    public static final String PASSWORD_HASHER = "SHA256_hasher";

    private Constants() {
    }
}
```

## Solution explanation

Several mistakes stack up here:

1. **Reinventing the wheel for salt generation.** `generateSalt` manually built a
   bcrypt-shaped string using `java.util.Random` instead of calling
   `BCrypt.gensalt()`. `java.util.Random` is a linear congruential generator: it's
   predictable and not suitable for anything security-related. The fix is to use
   `java.security.SecureRandom`, or — better — let the audited bcrypt library
   generate its own salt entirely (`BCrypt.gensalt(rounds)`), so you're not
   reimplementing a cryptographic primitive by hand.

2. **A cryptographically broken hashing algorithm was selected by default**
   (`PASSWORD_HASHER = "MD5_hasher"`). MD5 has no salt and is extremely fast to
   compute, making it trivial to brute-force or look up in rainbow tables. The
   fix is to point `PASSWORD_HASHER` at `SHA256_hasher`, which combines a slow,
   salted algorithm (bcrypt) with the password.

3. **A secret committed directly to source control**
   (`SECRET_KEY = "TjWnZr4u7x!A%D*G-KaPdSgVkXp2s5v8"`). Anyone with read access
   to the repository — including its full git history — can read this value
   forever, even if it's later removed from the latest commit. Secrets must be
   injected at runtime, e.g. via environment variables or a secret manager,
   never hardcoded.

A good practice is to always rely on modules/libraries specifically designed,
and confirmed by the security community, for cryptography-related use cases
(`java.security.SecureRandom`, Spring Security Crypto's `BCrypt`) rather than
general-purpose utilities (`java.util.Random`) or "roll your own" primitives.

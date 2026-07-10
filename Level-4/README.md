## Level 4: Locanda

Languages: `java` (Maven, Spring Security Crypto)

### 📝 Storyline

Locanda attracts travelers who prefer long passphrases. But the backend team rushed deployment and implemented password handling incorrectly. Some accounts may be vulnerable to dictionary attacks or hash guessing. Your mission: inspect the authentication code (storage & comparison), find the fatal mistakes, and secure Locanda's passwords so guests stay safe.

### :keyboard: What's in this module?

- `src/main/java/.../RandomGenerator.java`, `Sha256Hasher.java`, `Md5Hasher.java`, `Constants.java` — the vulnerable code to review.
- `src/test/java/.../SecurityChecksTest.java` — quick local checks. They will **fail** initially; your goal is to get them to pass.
- `src/test/java/.../CryptoTest.java` — the regression tests that must keep passing after your fix.
- `hint.txt` — a hint if you get stuck.
- `SOLUTION.md` — the model solution and explanation.

### 🚦 Time to start!

```bash
mvn -pl Level-4 test
```

1. Review the code. Can you spot the bug(s)?
2. Try to fix the bug. Open a pull request to `main` or push your fix to a branch.
3. You successfully completed this level when you (a) resolve all related code scanning alerts and (b) `CryptoTest` and `SecurityChecksTest` pass 🟢. Note: this level has no functional exploit test beyond `SecurityChecksTest` — go read the CodeQL alerts too.
4. If you need more guidance, read the CodeQL scanning alerts.

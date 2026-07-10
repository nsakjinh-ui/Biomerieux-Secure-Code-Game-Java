## Level 2: Social Network

_Nice work finishing Level 1: Cyber Monday! It's now time for Level 2: Social Network_ :sparkles:

Languages: `java` (Maven, Spring Boot)

### 📝 Storyline

In the mid-2030s, governments launch social networks to fight crime and assist citizens. Users can upload tax forms, set profile pictures, and share public tips. But the rush to digitize has left the platform exposed. Hackers may access files far beyond what was intended. Can you secure the system and unlock Level 3?

### :keyboard: Setup instructions

- We encourage you to enable code scanning with CodeQL (already wired up in `.github/workflows/codeql-analysis.yml` at the repo root, `java` is part of the language matrix).

### :keyboard: What's in this module?

- `src/main/java/.../TaxPayer.java` — the vulnerable code to review.
- `src/test/java/.../TaxPayerHackTest.java` — exploits the vulnerabilities. It will **fail** initially; your goal is to get it to pass.
- `src/test/java/.../TaxPayerTest.java` — the regression tests that must keep passing after your fix.
- `hint.txt` — a hint if you get stuck.
- `SOLUTION.md` — the model solution and explanation.

### 🚦 Time to start!

```bash
mvn -pl Level-2 test
```

1. Review the code in `TaxPayer.java`. Can you spot the bug(s)?
2. Try to fix the bug. Open a pull request to `main` or push your fix to a branch.
3. You successfully completed this level when you (a) resolve all related code scanning alerts and (b) when both `TaxPayerHackTest` and `TaxPayerTest` pass 🟢.
4. If you need more guidance, read the CodeQL scanning alerts.

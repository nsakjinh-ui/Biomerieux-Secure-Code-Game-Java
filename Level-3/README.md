## Level 3: Data Bank

_Nicely done! Level 2: Social Network is complete. It's time for Level 3: Database_ :partying_face:

Languages: `java` (Maven, Spring Boot, SQLite via JDBC)

### 📝 Storyline

The data backbone of our platform holds stock prices and historical records. A single rogue query could alter prices or delete tables — and an attacker is already probing endpoints. Can you harden the database layer so users can't inject SQL and rewrite history? Secure it, keep the tests green, and progress to Level 4.

### :keyboard: What's in this module?

- `src/main/java/.../DbCrudOps.java` — the vulnerable code to review.
- `src/test/java/.../DbCrudOpsHackTest.java` — exploits the vulnerabilities. It will **fail** initially; your goal is to get it to pass.
- `src/test/java/.../DbCrudOpsTest.java` — the regression tests that must keep passing after your fix.
- `hint.py` — a hint if you get stuck.
- `SOLUTION.md` — the model solution and explanation.

### 🚦 Time to start!

```bash
mvn -pl Level-3 test
```

1. Review the code in `DbCrudOps.java`. Can you spot the bug(s)?
2. Try to fix the bug. Open a pull request to `main` or push your fix to a branch.
3. You successfully completed this level when you (a) resolve all related code scanning alerts and (b) when both `DbCrudOpsHackTest` and `DbCrudOpsTest` pass 🟢.
4. If you need more guidance, read the CodeQL scanning alerts.

> Heads up: each test resets the database (`level-3.db`, created in this folder) before running, so you don't need to delete it manually between runs — but you can if you want a completely clean slate.

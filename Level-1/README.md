## Level 1: Cyber Monday

_Welcome to Level 1!_ :chess_pawn:

Languages: `java` (Maven)

### 📝 Storyline

Just days before Cyber Monday, an electronics shop rushed its brand-new website online. All the budget went into coding — none into security. The result? A shiny but vulnerable shop. Can you patch the bug, keep the business running, and unlock Level 2?

### :keyboard: What's in this module?

- `src/main/java/.../OrderValidator.java` — the vulnerable code to review.
- `src/test/java/.../OrderValidatorHackTest.java` — exploits the vulnerabilities. It will **fail** initially; your goal is to get it to pass.
- `src/test/java/.../OrderValidatorTest.java` — the regression tests that must keep passing after your fix.
- `hint.js` — a hint if you get stuck.
- `SOLUTION.md` — the model solution and explanation.

### 🚦 Time to start!

From the repo root, or from this folder:

```bash
mvn -pl Level-1 test
```

1. Review the code in `OrderValidator.java`. Can you spot the bug(s)?
2. Try to fix the bug. Ensure `OrderValidatorTest` is still passing 🟢.
3. You successfully completed the level when both `OrderValidatorHackTest` and `OrderValidatorTest` pass 🟢.

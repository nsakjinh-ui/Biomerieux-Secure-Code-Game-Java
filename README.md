# Mission : Code Secure — Java Edition

_Welcome to the mission : Code Secure!_ :wave:

This is the **Java / Maven / Spring Boot port** of Biomerieux's "Mission: Code Secure"
(itself based on GitHub's open source [secure-code-game](https://github.com/skills/secure-code-game)).
Same 5 storylines, same vulnerability classes — rewritten so Java developers can play
in their own language.

To get started, please follow the 🛠️ set up guide.

1. Click **Use this template** (top-right of the repo page) → **Create a new repository**.
   - For owner, choose your personal account or an organization to host the repository.
   - We recommend creating a public repository, as private repositories will
     [use Actions minutes](https://docs.github.com/en/billing/managing-billing-for-github-actions/about-billing-for-github-actions).
2. You can now proceed to the 🛠️ set up section.

## 🛠️ The set up

#### 🖥️ Using Codespaces (recommended)

All levels are configured to run instantly with GitHub Codespaces — no local
installation needed.

1. Click the **Code** drop-down button in the upper-right of your repository
   navigation bar.
2. Click **Create codespace on main**.
3. Relax and wait for the container to build and `mvn install` to run in the
   background (a couple of minutes the first time).
4. At this point, you can get started with Levels 1 through 5 by navigating to the
   respective folder and reading its `README.md` file.

#### 💻 Local setup

You'll need **JDK 17+** and **Maven 3.9+**.

```bash
mvn -v   # sanity check
```

Then, from the repo root:

```bash
mvn install    # builds every level once
```

You are now ready to play!

## 🎮 How to play

Each level is an independent Maven module. For each level:

```bash
mvn -pl Level-N test
```

- `src/main/java/...` — the vulnerable code to review and fix.
- `src/test/java/...XxxHackTest.java` — exploits the vulnerability. It **fails**
  initially; your goal is to make it pass.
- `src/test/java/...XxxTest.java` — the regression tests that must keep passing
  after your fix.
- `hint.*` — a hint if you get stuck.
- `SOLUTION.md` — the model solution and a full write-up.

Levels 2, 3, and 5 are small Spring Boot apps; you can run them with, e.g.:

```bash
mvn -pl Level-5 spring-boot:run
```

| Level | Name           | Vulnerability class                        |
|-------|----------------|---------------------------------------------|
| 1     | Cyber Monday   | Floating-point / business-logic bug          |
| 2     | Social Network | Path traversal                               |
| 3     | Data Bank      | SQL injection                                |
| 4     | Locanda        | Weak password hashing & hardcoded secrets    |
| 5     | Space-Crossing | Cross-Site Scripting (XSS)                   |

Code scanning with CodeQL is enabled at the repo root (`.github/workflows/codeql-analysis.yml`)
for the `java` language — check the Security tab for alerts as you play.

<footer>

---

</footer>

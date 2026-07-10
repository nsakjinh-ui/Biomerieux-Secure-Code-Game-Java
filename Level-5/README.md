## Level 5: Space-Crossing

_Almost there! One level to go and complete the mission!_ :heart:

Languages: `java` (Maven, Spring Boot, Thymeleaf)

### 📝 Storyline

Space enthusiasts built a public website to share facts about planets, with a search bar for visitors to explore. But in their rush, they overlooked a critical web security issue: user input isn't handled safely. Attackers can inject malicious code into the site. Can you secure the search feature, protect the community, and complete the mission?

### :keyboard: What's in this module?

- `src/main/java/.../PlanetController.java` — the vulnerable code to review.
- `src/main/resources/templates/index.html`, `details.html` — the front-end.
- `src/test/java/.../PlanetControllerHackTest.java` — exploits the vulnerability. It will **fail** initially; your goal is to get it to pass.
- `src/test/java/.../PlanetControllerTest.java` — the regression tests that must keep passing after your fix.
- `hint.txt` — a hint if you get stuck.
- `SOLUTION.md` — the model solution and explanation.

### 🚦 Time to start!

```bash
mvn -pl Level-5 test
```

1. Review the code in `PlanetController.java` and `details.html`. Can you spot the bug(s)?
2. Try to fix the bug. Open a pull request to `main` or push your fix to a branch.
3. You successfully completed this level when you (a) resolve all related code scanning alerts and (b) when both `PlanetControllerHackTest` and `PlanetControllerTest` pass 🟢.
4. If you need more guidance, read the CodeQL scanning alerts.
5. Want to see it live in a browser? Run `mvn -pl Level-5 spring-boot:run`, open http://localhost:8080/, and try the payload described in `PlanetControllerHackTest`.

## Finish

🎉 Congratulations, you've completed the mission! 🎉

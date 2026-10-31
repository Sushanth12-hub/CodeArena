# CodeArena

CodeArena is a small Java web application for studying Java OOP and DBMS/SQL through notes and topic quizzes. It intentionally uses the course technologies: Java, JDBC, SQL, HTML, CSS and JavaScript. It includes six topics, 30 starter questions, progress statistics, searchable topics, detailed quiz review, result history, and complete admin MCQ creation/deletion.

## Run locally (Windows)

1. Install a JDK (17 or newer).
2. Run `./run.ps1` from PowerShell.
3. Open `http://localhost:8080`.

The script downloads H2 the first time, compiles the source, creates an embedded database, and starts the site. No MySQL installation is needed for the demonstration. Data is stored in `data/codearena_demo.mv.db` and is retained between restarts.

Demo accounts:

- Admin: `admin@codearena.local` / `Admin@123`
- Or create a student account from the registration page.

## Project structure

- `src/main/java` - model classes, JDBC repositories, services, routing and utilities
- `src/main/resources/database` - schema and starter data
- `src/main/resources/static` - CSS and browser-side validation
- `docs` - diagrams, test checklist, and explanation of concepts

## Notes

Passwords are stored as SHA-256 hashes for this classroom demonstration. A production system should use a deliberately slow password-hashing algorithm such as BCrypt or Argon2, HTTPS, server-managed sessions, CSRF protection, and a real secrets configuration.

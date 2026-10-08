# Constellist

*Complete tasks. Connect stars. Build your universe.*

Constellist is a JavaFX to-do app where each completed task lights a star. Light all 10 stars in a constellation to unlock it in your gallery, then move on to the next one: Heart, Capricorn, Smile, Lightning, Dragonfly, Star, Swan, Dinosaur, Dragon and Jellyfish.

Built by the Constellation Team for NMSU CS 371.

## Team

- Brittany ([bxbritt](https://github.com/bxbritt))
- Caitlyn
- Kleo
- Samantha

## Features

- Accounts with sign up, log in and a forgot-password flow (passwords are stored as SHA-256 hashes)
- Multiple task lists, with up to 10 tasks per list
- A star in the current constellation lights up for each completed task
- A progress page showing remaining and completed tasks next to your current constellation
- A gallery of the constellations you have completed
- Background music, sound effects and an animated starfield

## Requirements

- **JDK 25** or newer
- **Maven 3.9** or newer

You don't need to install JavaFX or the SQLite driver yourself. Maven downloads both.

## Running

```bash
cd TaskManagerProject
mvn javafx:run
```

Press **F11** to toggle fullscreen (Esc also exits it).

On the first run, the app creates `users.db` (a local SQLite database) in the folder you launched it from. The file is git-ignored, so every developer keeps their own accounts and progress.

## Opening in an IDE

- **Eclipse:** use *File → Import → Maven → Existing Maven Projects* and select `TaskManagerProject`.
- **VS Code / IntelliJ:** open the `TaskManagerProject` folder. The IDE picks up `pom.xml` automatically.

To run from the IDE, run **`application.Launcher`**. Don't run `application.Main` directly: launched that way, Java stops with "JavaFX runtime components are missing."

## Project layout

```
TaskManagerProject/
├── pom.xml                 Maven build (JavaFX 27, sqlite-jdbc)
├── src/
│   ├── application/        Java sources and style.css
│   ├── music/              Background music
│   └── sfx/                Sound effects
├── JavaDocs/               Generated API docs (start at JavaDocs/application/package-summary.html)
└── JaCoCo/                 Code coverage report
```

## Notes

- On Linux, JavaFX plays audio through the system's FFmpeg libraries. If your FFmpeg version isn't supported, the app keeps running without sound.
- Password reset emails aren't sent. The reset link is printed to the console.

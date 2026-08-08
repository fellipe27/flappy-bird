# 🐦 Flappy Bird
A Flappy Bird clone built in pure Java using Swing, focusing on game development fundamentals such as physics, animation and collision detection.

## 🎮 Preview
![Gameplay](docs/gameplay.gif)

## 🚀 Features
- Smooth gravity-based movement
- Bird rotation animation
- Pixel-perfect generation
- Infinite pipe generation
- Score and high score system
- Sound effects and background music
- Game states (menu, gameplay, game over)

## 🛠️ Technologies
- Java
- Swing (GUI & rendering)
- Java Sound API

## ⚙️ Requirements
- Java 17+ (recommended Java 25)

## ▶️ How to run
Option 1 - Run from source:
```bash
git clone https://github.com/fellipe27/flappy-bird.git
cd flappy-bird
javac src/*.java
java src.Main
```

Option 2 - Run JAR:
```bash
java -jar flappy-bird.jar
```

## 📦 Download
You can download the latest version here:
https://github.com/fellipe27/flappy-bird/releases

## 🗂️ Project structure
```text
flappy-bird/
    - src/game/             # Game source code
        - managers/         # UI management
        - objects/          # Core game objects
        - states/           # Game state mappers
        - GamePanel.java    # Game scene
        - Main.java         # Game entry point
    - resources/            # Images and sounds
    - docs/                 # Screenshots and GIFs
```

## 📄 License
MIT

## 👨‍💻 Author
Developed by **[Paulo Fellipe](https://github.com/fellipe27)**

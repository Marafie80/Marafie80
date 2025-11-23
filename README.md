# Undertale-Like Game Engine

A comprehensive Java-based game engine inspired by Undertale, featuring turn-based combat, dialogue systems, and RPG mechanics.

## Overview

This repository contains a complete game engine architecture designed for creating Undertale-style RPG games with:

- **Turn-based Combat** with bullet-hell dodge mechanics
- **Dialogue System** with branching conversations
- **World Exploration** using tile-based maps
- **Entity System** (Player, NPC, Enemy, Projectile)
- **UI Framework** including DialogBox, BattleHUD, and SoulController
- **Save/Load System** with JSON persistence

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Build
```bash
mvn clean compile
```

### Architecture
See [ARCHITECTURE.md](ARCHITECTURE.md) for detailed documentation on:
- Package structure and design patterns
- Class relationships and UML diagrams
- Combat flow and game mechanics
- Extension points for custom implementations

## Project Structure

```
src/main/java/com/undertale/
├── engine/        # Core game loop, rendering, input, assets
├── world/         # World, TileMap, collision detection
├── entity/        # Player, NPC, Enemy, Projectile
├── combat/        # CombatManager, AI, battle system
├── ui/            # UIManager, DialogBox, BattleHUD
└── io/            # SaveManager, game state persistence

docs/
├── class-diagram.puml      # UML class diagram
└── battle-sequence.puml    # Combat sequence diagram
```

## Key Features

### Undertale-Inspired Mechanics
- **Mercy System**: Spare enemies instead of fighting
- **Bullet-Hell Combat**: Dodge projectile patterns during enemy turns
- **ACT System**: Special actions that increase mercy chance
- **Soul Movement**: Precise control during combat phases

### Extensible Architecture
- **GameState Interface**: Implement custom game screens
- **CombatAI**: Create unique enemy behavior patterns
- **DialogueTree**: Build branching conversations
- **Entity System**: Easy to extend for new game objects

## Documentation

- [Architecture Guide](ARCHITECTURE.md) - Complete architectural overview
- [Class Diagram](docs/class-diagram.puml) - UML class relationships
- [Battle Sequence](docs/battle-sequence.puml) - Combat flow diagram

## About Me

- 👋 Hi, I'm @Marafie80
- 👀 I'm interested in = {Data Science and AI, Cloud Computing, Blockchain, Software Engineering}
- 🌱 I'm currently learning = {Data science and AI}
- 💞️ I'm looking to collaborate on Data science and AI, Software engineering
- 📫 How to reach me: Marafie_80@hotmail.com
- ⚡ Fun fact: I know prompt engineering and build AI and automation systems and websites before with no coding experience

<!---
Marafie80/Marafie80 is a ✨ special ✨ repository because its `README.md` (this file) appears on your GitHub profile.
You can click the Preview link to take a look at your changes.
--->

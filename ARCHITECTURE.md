# Undertale-Like Game Engine Architecture

A comprehensive game engine inspired by Undertale, featuring turn-based combat, dialogue systems, and world exploration.

## 🎮 Overview

This engine provides a complete framework for creating RPG games with Undertale-style mechanics:
- Turn-based combat with bullet-hell dodge mechanics
- Dialogue system with branching conversations
- World exploration with tile-based maps
- Inventory and item management
- Save/Load system
- Flexible AI system for enemies

## 📐 Architecture

### Package Structure

```
com.undertale
├── engine/        # Core game engine
├── world/         # World and map management
├── entity/        # Game entities (Player, NPC, Enemy)
├── combat/        # Combat system
├── ui/            # User interface components
└── io/            # Save/Load system
```

## 🏗️ Core Components

### Engine Package

**Game** - Main game loop and state management
- Manages game initialization
- Fixed timestep game loop (60 FPS)
- State transitions

**GameState** - Interface for game states
- Implement this for different game screens (menu, gameplay, combat)
- Lifecycle: `enter()` → `update()` → `render()` → `exit()`

**Renderer** - Graphics rendering abstraction
- Draw sprites, text, shapes
- Platform-independent rendering interface

**InputManager** - Input handling
- Keyboard state tracking
- Event queue system
- Axis-based input (for movement)

**AssetManager** - Resource management
- Load and manage sprites and sounds
- Asset caching

### World Package

**World** - Game world container
- Manages entities and tile map
- Handles entity updates and collisions
- Rendering order management

**TileMap** - Grid-based map system
- 2D tile array
- Walkability checking
- Tile-based collision

**Tile** - Individual tile data
- Walkable flag
- Sprite ID reference

### Entity Package

**Entity** (Abstract) - Base class for all game objects
- Position and sprite
- Update and render lifecycle
- Collision callbacks

**Player** - Player character
- Movement control
- Health and mercy stats
- Inventory management
- Combat initiation

**NPC** - Non-player characters
- Dialogue trees
- Interaction callbacks

**Enemy** - Combat enemies
- HP and attack stats
- AI integration
- Defeat handling

**Projectile** - Combat projectiles
- Velocity-based movement
- Damage on collision
- Auto-cleanup when out of bounds

### Combat Package

**CombatManager** - Battle orchestration
- Turn-based combat flow
- Player and enemy action handling
- Projectile management
- Victory/defeat conditions

**BattleState** - Combat phase tracking
- Phase: PLAYER_TURN → ENEMY_TURN → RESOLUTION
- Turn counting

**CombatAI** - Enemy behavior
- Action decision making
- Attack pattern generation

**CombatContext** - Battle state snapshot
- Immutable combat state for AI decisions

**Action** - Combat actions enum
- ATTACK: Deal damage
- ACT: Special actions (increases mercy)
- MERCY: Spare or flee
- ITEM: Use inventory item

### UI Package

**UIManager** - UI system coordinator
- Element lifecycle management
- Input routing to UI elements

**UIElement** (Abstract) - Base UI component
- Position and visibility
- Update and render lifecycle
- Input handling

**DialogBox** - Text display
- Character-by-character animation
- Multi-line support
- Advancement controls

**BattleHUD** - Combat interface
- HP bars
- Action menu
- Enemy stats display

**SoulController** - Player's combat soul
- Movement within battle box
- Collision with projectiles
- Constrained to battle boundaries

### IO Package

**SaveManager** - Persistence system
- JSON-based save files
- Load/save game state
- Save file management

**GameStateData** - Save data container
- Player state
- World state
- Metadata (timestamp, version)

## 🔄 Game Flow

### 1. Initialization
```
Game.init() → Load assets → Set initial GameState
```

### 2. Main Loop
```
Input → Update (fixed timestep) → Render → Repeat
```

### 3. Combat Flow
```
Player interacts with Enemy
→ CombatManager.startBattle()
→ PLAYER_TURN: Choose action
→ ENEMY_TURN: Dodge projectiles
→ RESOLUTION: Check victory/defeat
→ Repeat or end battle
```

### 4. State Transitions
```
GameState.exit() → Game.changeState() → GameState.enter()
```

## 🎯 Key Features

### Undertale-Inspired Mechanics

1. **Mercy System**
   - Players can spare enemies instead of killing
   - ACT actions increase mercy chance
   - Encourages non-violent gameplay

2. **Bullet-Hell Combat**
   - Enemy turn involves dodging projectiles
   - Soul controller for precise movement
   - Collision detection with projectiles

3. **Dialogue Trees**
   - Branching conversations
   - Choice-based interactions
   - NPC personality expression

4. **Save System**
   - Persistent game state
   - JSON format for easy editing
   - Version tracking

## 📊 Class Relationships

See `docs/class-diagram.puml` for complete UML diagram.

### Key Relationships

- **Game** manages **GameState** implementations
- **World** contains **TileMap** and multiple **Entity** objects
- **Player**, **NPC**, **Enemy**, **Projectile** extend **Entity**
- **CombatManager** orchestrates battles between **Player** and **Enemy**
- **UIManager** manages multiple **UIElement** objects
- **BattleHUD** and **SoulController** are **UIElement** implementations

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Building
```bash
mvn clean compile
```

### Running
Implement a `GameState` and set it in your main class:

```java
public class Main {
    public static void main(String[] args) {
        Game game = new Game(640, 480);
        game.init();

        // Set your initial game state
        game.changeState(new YourGameState());

        // Start game loop
        game.gameLoop();
    }
}
```

## 🎨 Extending the Engine

### Creating a Custom Enemy AI

```java
public class SmartEnemyAI extends CombatAI {
    @Override
    public Action decideAction(CombatContext context) {
        // Custom decision logic
        if (context.getPlayerSnapshot().getHealth() < 5) {
            return Action.ACT; // Be merciful
        }
        return Action.ATTACK;
    }

    @Override
    public List<Projectile> generateAttackPattern(int x, int y) {
        // Custom bullet pattern
        // Return list of projectiles
    }
}
```

### Creating a Custom Game State

```java
public class GameplayState implements GameState {
    private World world;
    private Player player;

    @Override
    public void enter() {
        // Initialize world and player
    }

    @Override
    public void update(float dt) {
        world.update(dt);
    }

    @Override
    public void render(Renderer r) {
        world.render(r);
    }

    @Override
    public void exit() {
        // Cleanup
    }

    @Override
    public void handleInput(InputEvent e) {
        // Handle input
    }
}
```

## 📝 Design Patterns Used

- **State Pattern**: GameState system
- **Observer Pattern**: Input event system
- **Component Pattern**: Entity system
- **Manager Pattern**: AssetManager, UIManager, CombatManager
- **Strategy Pattern**: CombatAI

## 🔮 Future Enhancements

- [ ] Animation system
- [ ] Sound effects and music playback
- [ ] Particle effects
- [ ] Multiple save slots
- [ ] Localization support
- [ ] Map editor
- [ ] Scripting system
- [ ] Network multiplayer

## 📄 License

This is an educational architecture demonstration. Feel free to use and modify for your projects.

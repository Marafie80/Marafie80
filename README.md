# Undertale-Inspired 2D Game

A 2D RPG game inspired by Undertale, featuring:
- **Overworld exploration** - Walk around and encounter enemies
- **Bullet-hell battle system** - Dodge enemy attacks with your soul
- **Dialog system** - Interact with enemies through ACT menu
- **Multiple enemy types** - Each with unique attack patterns
- **Spare mechanic** - Show mercy to enemies instead of fighting

## How to Play

### Running the Game
1. Open `index.html` in a web browser
2. The game will start automatically

### Controls

**Overworld Mode:**
- **Arrow Keys** - Move your character
- **SPACE** - Interact with objects/NPCs
- Walk into red enemies to start a battle!

**Battle Mode:**
- **Arrow Keys** - Move your soul (red heart) to dodge bullets
- **Z** - Confirm selection / Select menu option
- **X** - Cancel / Go back
- **C** - Attempt to spare the enemy

### Battle Menu Options
- **FIGHT** - Attack the enemy
- **ACT** - Interact with the enemy (Check, Compliment, Talk)
  - Use ACT options to make enemies spareable!
- **ITEM** - Use items (currently empty)
- **MERCY** - Spare the enemy (only works after using ACT)

### Game Features
- **HP System** - Take damage when bullets hit your soul
- **Gold Rewards** - Earn gold from defeating enemies
- **Enemy Types:**
  - **Froggit** - Hops and shoots flies
  - **Dummy** - Shoots cotton balls, easy to spare

### Tips
- Use the ACT menu to interact with enemies before trying to SPARE them
- Different enemies have different attack patterns - learn to dodge!
- You can't spare an enemy until you've talked to them enough
- If your HP reaches 0, you'll restart with full health

## Game Architecture

The game is built with:
- **HTML5 Canvas** - Graphics rendering
- **Vanilla JavaScript** - Game logic
- **CSS3** - UI styling

### File Structure
```
index.html - Main HTML file
game.js - Complete game logic (player, battles, enemies, bullets)
styles.css - Game styling
```

### Key Game Systems (game.js)
- **Game State Management** - Handles overworld, battle, and dialog states
- **Player Controller** - Movement in overworld and soul control in battles
- **Battle System** - Turn-based combat with bullet-hell mechanics
- **Enemy AI** - Multiple attack patterns and behaviors
- **Dialog System** - Animated text rendering
- **Collision Detection** - For player-enemy and soul-bullet interactions

## About Me
- 👋 Hi, I'm @Marafie80
- 👀 I'm interested in = {Data Science and Ai , Cloud Computing , Blockchain , Software Engineering}
- 🌱 I'm currently learning = {Data science and Ai}
- 💞️ I'm looking to collaborate on Data science and Ai , Software engineering
- 📫 How to reach me e-mail : Marafie_80@hotmail.com
- ⚡ Fun fact: i know prompt engineering and build ai and automation systems and website before with no coding experience

---

*This is a fan-made game inspired by Undertale by Toby Fox. Created for educational purposes.*

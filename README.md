# MeguminAndGremlins

A Java/Processing-based 2D arcade game built with Gradle.  
Control a wizard through dangerous gremlin-filled dungeons, destroy brick walls, collect powerful power-ups, defeat increasingly tough enemies, and face a multiphase final boss to reach the exit and win!

## Key Features

- Modifiable and extensible map design
    - Grid-based levels (36×33 tiles) loaded from text files
    - Level property controlled in config.json file
    - Position of game elements defined using CAPITAL letters
- Versatile enemy types with unique movement logic and defeat conditions
    - Gremlins (G)
    - Mutations (M)
    - Laser Turrets (Z)
    - Final Boss (F)
- Player ability
    - Press 'arrow keys' to move the wizard Megumin (W)
    - Press 'spacebar' to shoot fireball when not cooling down
      - Destructible brick walls (B)
      - Unbreakable stone walls (X)
    - Three collectible power-ups
        - Firestorm Potion (P)
        - Laser Potion (L)
        - ??? Potion (H)
- Special functionalities
    - Press 'K' to kill self
    - Press 'L' to skip the current level

## Installation & Running

### Prerequisites
- Java JDK 8 or higher
- Gradle (wrapper included)

### Steps
```bash
git clone https://github.com/hanchen9127/MeguminAndGremlins.git
cd MeguminAndGremlins
gradle run
```

### License
This project is for academic purposes and does not include a commercial license.
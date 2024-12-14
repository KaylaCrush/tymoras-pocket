# Tymora's Pocket

Tymora's Pocket is a versatile and persistent virtual dice-rolling utility. Named after the goddess of luck, Tymora, this program provides a fun and feature-rich way to manage dice rolls for tabletop RPGs, group games, and more. Tymora's Pocket keeps your dice alive—each die is a persistent object with its own state, roll history, and a touch of personality.

---

## Current Features

- **Persistent Virtual Dice:**
  - Dice have persistent states, including seeds and roll history.
  - Dice have detailed descriptions, including info about "luck" - ie, how well it has been rolling lately

- **DiceBag:**
  - Manages a group's collection of dice.
  - Automatically provides existing or new dice based on requested specifications (e.g., number of sides).
  - Allows saving and loading the entire dice collection.

- **DiceSet (Prototype):**
  - Supports rolling multiple dice simultaneously.
  - Includes functionality for adding constant bonuses to rolls.

- **Core Architecture:**
  - Centralized persistence management to save and load all application data as a single state.
  - Modular design with extensible interfaces and utilities for core dice operations.

---

## Planned Features

- **User Management:**
  - Support for multiple users within a single group.
  - Each user can create personal dice groups and macros.

- **Dice Groups and Macros:**
  - Preferred dice sets for default rolls.
  - Custom roll macros combining multiple rolls and bonuses, e.g., `1d20+7, 2d6+1d4+3`.

- **Interfaces:**
  - A Command Line Interface (CLI) for local use.
  - Discord bot integration to manage group rolls in real time.
  - A web client for graphical user interaction and broader accessibility.

- **Descriptive Roll Feedback:**
  - Generate colorful, dynamic descriptions for rolls based on results (e.g., “critical success!” or “near miss”).

- **Advanced Roll Statistics:**
  - Provide insights into roll probabilities, averages, and other metrics.

- **Cross-Platform Persistence:**
  - Sync dice and user data across devices.

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/tymoras-pocket.git
   cd tymoras-pocket
   ```
2. Build the project:
   ```bash
   ./gradlew build
   ```
3. Run the application:
   ```bash
   ./gradlew run
   ```

---

## Usage

Currently, Tymora's Pocket is in active development. The main functionality focuses on creating, managing, and rolling dice through the DiceBag and DiceSet classes.

Example usage:

```java
DiceBag myBag = new DiceBag("My Lucky Bag");

// Add dice to the bag
myBag.addDie(new Die(6));
myBag.addDie(new Die(20));

// Roll a 20-sided die
Die d20 = myBag.getDice(20, 1).get(0);
System.out.println("Rolled a " + d20.roll());

// Save the DiceBag
myBag.saveBag("dicebag.dat");

// Load the DiceBag
DiceBag loadedBag = DiceBag.loadBag("dicebag.dat");
System.out.println("Loaded Bag: " + loadedBag);
```

---

## Contributing

Contributions are welcome! If you'd like to contribute:

1. Fork the repository.
2. Create a new branch for your feature:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add feature-name"
   ```
4. Push your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a Pull Request.

---

## License

Tymora's Pocket is licensed under the [MIT License](LICENSE).

---

## Future Plans

- Add robust testing for all core components.
- Enhance documentation with detailed usage guides.
- Expand interface support and user management capabilities.


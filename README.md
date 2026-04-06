# Minesweeper

A Java terminal recreation of the classic Minesweeper game, featuring user authentication, configurable board sizes, two game modes, and a persistent high score leaderboard.

**Authors:** Paanini Kartik & Emmett Burns  
**Last modified:** January 22, 2023

---

## Features

- **User authentication** — Register or log in with a username and password. Credentials are stored persistently across sessions.
- **Configurable board size** — Choose a square board between 5×5 and 16×16.
- **Two game modes:**
  - *Limited Time* — Race against a countdown timer to clear the board.
  - *Unlimited Time* — Clear the board at your own pace; your completion time is recorded.
- **High scores** — Unlimited Time completions are saved and can be viewed from the main menu, sorted by fastest time.
- **Safe first click** — The board is generated after your first move, guaranteeing the selected tile and its neighbours are mine-free.
- **Flag support** — Mark suspected mines with flags before revealing.

---

## Project Structure

| File | Description |
|---|---|
| `Main.java` | Entry point. Handles the menu, login system, game loop, score tracking, and display logic. |
| `InnerBoard.java` | Represents the hidden board — stores mine placement and numeric clues. |
| `OuterBoard.java` | Represents the visible board shown to the player — tracks revealed tiles and flags. |
| `highScores.txt` | Persisted completion times and associated usernames. |
| `usernames.txt` | Registered usernames, one per line. |
| `passwords.txt` | Passwords corresponding to each username, one per line. |

---

## Requirements

- Java 8 or later
- A terminal that supports ANSI escape codes (for screen clearing and the ASCII title art)

---

## How to Run

```bash
# Compile all source files
javac Main.java InnerBoard.java OuterBoard.java

# Run the program
java Main
```

---

## Gameplay

1. **Log in or register** when prompted at startup.
2. From the main menu, select:
   - `1` — Play Minesweeper
   - `2` — View instructions
   - `3` — View high scores
   - `4` — Exit
3. Choose a **board dimension** (5–16).
4. Choose a **game mode** (Limited Time or Unlimited Time).
5. Enter coordinates to **reveal** or **flag** tiles.
6. Reveal all non-mine squares to win. Hit a mine and the game ends.

### Mine count

The number of mines scales with board size using the formula:

```
mines = dimension² × (0.10 + 0.02 × √dimension)
```

This keeps smaller boards approachable while making larger boards progressively more challenging.

---

## High Scores

Scores are only recorded in **Unlimited Time** mode. After completing a game, your time is appended to `highScores.txt` and the leaderboard is re-sorted. Select option `3` from the main menu to view the top scores.

---

## Notes

- The `usernames.txt`, `passwords.txt`, and `highScores.txt` files are created automatically on first run if they do not exist.
- Passwords must be at least 8 characters long.
- Enter `back` at any username or password prompt to return to the previous menu.

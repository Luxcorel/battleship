<h1 align="center">Battleship game</h1>

<p align="center">
  <a href="#overview">Overview</a> •
  <a href="#getting-started">Getting started</a>
</p>

<div align="center">
  <table>
    <tr>
      <td>
        <img width="471" alt="The game's main menu" src="https://github.com/user-attachments/assets/38db81cf-428e-4103-b277-613f3497811c">
      </td>
      <td>
        <img width="1035" alt="A game in progress" src="https://github.com/user-attachments/assets/8b0d2899-63e6-4041-9592-abccee798ae8">
      </td>
    </tr>
    <tr>
      <td>
        <p>Main menu</p>
      </td>
      <td>
        <p>Game in progress</p>
      </td>
    </tr>
  </table>
</div>

## Overview

This is the battleship game I and another student did during our first programming course.
During this project, I learned a lot about object-oriented programming in Java
as well as (the horrors of) creating Java Swing user interfaces.
Other things I learned during this project were
how to use GSON for Java object serialization and the hunt and target algorithm
which is used in the "Player vs. AI mode" in the game.
Expect bugs and cursed code as this was one of my first programming projects :)

### Technologies

- Java
- Java Swing
- Google GSON (Java object serialization library)

### Features

- Two player mode
- Single player mode
- Player vs. "AI" mode (hunt and target algorithm)
- Save and load game state
- Scoreboard

### Limitations

- Loading/saving game state is not supported for the player vs. AI mode. If a player vs AI game is saved, it will simply be converted to a two player game when the game state is loaded. This can be fixed by adding a metadata file which describes what type of game is saved. The code is currently only able to figure out if it is multiplayer or singleplayer based on the saved state.
- The single player mode is not very elegant as it is the two player mode with the right board invisible... This can be fixed by sacrificing more time to Java swing.

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8 or higher

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/Luxcorel/battleship
    cd battleship
    ```

2. Build the project:
    ```sh
    ./gradlew build
    ```

3. Run the application:
    ```sh
    ./gradlew run
    ```

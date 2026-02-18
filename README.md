# Football World Cup Score Board (Java)

A **tiny, pure‑Java** library that lets you keep track of live football matches,
update their scores and retrieve a summary ordered by total goals.


## Features

| Operation | Description |
|-----------|-------------|
| `startGame(home, away)` | Adds a new match with an initial score **0‑0**. |
| `finishGame(home, away)` | Removes the match from the board. |
| `updateScore(home, away, homeScore, awayScore)` | Changes the current score (non‑negative integers). |
| `getSummary()` | Returns a list of matches sorted by **total score descending**. If two matches have the same total, the **most recently added** match appears first. |

The whole state lives in memory – perfect for unit‑tests, prototypes or as a building block inside a larger system.

## Quick start (plain Java)

```java
ScoreBoard board = new ScoreBoard();

board.startGame("Mexico", "Canada");
board.startGame("Spain", "Brazil");

board.updateScore("Mexico", "Canada", 0, 5);
board.updateScore("Spain", "Brazil", 10, 2);

for (ScoreBoard.GameInfo info : board.getSummary()) {
    System.out.println(info);
}

// Output:
// Spain 10 - Brazil 2
// Mexico 0 - Canada 5
```

## Design decisions & assumptions

| Decision                           | Reasoning                                                                                                                                                  |
|------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **In‑memory store**                | `LinkedHashMap` guarantees insertion order, needed for the “most recent” tie‑breaker.                                                                      |
| **Key = `home \| away`**           | Guarantees uniqueness while preserving home/away orientation.                                                                                              |
| **long insertionOrder per `Game`** | Provides a clear way to break ties when total scores are equal.                                                                                            |
| **No thread‑safety**               | The library is intended for single‑threaded usage; adding a `synchronized` block or a `Lock` would be trivial if needed.                                   |
| **Exceptions**                     | `IllegalArgumentException` for invalid arguments, `NoSuchElementException` for missing games – mirrors Java’s standard collection behaviour.               |
| **Java version**                   | 11+ (uses `java.time.Instant`). For simplier version it can be removed|                                                                                     |
| **Testing**                        | Full TDD coverage with JUnit 5; no external test libraries required.                                                                                       |
| **Extensibility**                  | Business logic lives only in `ScoreBoard`; persisting the data or adding event listeners would involve creating a new class that uses the same public API. |

## Building & testing

The project follows the standard Maven layout, but the code works equally well with Gradle or a plain IDE.

```bash
# Clone the repo, then:
mvn clean test
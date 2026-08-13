# Where Am I? Mobile Game — Specification

## Overview

This document specifies the behavior of `Where Am I?`, a mobile geography-guessing
game for Android. The player is placed inside a Street View panorama at a target
location in an inhabited city and must place a guess on a standard Google Map.
The game awards a score based on the distance between the target and the guess,
and on whether the guessed country matches the target country. The game stores
every match locally, tracks the total score of the last seven days, and provides
a History screen for reviewing past results.

All documentation and source-code artifacts for this project must be in English.

## Status

Approved

This specification is approved and the implementation phase may begin.

## Scope

### In Scope

- Home screen that allows the player to start a new game.
- Play mode: Street View panorama in an inhabited city, 150-second countdown
  timer, Google Map guess, score calculation, and result persistence.
- History screen that lists all stored matches.
- Local persistence of completed and incomplete matches.
- Weekly score total for the last 7 days.

### Not in Scope

- User accounts, authentication, or authorization.
- Multiplayer or online leaderboards.
- Remote backend server.
- In-app purchases, advertisements, or social features.

## Facts

All facts are written in the **Given-When-Then** format. They describe
observable behavior that must be verifiable through tests.

### Fact 1 — Start New Game

Given the player is on the Home screen  
When the player chooses to start a new game  
Then a target location is selected from the bundled 200-location seed file  
And the selected location is inside an inhabited city  
And the Play screen is displayed  
And the countdown starts at 150 seconds

### Fact 2 — Street View Presentation

Given the Play screen is displayed  
Then the Street View panorama for the target location is rendered  
And the player can move forward, backward, along streets, and rotate the camera  
And the target location used for scoring remains the same regardless of how far
  the player moves inside the Street View panorama  
And the player can pan and zoom the current panorama

### Fact 3 — Open Guess Map

Given the Play screen is displayed  
When the player selects the guess action  
Then a standard Google Map is displayed  
And the map initially shows country borders  
And zooming in reveals city and region names  
And the player can place one marker at a time  
And placing a new marker replaces the previous marker

### Fact 4 — Submit Guess

Given the player has placed a marker inside a continent on the guess map  
When the player taps the submit button on the screen  
Then the guess coordinates are captured  
And the map closes  
And the score is calculated  
And the timer stops

### Fact 5 — Timer Countdown

Given the Play screen is displayed  
When the game starts  
Then the timer shows 150 seconds  
And the timer decreases by one second at each elapsed second  
And the timer continues to run while the guess map is open  
And the remaining time is always non-negative

### Fact 6 — Timer Warning

Given the game is in progress  
When the remaining time becomes 10 seconds or less  
Then the timer display indicates a warning state

### Fact 7 — Time Expiration Without Guess

Given the game is in progress  
When the timer reaches 0 seconds  
And the player has not submitted a guess  
Then the game ends with status `INCOMPLETE`  
And the final score is 0  
And the match is saved to the local history

### Fact 8 — Score Calculation

Given a guess has been submitted before the timer reaches 0  
When the score is calculated  
Then the score is derived from the distance between the target and the guess  
And the base distance score is in the inclusive range `0` to `4000`  
And a country bonus of `1000` is added if the guessed country matches the target country  
And the final score is an integer in the inclusive range `0` to `5000`

### Fact 9 — Match Storage

Given a match has ended  
When the match result is saved  
Then the local history contains a record with the following fields:

| Field            | Description                                           |
|------------------|-------------------------------------------------------|
| `id`             | Auto-generated unique identifier                      |
| `date_played`    | Timestamp when the match ended                        |
| `target_lat_lng` | Target location as `"lat,lng"` rounded to 5 decimals  |
| `guess_lat_lng`  | Player guess as `"lat,lng"` rounded to 5 decimals, or null if none |
| `time_taken_ms`  | Elapsed time in milliseconds                          |
| `score`          | Final score, 0 for incomplete matches                 |
| `status`         | `COMPLETED` or `INCOMPLETE`                           |

### Fact 10 — History Display

Given the player is on the History screen  
When the screen loads  
Then the stored matches are displayed  
And the list is ordered by `date_played` in descending order  
And each item shows the match status, score, and date

### Fact 11 — Weekly Total Score

Given the player is on the Home or History screen  
When the weekly score is requested  
Then the game sums the `score` of all stored matches where `date_played` is within the last 168 hours  
And the total is displayed

## Rules

Rules describe deterministic input-output mappings. They are the contract the
system must honor.

### Rule 1 — Game Timer

- **Input:** a start command and a clock source that reports elapsed seconds.
- **Pre-condition:** game is in progress, no guess has been submitted, remaining time `> 0`.
- **Output:** `remainingSeconds = 150 - elapsedSeconds`.
- **Post-condition:**
  - `remainingSeconds` is clamped to a minimum of `0`.
  - When `remainingSeconds <= 10`, the warning state is active.
  - When `remainingSeconds == 0` and no guess exists, the game ends as `INCOMPLETE`.

### Rule 2 — Distance Score

- **Input:** distance in kilometers between target and guess.
- **Pre-condition:** guess coordinates are available, on land, and inside a
  recognized continent.
- **Output:** a base score in the inclusive range `0` to `4000`.
- **Mapping:**
  - At `0 km`, the base score is `4000`.
  - The score decays exponentially as distance increases.
  - For any distance `d` in kilometers:
    `baseScore = max(0, floor(4000 * exp(-d / SCORE_DECAY_CONSTANT)))`.
  - `SCORE_DECAY_CONSTANT` is `2500` km.
  - The function must be fully defined and testable without API calls.

### Rule 3 — Country Bonus

- **Input:** target coordinates and guess coordinates.
- **Pre-condition:** both coordinates are on land, inside a continent, and have an
  associated country resolved by the geocoding service.
- **Output:**
  - If the resolved `short_name` (ISO 3166-1 alpha-2) country codes are equal, add `1000` to the base score.
  - Otherwise, add `0` to the base score.
- **Post-condition:** the final score does not exceed `5000`.

### Rule 4 — Incomplete Match

- **Input:** timer reaches `0` without a submitted guess.
- **Pre-condition:** no guess coordinates exist.
- **Output:**
  - `score = 0`
  - `status = INCOMPLETE`
  - `guess_lat_lng = null`
  - `time_taken_ms` equals the configured maximum time (`150000` milliseconds).
- **Post-condition:** the match record is persisted to local storage.

### Rule 5 — Completed Match

- **Input:** a guess is submitted before the timer reaches `0`.
- **Pre-condition:** valid target coordinates and guess coordinates inside a continent.
- **Output:**
  - screen shows maps with target and guess markers with a line connecting them
  - `score` is calculated by Rule 2 and Rule 3.
  - `status = COMPLETED`.
  - `time_taken_ms` equals the number of milliseconds between game start and
    guess submission.
  - `guess_lat_lng` contains the submitted coordinates.
- **Post-condition:** the match record is persisted to local storage.

### Rule 6 — Guess on Continent

- **Input:** a latitude/longitude coordinate selected by the player.
- **Pre-condition:** the player is placing a guess marker.
- **Output:**
  - If the coordinate is inside a recognized continent, the marker is accepted.
  - If the coordinate is over ocean, on an unrecognized land area, or outside any
    continent, the marker is rejected and the player must place a new marker.

### Rule 7 — Retry Policy for External Services

- **Input:** a request to load the Street View panorama or resolve a country.
- **Pre-condition:** the network call or SDK request is available.
- **Output:**
  - The request is retried up to 3 times only for the following conditions:
    - Network timeout.
    - 5xx server errors.
    - Absence of network connectivity.
  - The request is not retried for non-retryable failures such as 4xx client
    errors or invalid API keys.
  - After 3 failures, the game shows an error screen, logs the error, and
    returns the player to the Home screen.

### Rule 8 — Weekly Score

- **Input:** current UTC timestamp and the stored match list.
- **Pre-condition:** none.
- **Output:** `weeklyScore = sum(score)` for all matches where
  `date_played >= currentUtcTimestamp - 168 hours`.
- **Post-condition:** the value is non-negative.

### Rule 9 — Target Location Selection

- **Input:** a bundled seed file containing 200 inhabited-city locations and a
  `locationSeed` integer.
- **Pre-condition:** the seed file contains at least one entry.
- **Output:** `targetLocation = pseudoRandom(locationSeed, seedFile)` where the
  result is one item from the file.
- **Post-condition:** the same `locationSeed` always produces the same
  `targetLocation`, enabling deterministic tests.

## Test Cases

| ID     | Maps to             | Description                                                  |
|--------|---------------------|--------------------------------------------------------------|
| TC-001 | Fact 1              | Play screen loads after player starts new game               |
| TC-002 | Fact 2              | Street View allows free navigation and camera movement       |
| TC-003 | Fact 3              | Guess map shows country borders and city names on zoom       |
| TC-004 | Fact 4              | Tapping submit captures a continental guess and scores       |
| TC-005 | Rule 1              | Timer counts down from 150 to 0 at one-second intervals      |
| TC-006 | Rule 1              | Timer warning activates at 10 seconds or less                |
| TC-007 | Rule 4, Fact 7      | Expired match without guess is saved as `INCOMPLETE` with 0  |
| TC-008 | Rule 2, Fact 8      | Base score is 4000 at 0 km distance                          |
| TC-009 | Rule 2              | Base score decays exponentially as distance increases        |
| TC-010 | Rule 3              | Country bonus adds 1000 when countries match                 |
| TC-011 | Rule 3              | No bonus when countries differ                               |
| TC-012 | Rule 5, Fact 9      | Completed match is persisted with all fields                 |
| TC-013 | Fact 10             | History list is ordered by most recent match                 |
| TC-014 | Edge Case E-001     | Guess at exactly 0 seconds is accepted and scored            |
| TC-015 | Edge Case E-003     | Geocoding failure yields no country bonus                    |
| TC-016 | Rule 6              | Marker placed on ocean is rejected                           |
| TC-017 | Rule 8              | Weekly score sums last 168 hours of matches                  |
| TC-018 | Rule 7              | After 3 retryable failures, error screen returns Home        |
| TC-019 | Rule 9              | Same seed always selects the same target location            |
| TC-020 | Fact 3              | New marker replaces previous marker                          |
| TC-021 | Rule 6              | Marker outside a continent is rejected                       |

## Edge Cases

- **E-001 — Guess at time 0:** If the player taps submit at the exact
  moment the timer reaches 0, the guess is accepted and scored.
- **E-002 — Maximum distance:** If the guess is on the exact opposite side of
  the Earth, the base distance score is close to `0`.
- **E-003 — Geocoding unavailable:** If the geocoding service cannot resolve a
  country, the country bonus is skipped and the score uses only the distance
  component.
- **E-004 — Antimeridian and poles:** Distance and country calculations must
  remain correct for coordinates that cross the antimeridian or are near the
  poles.
- **E-005 — Storage full or unavailable:** If local storage fails, the game
  reports an error but must not crash.
- **E-006 — Empty history:** When no matches have been played, the History
  screen displays an empty state.
- **E-007 — Weekly boundary:** A match that is exactly 168 hours old is
  included; a match older than 168 hours is not.

## Error Conditions

- **EC-001 — Street View load failure:** If the Street View panorama cannot be
  loaded after 3 retries for retryable failures, the Play screen displays an
  error, logs the error, and returns the player to the Home screen.
- **EC-002 — Geocoding failure:** If the geocoding request fails after 3 retries
  for retryable failures, the country bonus is not applied; the distance score
  is still computed and stored.
- **EC-003 — Storage failure:** If writing the match to local storage fails,
  the user is notified and the match is discarded from the local history.
- **EC-004 — Guess outside a continent:** If the player places a marker on
  ocean, an unrecognized land area, or outside any continent, the marker is
  rejected and the map remains open for a new guess.

## Constraints

- All documentation and source code must be in English.
- The user interface is implemented with Jetpack Compose.
- State management uses Kotlin Coroutines and `StateFlow`.
- Local data persistence uses Room.
- Dependency injection uses Hilt.
- Map services use the Google Maps SDK and Google Street View SDK.
- Country resolution uses a geocoding service.
- The score calculation does not depend on the AI model or on randomness;
  it is fully deterministic for the same inputs.
- The timer must be testable through an injectable clock abstraction.
- Target locations are read from a bundled seed file with 200 inhabited cities
  distributed around the world.
- The guess map is a standard Google Map that shows country borders by default
  and reveals city and region names as the player zooms in.
- A guess marker must be inside a recognized continent; markers on ocean or
  outside continents are rejected.
- A new guess marker replaces the previous marker.
- The target coordinates used for scoring do not change as the player navigates
  inside the Street View panorama.
- External service calls are retried up to 3 times only for timeout, 5xx, or
  lack of connectivity, then return to Home.
- Match coordinates are stored as `"lat,lng"` strings rounded to 5 decimal
  places.
- The weekly score window is a rolling 168 hours from the current UTC timestamp.

## Addendum — History to Ranking UI Change

After the original specification was approved and implemented, the "History" feature was updated to a "Ranking" feature to better reflect the user-facing behavior. The following changes apply to the presentation layer and are now in effect:

- The Home screen button previously labeled "History" now reads "Ranking".
- The navigation route, screen, and ViewModel were renamed from `history` to `ranking` (`RankingScreen`, `RankingViewModel`, `RankingMatch`).
- The screen title is "Ranking Top 5".
- The list displays the top 5 matches with the highest `score` across all stored matches, with no weekly time window.
- The "Total Score" shown is the sum of the top 5 scores.
- Each item shows the match status, guessed country (when available), score, and the `date_played` formatted as `dd/MM - HH:mm` using the 24-hour clock.
- The domain use case `GetMatchHistoryUseCase` was renamed to `GetAllMatchesUseCase`; the ranking computation (top 5 + total) is performed in `RankingViewModel`.

The remainder of the specification remains unchanged and has been delivered as planned.

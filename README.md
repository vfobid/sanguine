# Sanguine - Turn-Based Strategy Game
Developed in collaboration with Shreya Nanda 

A Java-based implementation of a turn-based strategy game inspired by Queen's Blood, featuring player-vs-player and player-vs-computer modes with configurable opponent strategies.

## Overview

Sanguine is a grid-based card game where two players (Red and Blue) compete by placing cards on a shared board to maximize their row scores. The game includes:
- **Player vs Player** mode with graphical interface
- **Player vs Computer** mode with configurable opponent strategies
- **MVC architecture** separating game logic, display, and user input
- **Strategy pattern** for extensible computer opponent behavior

## Project Structure
```
src/
├── model/          # Game state, rules, and card logic
├── view/           # GUI components and rendering
├── controller/     # User input handling and game flow
└── strategy/       # Computer opponent decision-making algorithms
```

## Core Components

### Model (`SanguineModel`)
- Manages board state (grid of `SanguineElement` objects: cards or pawn clusters)
- Handles card placement, influence application, and win condition checking
- Reads deck configurations from files using `FileReader`
- Uses `Player` enum (RED, BLUE) for turn tracking

### View (`JFrameView`)
- **Board Panel**: Displays game grid with card placements
- **Hand Panel**: Shows current player's available cards
- **Deck Panel**: Displays remaining cards in player's deck
- Interactive card/cell selection with cyan highlighting
- Dialog boxes for illegal move notifications and game-over states

### Controller (`BasicSanguineController`)
- Implements `FeaturesListener` for mouse clicks and keyboard input
- Validates moves before applying them to the model
- Coordinates turn switching between human and computer players
- Keyboard controls: `Enter` confirms moves, `Space` passes turn

### Strategy (`SanguineStrategy`)
- **Interface**: Defines `chooseMove()` method for opponent decision-making
- **Implementations**: Multiple strategy variants for different difficulty levels
- Computer opponent evaluates board state and selects optimal card placements

## Game Rules

### Setup
```java
SanguineModel model = new BasicSanguineModel(3, 5); // 3 rows, 5 columns
List redDeck = model.createRedDeck("path/to/red_deck.txt");
List blueDeck = model.createBlueDeck("path/to/blue_deck.txt");
model.startGame(3, redDeck, blueDeck, false); // 3-card hand, no shuffle
```

### Gameplay
1. Players alternate placing cards from their hand onto the board
2. Cards apply **influences** to adjacent cells (modifying card ownership or values)
3. Each row's score is calculated from owned cards
4. Game ends when no more legal moves remain
5. Winner is determined by total score across all rows

### Board Representation (Text View)
- `_` = Empty cell
- `R` = Red card
- `B` = Blue card
- `1`, `2`, `3` = Pawn clusters (1-3 pawns)

## Key Features

### Observer Pattern
- Model notifies controllers via `ModelListener` when turns switch
- View updates automatically on state changes
- Supports multiple simultaneous controllers (e.g., two computer opponents)

### Player Abstraction
- `PlayerActions` interface enables human and machine players
- `HumanPlayer`: Input handled through view
- `MachinePlayer`: Uses strategy to compute moves automatically

### Extensibility
- `Influence` interface allows custom card effects
- Strategy pattern enables new opponent behaviors without modifying existing code
- View components can be extended for different rendering styles

## Development History

### Phase 1: Core Architecture
- Implemented MVC pattern with model, view, and controller separation
- Developed card placement mechanics and influence system
- Built file-based deck configuration system
- Created text-based view for game state visualization

### Phase 2: Architecture Refinements
- Enhanced type safety with `Player` enum (RED/BLUE)
- Centralized move validation logic in model layer
- Improved error handling by replacing boolean returns with exceptions
- Streamlined player-specific data access methods
- Added direct board dimension accessors

### Phase 3: Graphical User Interface
- Built Swing-based interface with three specialized panels (board, hand, deck)
- Implemented interactive card/cell selection with visual highlighting
- Added keyboard shortcuts (`Enter` to confirm, `Space` to pass)
- Created `ViewModel` wrapper to enforce read-only model access from view
- Integrated mouse click handling for intuitive gameplay

### Phase 4: Computer Opponent System
- Designed strategy pattern for extensible opponent behavior
- Implemented `Move` class to encapsulate card placement decisions
- Created multiple strategy implementations for varying difficulty levels
- Added `canBePlaced()` observation method for strategy evaluation
- Developed mock model for isolated strategy testing

### Phase 5: Multiplayer & Game Flow
- Implemented observer pattern with `ModelListener` for turn notifications
- Created `PlayerActions` abstraction supporting human and computer players
- Built `MachinePlayer` with strategy-based automated decision-making
- Added game-over detection and winner announcement dialogs
- Separated game initialization from controller responsibilities for multi-controller support

## Testing

- **Mock Model**: Logs method calls to verify correct controller behavior
- **Strategy Tests**: Use mock model with configurable game states to test opponent decision-making
- **Controller Tests**: Verify listener methods and input handling
- **Test Scripts**: See `scripts/` folder for detailed test scenarios and walkthroughs

## Usage Example
```java
// Initialize model and decks
SanguineModel model = new BasicSanguineModel(3, 5);
List redDeck = FileReader.readDeck("red_deck.txt");
List blueDeck = FileReader.readDeck("blue_deck.txt");

// Create players
PlayerActions redPlayer = new HumanPlayer();
PlayerActions bluePlayer = new MachinePlayer(new OptimalStrategy());

// Set up controllers and views
IView redView = new JFrameView(model);
IView blueView = new JFrameView(model);
SanguineController redController = new BasicSanguineController(model, redView, redPlayer);
SanguineController blueController = new BasicSanguineController(model, blueView, bluePlayer);

// Start game
model.startGame(3, redDeck, blueDeck, false);
redController.playGame();
blueController.playGame();
```

## Design Decisions

1. **Turn Management**: Controller doesn't auto-switch turns after moves to allow for pass actions
2. **Board Orientation**: Model stores board from Red's perspective; view handles Blue's inverted view
3. **Error Handling**: Methods throw exceptions instead of returning booleans for clearer failure reasons
4. **Deck Creation**: Moved from model to controller to separate file I/O concerns
5. **Game Initialization**: Called in main method rather than controller to support multi-controller scenarios

## Technical Highlights

- **Design Patterns**: MVC, Strategy, Observer
- **Java Features**: Enums, interfaces, exceptions, Swing GUI
- **Testing**: Mock objects, unit tests, integration tests
- **Code Organization**: Clear separation of concerns across packages

Developed collaboratively by Vanessa Fobid and Shreya Nanda as part of Northeastern University's Object-Oriented Design course.
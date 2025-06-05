package project.GameState;

import project.CaveSystem.CaveManager;
import project.CaveSystem.GameLocations;
import project.GameManager;
import project.Helper;
import project.ScoreManager;
import project.TriviaManager;
import project.UI.UIManager;
import project.UI.elements.UIMap;

public class DungeonState implements IState {
    private CaveManager caveMan;
    private TriviaManager triviaMan;
    private DungeonTurnState turnState = DungeonTurnState.Completed;

    @Override
    public void Init() {
        caveMan = new CaveManager();
        GameManager.activeRoom = CaveManager.rooms[1];

        triviaMan = new TriviaManager();

        GameLocations.randomizeLocations();

        UIManager.instance.setActiveRoom();
        UIManager.instance.renderScreen();
    }

    @Override
    public void Run() {
        processTurn();

        switch (turnState) {
            case Completed:
                GameLocations.wumpus.turn();
                ScoreManager.score.turnNum++;
                return;
            case LossByCoins: endGame("You Lost! Ran out of Coins!"); break;
            case LossByPit: endGame("You Lost! Fell to death!"); break;
            case LossByArrows: endGame("You Lost! You ran out of arrows and the WUMPUS mauled you!"); break;
            case LossByWumpus: endGame("You Lost! The wumpus slaughtered you!"); break;
            case Win: endGame("You Win! You killed the WUMPUS!"); break;
        }
    }

    private void processTurn() {
        UIManager.instance.addWarning(GameLocations.getAdjacentHazards(GameManager.activeRoom));
        UIManager.instance.renderScreen();
        UIManager.clearTextBelowScreen();

        if (GameLocations.checkWumpus(GameManager.activeRoom.roomNum)) {
            if (!wumpusEncounterAndWin("The WUMPUS snuck up on you!")) return;
        }

        turnState = DungeonTurnState.NotCompleted;
        System.out.println("Enter Turn Choice: ");
        var input = GameManager.scan.next().toLowerCase();

        switch (input) {
            case "m":
                turnState = DungeonTurnState.Completed;
                getMoveInput();
                break;

            case "mne", "mn", "mnw", "msw", "ms", "mse":
                turnState = DungeonTurnState.Completed;
                getMoveInput(input.substring(1));
                break;

            case "s":
                turnState = DungeonTurnState.Completed;
                getShootInput();
                break;

            case "sne", "sn", "snw", "ssw", "ss", "sse":
                turnState = DungeonTurnState.Completed;
                getShootInput(input.substring(1));
                break;

            case "pa":
                turnState = DungeonTurnState.Completed;
                switch (triviaMan.askTrivia(3, 2)) {
                    case 0: System.out.println("Purchase Failed"); break;
                    case 1:
                        ScoreManager.score.arrows += 2;
                        UIManager.instance.renderScreen();
                        System.out.println("2 Arrows purchased");
                        break;
                    case 2: turnState = DungeonTurnState.LossByCoins; return;
                }
                break;

            case "ps":
                turnState = DungeonTurnState.Completed;
                switch (triviaMan.askTrivia(3, 2)) {
                    case 0: System.out.println("Purchase Failed"); break;
                    case 1: generateSecret(); break;
                    case 2: turnState = DungeonTurnState.LossByCoins; return;
                }
                break;

            case "map":
                UIManager.instance.clearAllLayers();
                var map = new UIMap();
                map.generateMap(caveMan.rooms);

                UIManager.instance.renderScreen();

                input = GameManager.scan.next();

                UIManager.instance.setActiveRoom();
                UIManager.instance.renderScreen();
                break;

            case "exit":
                GameManager.setState(State.MainMenu);
                return;
        }

        Helper.sleep(500);
    }

    private void getMoveInput() { getMoveInput(""); }
    private void getMoveInput(String input) {
        input = tryGetDirectionInput(input, "Move", true);
        if (input == null) return;

        var roomInput = tryGetRoomInput(input);
        if (roomInput != -1) {
            GameManager.activeRoom = caveMan.rooms[roomInput];
            GameManager.activeRoom.discovered = true;
            UIManager.instance.transitionToDistantRoom();
            return;
        }

        var direction = Helper.NAME_TO_DIRECTION.get(input.toUpperCase());
        ScoreManager.score.tryAddGold();

        GameManager.activeRoom = caveMan.rooms[CaveManager.getConnectedRoomNum(
                GameManager.activeRoom.roomNum, direction)];
        GameManager.activeRoom.discovered = true;

        UIManager.instance.transitionToAdjacentRoom(direction);

        checkRoomHazards();
    }

    private void getShootInput() { getShootInput(""); }
    private void getShootInput(String input) {
        input = tryGetDirectionInput(input,"Shoot", false);
        if (input == null) return;

        var direction = Helper.NAME_TO_DIRECTION.get(input.toUpperCase());
        var hasArrows = ScoreManager.score.tryRemoveArrows();

        UIManager.instance.setActiveRoom();
        UIManager.instance.arrowAnimation(direction);
        UIManager.instance.renderScreen();

        var roomShotInto = CaveManager.getConnectedRoomNum(GameManager.activeRoom.roomNum, direction);
        if (GameLocations.wumpus.roomNum == roomShotInto) {
            ScoreManager.score.wumpusKilled = true;
            turnState = DungeonTurnState.Win;
        }
        else {
            System.out.println("You missed!");
            if (!hasArrows) turnState = DungeonTurnState.LossByArrows;
            else GameLocations.wumpus.setAwake(true);
        }
    }

    private String tryGetDirectionInput(String input, String typeText, boolean allowTeleport) {
        UIManager.instance.changeToDirectionPrompt();
        UIManager.instance.renderScreen();
        UIManager.clearTextBelowScreen();

        if (input.isEmpty()) {
            System.out.println("Enter " + typeText + " Direction: ");
            input = GameManager.scan.next();
        }

        while (!isValidMoveInput(input, allowTeleport)){
            if (input.equalsIgnoreCase("exit")) {
                GameManager.setState(State.MainMenu);
                return null;
            }
            UIManager.clearTextBelowScreen();
            System.out.println("Invalid Direction! Enter " + typeText + " Direction: ");
            input = GameManager.scan.next();
        }
        return input;
    }

    private static boolean isValidMoveInput(String input, boolean allowTeleport) {
        if (allowTeleport && tryGetRoomInput(input) != -1) return true;
        if (!Helper.NAME_TO_DIRECTION.containsKey(input.toUpperCase())) return false;
        return GameManager.activeRoom.connectedRooms[Helper.NAME_TO_DIRECTION.get(input.toUpperCase())];
    }

    private static int tryGetRoomInput(String input) {
        if (input.charAt(0) == 'r') {
            try {
                var num = Integer.parseInt(input.substring(1));
                if (num < 0) return -1;
                return num % CaveManager.TOTAL_ROOMS;
            }
            catch (NumberFormatException e) { }
        }
        return -1;
    }

    private void checkRoomHazards() {
        if (GameLocations.checkWumpus(GameManager.activeRoom.roomNum)) {
            if (!wumpusEncounterAndWin("You encountered the WUMPUS!")) return;
        }

        if (GameLocations.checkPit(GameManager.activeRoom.roomNum)) {
            System.out.println("You encountered a pit!");
            Helper.sleep(1500);

            switch (triviaMan.askTrivia(3, 2)) {
                case 0: turnState = DungeonTurnState.LossByPit; break;
                case 1:
                    GameManager.activeRoom = caveMan.rooms[1];
                    UIManager.instance.transitionToDistantRoom();
                    break;
                case 2: turnState = DungeonTurnState.LossByCoins; break;
            }
        }
        else if (GameLocations.checkBat(GameManager.activeRoom.roomNum)) {
            System.out.println("You encountered a giant bat!");
            Helper.sleep(1500);

            var randRoom = GameManager.rand.nextInt(CaveManager.TOTAL_ROOMS - 1);
            if (randRoom >= GameManager.activeRoom.roomNum) randRoom++;

            GameManager.activeRoom = caveMan.rooms[randRoom];
            GameManager.activeRoom.discovered = true;
            UIManager.instance.transitionToDistantRoom();

            checkRoomHazards();
        }
    }

    private boolean wumpusEncounterAndWin(String text) {
        System.out.println(text);
        Helper.sleep(1500);

        switch (triviaMan.askTrivia(5, 3)) {
            case 0: turnState = DungeonTurnState.LossByWumpus; break;
            case 1:
                GameLocations.wumpus.defeatedInTrivia();
                System.out.println("The WUMPUS was temporarily scared away.");
                Helper.sleep(1500);
                return true;
            case 2: turnState = DungeonTurnState.LossByCoins; break;
        }
        return false;
    }

    private void generateSecret() {
        String text = switch (GameManager.rand.nextInt(8)) {
            case 0 -> "There is a pit in room "+GameLocations.pit1Room;
            case 1 -> "There is a pit in room "+GameLocations.pit2Room;
            case 2 -> "There is a bat in room "+GameLocations.bat1Room;
            case 3 -> "There is a bat in room "+GameLocations.bat2Room;
            case 4 -> "The WUMPUS is in room "+GameLocations.wumpus.roomNum;
            case 5 -> "You are in room "+GameManager.activeRoom.roomNum+" :)";
            case 6 -> "The WUMPUS "+(GameLocations.isWumpusWithin2Rooms() ? "is" : "is not")
                            + " within 2 rooms of you!";
            default -> "The answer is Yes!";
        };
        System.out.println(text);
        Helper.sleep(1000);
    }

    private static void endGame(String text) {
        GameManager.scoreMan.trySaveCurrentScore();
        System.out.println(text);
        Helper.sleep(3000);
        GameManager.setState(State.MainMenu);
    }
}

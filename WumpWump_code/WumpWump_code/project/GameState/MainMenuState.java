package project.GameState;

import project.GameManager;
import project.ScoreManager;
import project.UI.Point;
import project.UI.UILayer;
import project.UI.UIManager;
import project.UI.elements.UITopScores;

public class MainMenuState implements IState{
    @Override
    public void Init() {
        UIManager.instance.clearAllLayers();
        UIManager.instance.setMainMenuUI();
        UIManager.instance.renderScreen();
    }

    @Override
    public void Run() {
        var input = GameManager.scan.next().toLowerCase();
        UIManager.clearTextBelowScreen();

        switch (input) {
            case "play", "p":
                var name = getPlayerName();
                if (name.isEmpty()) return;

                ScoreManager.resetScore();
                ScoreManager.score.player = name.toUpperCase();

                ScoreManager.score.seed = getPlayRandom() ? GameManager.rand.nextLong() : getSeed();
                GameManager.rand.setSeed(ScoreManager.score.seed);

                GameManager.setState(State.Dungeon);
                break;

            case "score", "scores", "top", "topscores", "top scores", "s", "t":
                UIManager.instance.clearLayer(UILayer.Game);
                UIManager.instance.addElement(new UITopScores(new Point(1, 1)), UILayer.UI);
                UIManager.instance.renderScreen();

                GameManager.scan.next();
                UIManager.instance.clearLayer(UILayer.UI);
                UIManager.instance.setMainMenuUI();
                UIManager.instance.renderScreen();
                break;

            case "exit", "e":
                UIManager.clearTextBelowScreen();
                GameManager.exitApp = true;
                break;
        }
    }

    private static String getPlayerName() {
        UIManager.clearTextBelowScreen();

        System.out.println("Enter 3 letter player name: ");
        var input = GameManager.scan.next();

        while (!isValidName(input)){
            if (input.equalsIgnoreCase("exit")) return null;
            UIManager.clearTextBelowScreen();
            System.out.println("Invalid name! Enter 3 letter player name: ");
            input = GameManager.scan.next();
        }
        return input;
    }

    private static boolean isValidName(String name) {
        if (name.length() != 3) return false;
        return containsOnlyLetters(name);
    }

    private static boolean containsOnlyLetters(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLetter(str.charAt(i))) return false;
        }
        return true;
    }

    private boolean getPlayRandom() {
        UIManager.clearTextBelowScreen();

        System.out.println("Would you like to play a random dungeon? ");
        var validatedInput = validateYesOrNo(GameManager.scan.next());

        while (validatedInput == -1) {
            UIManager.clearTextBelowScreen();
            System.out.println("Invalid Answer, Would you like to play a random dungeon? ");
            validatedInput = validateYesOrNo(GameManager.scan.next());
        }

        return validatedInput == 1;
    }

    private int validateYesOrNo(String input) {
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return 1;
        }
        if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return 0;
        }
        return -1;
    }

    private long getSeed() {
        UIManager.clearTextBelowScreen();

        System.out.println("Enter seed: ");
        var input = GameManager.scan.next();

        while (!isValidLong(input)) {
            UIManager.clearTextBelowScreen();
            System.out.println("Invalid Answer, Enter seed: ");
            input = GameManager.scan.next();
        }

        return Long.parseLong(input);
    }

    public static boolean isValidLong(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (NumberFormatException e) { return false; }
    }
}

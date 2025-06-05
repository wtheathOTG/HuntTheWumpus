package project.UI;

import java.util.ArrayList;
import java.util.Arrays;

import project.GameManager;
import project.Helper;
import project.UI.elements.*;

public class UIManager {
    public static UIManager instance;

    public static final int SCREEN_WIDTH = 68;
    public static final int SCREEN_HEIGHT = 24;

    private static final int transitionSpeed = 150;

    private ArrayList<UIElement> uiLayer = new ArrayList<>();
    private ArrayList<UIElement> gameForegroundLayer = new ArrayList<>();
    private final ArrayList<UIElement> gameLayer = new ArrayList<>();

    private final UIElement[] dungeonCoreUI;
    private final ArrayList<UIElement> activeUIRoomGroup = new ArrayList<>();
    private UIPlayer playerElement;
    private final ArrayList<UIWarning> warnings = new ArrayList<>();
    private final Point roomPos = new Point(SCREEN_WIDTH / 2 - 11, SCREEN_HEIGHT / 2 - 6);

    private static final Point[] offsetByDirection = new Point[] {
            new Point(20, -5),
            new Point(0, -10),
            new Point(-20, -5),
            new Point(-20, 5),
            new Point(0, 10),
            new Point(20, 5)
    };

    private UIManager() {
        dungeonCoreUI = new UIElement[] {
                new UIScoreBoard(new Point(1, 1)),
                new UIInventory(new Point(1, SCREEN_HEIGHT - 3)),
                new UIRoomNum(new Point(2, 3)),
        };
    }

    public static void initSingleton() { if (instance == null) instance = new UIManager(); }

    public void addElement(UIElement element, UILayer layer) {
        switch (layer) {
            case Game -> gameLayer.add(element);
            case GameForeground -> gameForegroundLayer.add(element);
            case UI -> uiLayer.add(element);
        }
    }

    public void clearLayer(UILayer layer) {
        switch (layer) {
            case Game -> gameLayer.clear();
            case GameForeground -> gameForegroundLayer.clear();
            case UI -> uiLayer.clear();
        }
    }

    public void clearAllLayers() {
        gameLayer.clear();
        gameForegroundLayer.clear();
        uiLayer.clear();
    }

    public static void clearTextBelowScreen() {
        int row = SCREEN_HEIGHT;
        int col = 0;

        setCursorPos(row, col);

        var builder = new StringBuilder();
        for (var i = 0; i < 5; i++) {
            for (var j = 0; j < 100; j++) builder.append(' ');
            builder.append('\n');
        }
        System.out.print(builder);

        setCursorPos(row, col);
    }

    public void renderScreen() {
        var renderStartTime = System.currentTimeMillis();
        var render = new String[SCREEN_HEIGHT];

        var allElements = new ArrayList<ArrayList<UIElement>>();
        allElements.add(0, sortElementsByXPos(gameLayer));
        allElements.add(1, sortElementsByXPos(gameForegroundLayer));
        allElements.add(2, sortElementsByXPos(uiLayer));

        for (var zElements : allElements) {
            for (var element : zElements) element.render();
        }

        moveCursorToTop();
        var builder = new StringBuilder();
        renderBackground(render, builder);

        for (var zElements : allElements) {
            for (var i = 1; i < SCREEN_HEIGHT - 1; i++) {
                renderScreenRow(render, i, getElementsAtHeight(zElements, i), builder);
            }
        }

        builder.setLength(0);
        for (var row : render) {
            builder.append(row);
            builder.append("\n");
        }

        System.out.print(builder);
        clearTextBelowScreen();
        //System.out.println("Render time ms: " + (System.currentTimeMillis() - renderStartTime));
    }

    private void renderBackground(String[] render, StringBuilder builder) {
        builder.append('+');
        builder.append("-".repeat(SCREEN_WIDTH - 2));
        builder.append('+');
        render[0] = builder.toString();

        for (var i = 1; i < SCREEN_HEIGHT - 1; i++) {
            builder.setLength(0);
            builder.append('|');
            builder.append(" ".repeat(SCREEN_WIDTH - 2));
            builder.append('|');
            render[i] = builder.toString();
        }

        builder.setLength(0);
        builder.append('+');
        builder.append("-".repeat(SCREEN_WIDTH - 2));
        builder.append('+');
        render[SCREEN_HEIGHT - 1] = builder.toString();
    }

    private void renderScreenRow(
                String[] render,
                int height,
                ArrayList<UIElement> elementsAtHeight,
                StringBuilder builder) {
        builder.setLength(0);
        builder.append(render[height]);

        var xDrawPos = 1;

        for (var element : elementsAtHeight) {
            var difference = Helper.clamp(
                    element.pos.x - xDrawPos,
                    0,
                    SCREEN_WIDTH - 1 - xDrawPos
            );
            xDrawPos += difference;

            if (height - element.pos.y >= element.rendered.length) continue;
            var text = removeEndingSpaces(element.rendered[height - element.pos.y]);
            if (text.isEmpty()) continue;
            if (xDrawPos - element.pos.x >= text.length()) continue;

            var posPlusSpaces = element.pos.x + getBeginningSpaceNum(text);
            if (posPlusSpaces > xDrawPos) xDrawPos = posPlusSpaces;

            var elementStartDrawPos = xDrawPos - element.pos.x;
            var width = Helper.clamp(
                    text.length() - elementStartDrawPos,
                    0,
                    SCREEN_WIDTH - 1 - xDrawPos
            );

            builder.replace(
                    xDrawPos,
                    xDrawPos + width,
                    text.substring(elementStartDrawPos, elementStartDrawPos + width)
            );

            xDrawPos += width;
            if (xDrawPos >= SCREEN_WIDTH - 1) break;
        }

        render[height] = builder.toString();
    }

    private void moveCursorToTop() { System.out.print("\033[H"); }

    public static void setCursorPos(int row, int col) {
        System.out.println(String.format("\033[%d;%dH", row, col));
    }

    private ArrayList<UIElement> getElementsAtHeight(ArrayList<UIElement> elements, int height) {
        var elementsAtHeight = new ArrayList<UIElement>();
        var elementsToRemove = new ArrayList<UIElement>();

        for (var element : elements) {
            if (height < element.pos.y) continue;
            elementsAtHeight.add(element);
            if (height >= element.pos.y + element.rendered.length - 1) elementsToRemove.add(element);
        }

        for(var element : elementsToRemove) elements.remove(element);
        return elementsAtHeight;
    }

    private int getBeginningSpaceNum(String text) {
        for (var i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ') return i;
        }
        return text.length();
    }
    private String removeEndingSpaces(String text) {
        var builder = new StringBuilder(text);
        for (var i = builder.length() - 1 ; i >= 0; i--) {
            if (builder.charAt(i) == ' ') builder.deleteCharAt(i);
            else break;
        }
        return builder.toString();
    }

    static ArrayList<UIElement> sortElementsByXPos(ArrayList<UIElement> unsorted) {
        var unsortedArray = unsorted.toArray(new UIElement[unsorted.size()]);
        Helper.quickSort(
                unsortedArray,
                0,
                unsorted.size() - 1,
                (i, pivot) -> unsortedArray[i].pos.x < unsortedArray[pivot].pos.x
        );
        return new ArrayList<>(Arrays.asList(unsortedArray));
    }

    private void resetDungeonUI() { uiLayer = new ArrayList<>(Arrays.asList(dungeonCoreUI)); }

    public void transitionToAdjacentRoom(int direction) {
        var previousUIRoom = activeUIRoomGroup.getFirst();
        createNewActiveRoom(
                previousUIRoom.pos.add(offsetByDirection[direction]),
                (direction + 3) % 6
        );

        resetDungeonUI();
        gameLayer.clear();
        gameLayer.add(previousUIRoom);
        gameLayer.addAll(activeUIRoomGroup);

        var moveAmount = offsetByDirection[direction].divide(4);
        for (var i = 0; i < 3; i++) {
            previousUIRoom.pos = previousUIRoom.pos.subtract(moveAmount);
            for (var element : activeUIRoomGroup) element.pos = element.pos.subtract(moveAmount);

            if (playerElement != null) playerElement.setFrame((i % 2) + 1);

            renderScreen();
            Helper.sleep(transitionSpeed);
        }

        setActiveRoom();
        renderScreen();
    }

    public void transitionToDistantRoom() {
        resetDungeonUI();
        gameLayer.clear();
        var room = new UIRoomShrink(roomPos);
        gameLayer.add(room);

        for (var i = 0; i < 7; i++) {
            room.setStage(i);
            renderScreen();
            Helper.sleep(transitionSpeed);
        }

        setActiveRoom();
        renderScreen();
    }

    public void setActiveRoom() {
        resetDungeonUI();
        addTurnOptions();
        createNewActiveRoom(roomPos);

        gameLayer.clear();
        gameLayer.addAll(activeUIRoomGroup);

        gameForegroundLayer.clear();
        playerElement = new UIPlayer(new Point(SCREEN_WIDTH / 2 - 1, SCREEN_HEIGHT / 2 - 2));
        gameForegroundLayer.add(playerElement);
    }

    private void createNewActiveRoom(Point roomPos) { createNewActiveRoom(roomPos, -1); }

    private void createNewActiveRoom(Point roomPos, int exclude) {
        activeUIRoomGroup.clear();

        activeUIRoomGroup.add(new UIRoom(roomPos, GameManager.activeRoom));

        for (var i = 0; i < 6; i++) {
            if (i == exclude) continue;
            if (GameManager.activeRoom.connectedRooms[i]) {
                activeUIRoomGroup.add(new UIRoomEntry(roomPos.add(offsetByDirection[i]), i));
            }
        }
    }

    private void addTurnOptions() {
        uiLayer.add(new UITurnOptions(new Point(SCREEN_WIDTH - 20, SCREEN_HEIGHT - 6)));
    }

    public void changeToDirectionPrompt() {
        UIElement remove = null;
        for (var element : uiLayer) {
            if (element instanceof UITurnOptions) remove = element;
        }
        if (remove != null) uiLayer.remove(remove);
        uiLayer.add(new UIDirectionPrompt(new Point(1, SCREEN_HEIGHT - 4)));
    }
    public void addWarning(ArrayList<Warning> types) {
        for (var warning : warnings) uiLayer.remove(warning);
        warnings.clear();

        for (var i = 0; i < types.size(); i++) {
            warnings.add(new UIWarning(1 + i, types.get(i)));
        }
        uiLayer.addAll(warnings);
    }

    public UITrivia addTrivia(int questionNum, int threshold) {
        var trivia = new UITrivia(
                new Point(SCREEN_WIDTH / 2 - 24, SCREEN_HEIGHT / 2 - 4), questionNum, threshold);
        uiLayer.add(trivia);

        for (var i = 1; i <= 2; i++) {
            renderScreen();
            Helper.sleep(transitionSpeed);
            trivia.setState(i);
        }

        return trivia;
    }

    public void removeTrivia(UITrivia trivia) {
        for (var i = 1; i >= 0; i--) {
            trivia.setState(i);
            renderScreen();
            Helper.sleep(transitionSpeed);
        }

        uiLayer.remove(trivia);
    }

    public void arrowAnimation(int direction) {
        var arrow = new UIArrow(new Point(
                SCREEN_WIDTH / 2 + UIArrow.dirOffset[direction].x,
                SCREEN_HEIGHT / 2  + UIArrow.dirOffset[direction].y), direction);
        gameForegroundLayer.add(arrow);

        for (var i = 0; i <= 2; i++) {
            arrow.setStage(i);
            renderScreen();
            Helper.sleep(175);
        }

        gameForegroundLayer.remove(arrow);
    }

    public void setMainMenuUI() {
        gameForegroundLayer.clear();
        gameLayer.clear();
        gameLayer.add(new UITitle(new Point(SCREEN_WIDTH / 2 - 28, 1)));

        gameLayer.add(new UIMenuOption(new Point(2, SCREEN_HEIGHT - 8), "Play"));
        gameLayer.add(new UIMenuOption(new Point(2, SCREEN_HEIGHT - 6), "Top Scores"));
        gameLayer.add(new UIMenuOption(new Point(2, SCREEN_HEIGHT - 4), "Exit"));
    }

    @Deprecated
    public void oldRenderScreen() {
        var renderStartTime = System.currentTimeMillis();

        moveCursorToTop();

        var elements = new ArrayList<UIElement>();
        elements.addAll(uiLayer);
        elements.addAll(sortElementsByXPos(gameLayer));

        for (var element : elements) element.render();

        var render = new StringBuilder();

        render.append(renderHorizontalScreenBorder() + "\n");
        for (var i = 1; i < SCREEN_HEIGHT - 1; i++) {
            render.append(oldRenderScreenRow(i, getElementsAtHeight(elements, i)) + "\n");
        }
        render.append(renderHorizontalScreenBorder());

        System.out.println(render);
        clearTextBelowScreen();
        System.out.println("Render time ms: " + (System.currentTimeMillis() - renderStartTime));
    }

    @Deprecated
    private String renderHorizontalScreenBorder() {
        var builder = new StringBuilder();
        builder.append('+');
        for (var i = 0; i < SCREEN_WIDTH - 3; i++) builder.append('-');
        builder.append('+');
        return builder.toString();
    }

    @Deprecated
    private String oldRenderScreenRow(int height, ArrayList<UIElement> elementsAtHeight) {
        var builder = new StringBuilder();

        builder.append('|');
        var xDrawPos = 1;

        for (var element : elementsAtHeight) {
            var difference = Helper.clamp(
                    element.pos.x - xDrawPos,
                    0,
                    SCREEN_WIDTH - 2 - xDrawPos
            );
            for (var i = 0; i < difference; i++) builder.append(" ");
            xDrawPos += difference;

            if (height - element.pos.y >= element.rendered.length) continue;
            var text = removeEndingSpaces(element.rendered[height - element.pos.y]);
            if (text.length() == 0) continue;
            if (xDrawPos - element.pos.x >= text.length()) continue;

            var elementStartDrawPos = xDrawPos - element.pos.x;
            var width = Helper.clamp(
                    text.length() - elementStartDrawPos,
                    0,
                    SCREEN_WIDTH - 2 - xDrawPos
            );
            builder.append(text.substring(elementStartDrawPos, elementStartDrawPos + width));

            xDrawPos += width;

            if (xDrawPos >= SCREEN_WIDTH - 2) break;
        }

        if (xDrawPos < SCREEN_WIDTH - 2) {
            var difference = SCREEN_WIDTH - 2 - xDrawPos;
            for (var i = 0; i < difference; i++) builder.append(" ");
        }

        builder.append('|');

        return builder.toString();
    }
}
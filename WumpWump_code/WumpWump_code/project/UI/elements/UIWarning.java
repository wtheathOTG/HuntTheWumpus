package project.UI.elements;

import java.util.HashMap;

import project.UI.Point;
import project.UI.UIManager;

public class UIWarning extends UIElement {
    public HashMap<Warning, String> warningToText = new HashMap() {{
        put(Warning.Wumpus, "I smell a Wumpus!");
        put(Warning.Bat, "Bats Nearby");
        put(Warning.Pit, "I feel a draft");
    }};

    public UIWarning(int yPos, Warning type) {
        super(new Point(UIManager.SCREEN_WIDTH - 2, yPos));
        var text = warningToText.get(type);
        pos.x -= text.length();
        graphic = new String[] { text };
    }
}

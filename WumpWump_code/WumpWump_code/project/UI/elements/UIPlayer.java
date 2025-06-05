package project.UI.elements;

import project.UI.Point;

public class UIPlayer extends UIElement {
    private String[] frame0 = new String[] {
            "(:D)",
            " l|",
    };

    private String[] frame1 = new String[] {
            "(:D)",
            " |l",
    };

    private String[] currentFrame;

    public UIPlayer(Point pos) {
        super(pos);
        currentFrame = frame0;
    }

    @Override
    public void render() { rendered = currentFrame; }

    public void setFrame(int num) {
        currentFrame = switch (num) {
            case 0 -> frame0;
            case 1 -> frame1;
            case 2 -> frame0;
            default -> frame0;
        };
    }
}

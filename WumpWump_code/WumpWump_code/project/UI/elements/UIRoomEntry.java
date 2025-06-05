package project.UI.elements;

import project.UI.Point;

public class UIRoomEntry extends UIElement {

    private static String[] NE = new String[] {
            "",
            "",
            "",
            "",
            " /",
            " \\",
            "",
            "",
            "    \\",
            "     -.",
    };

    private static String[] N = new String[] {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "    \\             ./    ",
            "     ---        ---     ",
    };

    private static String[] NW = new String[] {
            "",
            "",
            "",
            "",
            "                      ; ",
            "                      / ",
            "",
            "",
            "                   /    ",
            "                .--     ",
    };

    private static String[] SW = new String[] {
            "                 ^-     ",
            "                   \\    ",
            "",
            "",
            "                      \\ ",
            "                      ] ",
            "",
            "",
            "",
            "",
    };

    private static String[] S = new String[] {
            "     ---        ---     ",
            "    ^              \\    ",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
    };

    private static String[] SE = new String[] {
            "     --^",
            "    /",
            "",
            "",
            " /",
            " [",
            "",
            "",
            "",
            "",
    };

    public UIRoomEntry(Point pos, int direction) {
        super(pos);
        graphic = switch (direction) {
            case 0 -> NE;
            case 1 -> N;
            case 2 -> NW;
            case 3 -> SW;
            case 4 -> S;
            case 5 -> SE;
            default -> new String[0];
        };
    }
}

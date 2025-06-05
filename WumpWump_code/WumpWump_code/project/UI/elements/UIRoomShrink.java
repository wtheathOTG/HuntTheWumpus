package project.UI.elements;

import project.UI.Point;

public class UIRoomShrink extends UIElement {
    private String[] full = new String[] {
            "     --------------     ",
            "    /              \\    ",
            "   /                \\   ",
            "  /                  \\  ",
            " /                    \\ ",
            " \\                    / ",
            "  \\                  /  ",
            "   \\                /   ",
            "    \\              /    ",
            "     --------------     "
    };

    private String[] ThreeQuarters = new String[] {
            "",
            "       ----------     ",
            "      /          \\    ",
            "     /            \\   ",
            "    /              \\  ",
            "    \\              /  ",
            "     \\            /   ",
            "      \\          /    ",
            "       ----------     ",
            ""
    };

    private String[] Half = new String[] {
            "",
            "",
            "        -------     ",
            "       /       \\    ",
            "      /         \\   ",
            "      \\         /   ",
            "       \\       /    ",
            "        -------     ",
            "",
            ""
    };

    private String[] Quarter = new String[] {
            "",
            "",
            "",
            "          ---     ",
            "         /   \\    ",
            "         \\   /    ",
            "          ---     ",
            "",
            "",
            ""
    };

    public UIRoomShrink(Point pos) {
        super(pos);
        setStage(0);
    }

    public void setStage(int stage) {
        graphic = switch (stage) {
            case 0 -> ThreeQuarters;
            case 1 -> Half;
            case 2 -> Quarter;
            case 3 -> new String[0];
            case 4 -> Quarter;
            case 5 -> Half;
            case 6 -> ThreeQuarters;
            default -> new String[0];
        };
    }
}

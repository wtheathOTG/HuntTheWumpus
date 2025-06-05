package project.UI.elements;

import project.UI.Point;

public class UITurnOptions extends UIElement {
    public UITurnOptions(Point pos) {
        super(pos);
        graphic = new String[] {
                "            Move(m)",
                "      View Map(map)",
                "     Shoot Arrow(s)",
                " Purchase Arrow(pa)",
                "Purchase Secret(ps)"
        };
    }
}

package project.UI.elements;

import project.GameManager;
import project.Helper;
import project.UI.Point;

public class UIDirectionPrompt extends UIElement {
    public UIDirectionPrompt(Point pos) {
        super(pos);
        graphic = new String[] { "Directions: " };
    }

    @Override
    public void render() { rendered = new String[]{ graphic[0] + connectedRoomsToString() }; }

    private String connectedRoomsToString() {
        var builder = new StringBuilder();
        var connections = GameManager.activeRoom.connectedRooms;
        var firstMove = true;

        for (var i = 0; i < connections.length; i++) {
            if (connections[i]) {
               if (firstMove) firstMove = false;
               else builder.append(", ");
               builder.append(Helper.DIRECTION_TO_NAME[i]);
            }
        }

        var text = builder.toString();
        return text;
    }
}

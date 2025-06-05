package project.UI.elements;

import project.CaveSystem.Room;
import project.UI.Point;

public class UIMapRoom extends UIElement {
    private Room room;
    private boolean current;
    public UIMapRoom(Point pos, Room room, boolean current) {
        super(pos);
        this.room = room;
        this.current = current;
        graphic = new String[] {
                " ---- ",
                "/    \\",
                "\\    /",
                " ---- "
        };
    }

    @Override
    public void render() {
        if (!room.discovered) {
            rendered = new String[] { "" };
            return;
        }

        var render = new String[4];
        render[0] = room.connectedRooms[1]
                ? " -  - " : " ---- ";

        render[1] = (room.connectedRooms[2] ? "," : "/")
                  +  (current ? " ## " : "    ")
                  + (room.connectedRooms[0] ? "," : "\\");

        render[2] = (room.connectedRooms[3] ? "," : "\\")
                  +  (current ? " ## " : "    ")
                  + (room.connectedRooms[5] ? "," : "/");

        render[3] = room.connectedRooms[4]
                ? " -  - " : " ---- ";

        rendered = render;
    }
}

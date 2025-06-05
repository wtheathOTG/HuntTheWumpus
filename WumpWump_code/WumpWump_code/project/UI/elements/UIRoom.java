package project.UI.elements;

import project.CaveSystem.Room;
import project.UI.Point;

public class UIRoom extends UIElement {
    private Room room;

    public UIRoom(Point pos, Room room) {
        super(pos);
        this.room = room;
        graphic = new String[] {
                "     --------------     ",
                "    /              \\    ",
                "   /                \\   ",
                "  /                  \\  ",
                " /                    \\ ",
                " \\                    / ",
                "  \\                  /  ",
                "   \\                /   ",
                "    \\              /    ",
                "     --------------     ",
        };
    }

    @Override
    public void render() {
        var render = new String[10];
        render[0] = room.connectedRooms[1]
                ? "     ---        ---     " : "     --------------     ";

        render[1] = "    /" + roomRowInterior(1) + "\\    ";

        render[2] = (room.connectedRooms[2] ? "    " : "   /")
                  + roomRowInterior(2) + (room.connectedRooms[0] ? "    " : "\\   ");

        render[3] = (room.connectedRooms[2] ? "   " : "  /")
                + roomRowInterior(3) + (room.connectedRooms[0] ? "   " : "\\  ");

        render[4] = " /" + roomRowInterior(4) + "\\ ";
        render[5] = " \\" + roomRowInterior(5) + "/ ";

        render[6] = (room.connectedRooms[3] ? "   " : "  \\")
                + roomRowInterior(6) + (room.connectedRooms[5] ? "   " : "/  ");

        render[7] = (room.connectedRooms[3] ? "    " : "   \\")
                + roomRowInterior(7) + (room.connectedRooms[5] ? "    " : "/   ");

        render[8] = "    \\" + roomRowInterior(8) + "/    ";
        render[9] = room.connectedRooms[4]
                ? "     ---        ---     " : "     --------------     ";

        rendered = render;
    }

    private String roomRowInterior(int row) {
        var rowText = room.getStalagmiteRow(row - 1);
        return rowText;

        /*var pitText = RoomItemGraphics.pit;
        var offset = RoomItemGraphics.pitOffset;
        if (!(row >= offset.y && row < offset.y + pitText.length)) return rowText;

        var pitRowText = pitText[row - offset.y];
        return rowText.substring(0, offset.x)
                + pitRowText
                + rowText.substring(offset.x + pitRowText.length());*/
    }
}
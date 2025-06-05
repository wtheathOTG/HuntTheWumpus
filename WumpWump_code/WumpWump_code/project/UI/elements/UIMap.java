package project.UI.elements;

import project.CaveSystem.CaveManager;
import project.CaveSystem.Room;
import project.GameManager;
import project.UI.Point;
import project.UI.UILayer;
import project.UI.UIManager;

public class UIMap {
    private Point[] directionToOffset = new Point[] {
            new Point(6, -2),
            new Point(0, -4),
            new Point(-6, -2),
            new Point(-6, 2),
            new Point(0, 4),
            new Point(6, 2)
    };

    public void generateMap(Room[] rooms) {
        var room = GameManager.activeRoom;
        var pos = new Point(UIManager.SCREEN_WIDTH / 2, UIManager.SCREEN_HEIGHT / 2)
                .subtract(new Point(3, 2));
        UIManager.instance.addElement(new UIMapRoom(pos, room, true), UILayer.Game);

        for (var i = 1; i <= 3; i++) {
            room = rooms[CaveManager.getConnectedRoomNum(room.roomNum, 1)];
            pos = pos.add(directionToOffset[1]);

            generateRoomRing(rooms, room, pos, i);
        }
    }

    public void generateRoomRing(Room[] rooms, Room room, Point pos, int radius) {
        for (var i = 5; i >= 0; i--) {
            for (var j = 0; j < radius; j++) {
                UIManager.instance.addElement(new UIMapRoom(pos, room, false), UILayer.Game);

                room = rooms[CaveManager.getConnectedRoomNum(room.roomNum, i)];
                pos = pos.add(directionToOffset[i]);
            }
        }
    }
}

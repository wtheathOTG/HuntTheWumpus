package project.UI.elements;

import project.CaveSystem.CaveManager;
import project.GameManager;
import project.UI.Point;

public class UIRoomNum extends UIElement {
    public UIRoomNum(Point pos) {
        super(pos);
        graphic = new String[] {"Room: "};
    }

    @Override
    public void render() {
        var roomNum = GameManager.activeRoom.roomNum;
        var text = (graphic[0] + (roomNum == 0 ? CaveManager.TOTAL_ROOMS : roomNum)).toString();
        rendered = new String[] {text};
    }
}

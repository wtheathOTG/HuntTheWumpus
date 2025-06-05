package project.CaveSystem.GenerationStrategies;

import project.CaveSystem.CaveManager;
import project.CaveSystem.Room;
import project.Helper;

public class Random2PassStrategy implements CaveConnectionStrategy {
    @Override
    public void execute(Room[] rooms, int totalRooms, int caveWidth, float probability) {
        for (var i = 0; i < totalRooms; i++) rooms[i] = new Room(i, false);

        for (var i = 0; i < totalRooms; i += (i % 2 == 0 ? caveWidth * 2 - 1 : caveWidth + 1)) {
            for (var j = 0; j < caveWidth; j += 2) setRoomConnections(rooms, (i + j) % totalRooms);
        }

        for (var i = 1; i < totalRooms; i += (i % 2 == 0 ? caveWidth * 2 - 1 : caveWidth + 1)) {
            for (var j = 0; j < caveWidth; j += 2) {
                addRoomConnections(rooms, (i + j) % totalRooms, probability);
            }
        }
    }

    private void setRoomConnections(Room[] rooms, int roomNum) {
        var connections = CaveManager.generateRandomConnectedRooms();
        var room = rooms[roomNum];

        room.connectedRooms = connections;

        for (var i = 0; i < connections.length; i++) {
            if (!connections[i]) continue;
            rooms[CaveManager.getConnectedRoomNum(roomNum, i)].connectedRooms[(i + 3) % 6] = true;
        }
    }

    private void addRoomConnections(Room[] rooms, int roomNum, float probability) {
        var room = rooms[roomNum];
        for (var i = 0; i < room.connectedRooms.length; i++) {
            if (room.connectedRooms[i]) continue;

            var isNewConnection = Helper.probabilityTest(probability);
            if (!isNewConnection) continue;

            room.connectedRooms[i] = true;
            rooms[CaveManager.getConnectedRoomNum(roomNum, i)].connectedRooms[(i + 3) % 6] = true;
        }
    }
}

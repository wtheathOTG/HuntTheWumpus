package project.CaveSystem.GenerationStrategies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import project.CaveSystem.CaveManager;
import project.CaveSystem.Room;
import project.GameManager;
import project.UI.UIManager;

public class ThreeConnectionsStrategy implements CaveConnectionStrategy {
    private Room[] _rooms;
    @Override
    public void execute(Room[] rooms, int totalRooms, int caveWidth, float probability) {
        _rooms = rooms;
        do {
            for (var i = 0; i < totalRooms; i++) _rooms[i] = new Room(i, true);

            var allRooms1To3Connections = false;
            while (!allRooms1To3Connections) {
                for (var room : _rooms) removeExcessConnections(room);
                allRooms1To3Connections = true;
                for (var room : _rooms) {
                    var connectionNum = getConnections(room).size();
                    if (connectionNum == 0 || connectionNum > 3) allRooms1To3Connections = false;
                }
            }
        }
        while (!checkAllRoomsConnected());
        rooms = _rooms;
    }

    private void removeExcessConnections(Room room) {
        var removeableDirections = getConnections(room);
        var removableNum = removeableDirections.size();

        for (var i = 0; i < removableNum - 3; i++) {
            var directionToRemove = removeableDirections.get(GameManager.rand.nextInt(removableNum));

            room.connectedRooms[directionToRemove] = false;

            var connectedRoom = _rooms[CaveManager.getConnectedRoomNum(room.roomNum, directionToRemove)];
            connectedRoom.connectedRooms[(directionToRemove + 3) % 6] = false;
            addConnectionsIfZero(connectedRoom);
        }
    }

    private void addConnectionsIfZero(Room room) {
        if (getConnections(room).size() != 0) return;

        var direction = GameManager.rand.nextInt(6);

        room.connectedRooms[direction] = true;
        var connectedRoom = _rooms[CaveManager.getConnectedRoomNum(room.roomNum, direction)];
        connectedRoom.connectedRooms[(direction + 3) % 6] = true;

        removeExcessConnections(connectedRoom);
    }

    private boolean checkAllRoomsConnected() {
        var visitedRooms = new HashSet<Integer>();
        followRoomConnections(_rooms[1], visitedRooms);
        return visitedRooms.size() == CaveManager.TOTAL_ROOMS;
    }

    private void followRoomConnections(Room room, Set<Integer> visitedRooms) {
        visitedRooms.add(room.roomNum);
        for (var i = 0; i < room.connectedRooms.length; i++) {
            var connection = room.connectedRooms[i];
            if (!connection) continue;

            var connectedRoomNum = CaveManager.getConnectedRoomNum(room.roomNum, i);
            if (visitedRooms.contains(connectedRoomNum)) continue;

            followRoomConnections(_rooms[connectedRoomNum], visitedRooms);
        }
    }

    private ArrayList<Integer> getConnections(Room room) {
        var connections = new ArrayList<Integer>();
        for (var i = 0; i < 6; i++) {
            if (room.connectedRooms[i]) connections.add(i);
        }
        return connections;
    }
}

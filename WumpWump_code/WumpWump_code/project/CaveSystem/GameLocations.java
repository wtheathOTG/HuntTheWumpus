package project.CaveSystem;

import project.GameManager;
import project.UI.elements.Warning;

import java.util.ArrayList;

public class GameLocations {
    public static Wumpus wumpus;
    public static int pit1Room;
    public static int pit2Room;
    public static int bat1Room;
    public static int bat2Room;


    public static void randomizeLocations() {
        var availableRooms = new ArrayList<Integer>();
        availableRooms.add(0);
        for (var i = 2; i < CaveManager.TOTAL_ROOMS; i++) availableRooms.add(i);

        wumpus = new Wumpus(availableRooms.get(GameManager.rand.nextInt(availableRooms.size())));

        pit1Room = getRandomAvailableRoom(availableRooms);
        pit2Room = getRandomAvailableRoom(availableRooms);
        bat1Room = getRandomAvailableRoom(availableRooms);
        bat2Room = getRandomAvailableRoom(availableRooms);
    }

    private static int getRandomAvailableRoom(ArrayList<Integer> availableRooms) {
        var roomNum = availableRooms.get(GameManager.rand.nextInt(availableRooms.size()));
        availableRooms.remove(roomNum);
        return roomNum;
    }

    public static boolean checkWumpus(int roomNum) { return wumpus.roomNum == roomNum; }
    public static boolean checkPit(int roomNum) { return pit1Room == roomNum || pit2Room == roomNum; }
    public static boolean checkBat(int roomNum) {
        if (bat1Room == roomNum || bat2Room == roomNum) {
            var availableRooms = new ArrayList<Integer>();
            for (var i = 0; i < CaveManager.TOTAL_ROOMS; i++) availableRooms.add(i);
            availableRooms.remove((Integer)pit1Room);
            availableRooms.remove((Integer)pit2Room);

            if (bat1Room == roomNum) {
                availableRooms.remove((Integer)bat2Room);
                bat1Room = getRandomAvailableRoom(availableRooms);
            }
            else {
                availableRooms.remove((Integer)bat1Room);
                bat2Room = getRandomAvailableRoom(availableRooms);
            }

            return true;
        }
        return false;
    }

    public static ArrayList<Warning> getAdjacentHazards(Room room) {
        var warnings = new ArrayList<Warning>();

        for (var i = 0; i < room.connectedRooms.length; i++) {
            if (!room.connectedRooms[i]) continue;

            var adjacentRoomNum = CaveManager.getConnectedRoomNum(GameManager.activeRoom.roomNum, i);

            if (wumpus.roomNum == adjacentRoomNum) warnings.add(Warning.Wumpus);
            if (bat1Room == adjacentRoomNum || bat2Room == adjacentRoomNum) warnings.add(Warning.Bat);
            else if (pit1Room == adjacentRoomNum || pit2Room == adjacentRoomNum) warnings.add(Warning.Pit);
        }

        return warnings;
    }

    public static boolean isWumpusWithin2Rooms() {
        return searchConnectedRoomsForWumpus(GameManager.activeRoom, 2, -1);
    }

    private static boolean searchConnectedRoomsForWumpus(Room room, int depth, int ignoreDirection) {
        if (depth <= 0) return false;
        depth--;

        for (var i = 0; i < room.connectedRooms.length; i++) {
            if (!room.connectedRooms[i] || i == ignoreDirection) continue;

            var connectedRoomNum = CaveManager.getConnectedRoomNum(room.roomNum, i);
            if (wumpus.roomNum == connectedRoomNum) return true;
            if (searchConnectedRoomsForWumpus(
                    CaveManager.rooms[connectedRoomNum], depth, (i + 3) % 6)) {
                return true;
            }
        }
        return false;
    }

    public static void printLocations() {
        System.out.println(
                "Pit1: "+pit1Room
                +" Pit2: "+pit2Room
                +" Bat1: "+bat1Room
                +" Bat2: "+bat2Room
                +" Wumpus: "+wumpus.roomNum
        );
    }
}

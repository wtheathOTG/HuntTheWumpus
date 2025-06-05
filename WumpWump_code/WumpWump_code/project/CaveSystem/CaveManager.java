package project.CaveSystem;

import project.CaveSystem.GenerationStrategies.*;
import project.GameManager;
import project.Helper;

public class CaveManager {
   public static final int TOTAL_ROOMS = 30;
   private static final int CAVE_WIDTH = 6;

   private static final float ROOM_CONNECTED_PROBABILITY = 0.5f;

   public static Room[] rooms = new Room[TOTAL_ROOMS];

   private CaveConnectionStrategy strategy = new ThreeConnectionsStrategy();

   public CaveManager() {
      strategy.execute(rooms, TOTAL_ROOMS, CAVE_WIDTH, ROOM_CONNECTED_PROBABILITY);
   }

   public static boolean[] generateRandomConnectedRooms() {
      var connections = new boolean[] {
              Helper.probabilityTest(ROOM_CONNECTED_PROBABILITY),
              Helper.probabilityTest(ROOM_CONNECTED_PROBABILITY),
              Helper.probabilityTest(ROOM_CONNECTED_PROBABILITY),
              Helper.probabilityTest(ROOM_CONNECTED_PROBABILITY),
              Helper.probabilityTest(ROOM_CONNECTED_PROBABILITY),
              Helper.probabilityTest(ROOM_CONNECTED_PROBABILITY)
      };

      var noConnections = true;
      for (var connection : connections) {
         if (connection) {
            noConnections = false;
            break;
         }
      }
      if (noConnections) connections[GameManager.rand.nextInt(connections.length)] = true;

      return connections;
   }

   //exception if direction is out of range
   public static int getConnectedRoomNum(int roomNum, int direction) {
      int current = roomNum + TOTAL_ROOMS;

      return (roomNum % 2 == 0
              ? switch (direction) {
         case 0 -> roomNum % CAVE_WIDTH == 0 ? current - 5 : current + 1;
         case 1 -> (current - 6);
         case 2 -> (current - 1);
         case 3 -> (current + 5);
         case 4 -> (current + 6);
         case 5 -> roomNum % CAVE_WIDTH == 0 ? current + 1 : current + 7;
         default -> -1;
      }
              : switch (direction) {
         case 0 -> (current - 5);
         case 1 -> (current - 6);
         case 2 -> roomNum % CAVE_WIDTH == 1 ? current - 1 : current - 7;
         case 3 -> roomNum % CAVE_WIDTH == 1 ? current + 5 : current - 1;
         case 4 -> (current + 6);
         case 5 -> (current + 1);
         default -> -1;
      }) % TOTAL_ROOMS;
   }
}
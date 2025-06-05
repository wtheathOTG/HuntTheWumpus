package project.CaveSystem;

import project.GameManager;
import project.Helper;

import java.util.ArrayList;

public class Wumpus {
   public int roomNum;
   private boolean isAwake;
   private int turns;
   private int stateChangeTurnNum;
   private int movesPerTurn = 1;
   private boolean canRunIntoPlayer;
   private int cantMoveDirection = -1;

   public Wumpus(int roomNum) {
      this.roomNum = roomNum;
      setAwake(false);
   }
   
   public void turn() {
      turns++;

      tryTeleportToRandomRoom();

      if (isAwake) {
         cantMoveDirection = -1;
         for (var i = 0; i < movesPerTurn; i++) move();
      }

      if (turns >= stateChangeTurnNum) setAwake(!isAwake);
   }

   public void defeatedInTrivia() {
      setAwake(true);
      movesPerTurn = 2;
      canRunIntoPlayer = false;
   }

   public void setAwake(boolean isAwake) {
      this.isAwake = isAwake;
      turns = 0;
      movesPerTurn = 1;
      canRunIntoPlayer = true;
      stateChangeTurnNum = (isAwake
              ? GameManager.rand.nextInt(3) + 1
              : GameManager.rand.nextInt(6) + 5
      );
   }

   private void tryTeleportToRandomRoom() {
      if (0 == GameManager.rand.nextInt(20)) {
         roomNum = GameManager.rand.nextInt(CaveManager.TOTAL_ROOMS);
      }
   }

   private void move() {
      var connections = CaveManager.rooms[roomNum].connectedRooms;

      var directions = new ArrayList<Integer>();
      for (var i = 0; i < connections.length; i++) {
         if (connections[i] && i != cantMoveDirection) directions.add(i);
      }

      if (directions.isEmpty()) return;

      var randomDirection = directions.get(GameManager.rand.nextInt(directions.size()));
      var movingToNum = CaveManager.getConnectedRoomNum(roomNum, randomDirection);

      if (!canRunIntoPlayer && movingToNum == GameManager.activeRoom.roomNum) {
         for (var direction : directions) {
            movingToNum = CaveManager.getConnectedRoomNum(roomNum, direction);
            if (movingToNum != GameManager.activeRoom.roomNum) roomNum = movingToNum;
         }
         return;
      }

      roomNum = movingToNum;
      cantMoveDirection = (randomDirection + 3) % 6;
   }
}


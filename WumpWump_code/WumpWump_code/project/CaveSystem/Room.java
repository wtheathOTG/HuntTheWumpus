package project.CaveSystem;

import project.Helper;

public class Room {
   public int roomNum;
   public boolean[] connectedRooms;

   public static final float STALAGMITE_PROBABILITY = 0.085f;
   private String[] stalagmites;

   public boolean discovered = false;

   public Room(int roomNum, boolean startOpen) {
      if (roomNum == 1) discovered = true;
      this.roomNum = roomNum;
      connectedRooms = startOpen
              ? new boolean[] {true, true, true, true, true, true}
              : new boolean[] {false, false, false, false, false, false};
      generateStalagmites();
   }

   public String getStalagmiteRow(int row) {
      return stalagmites[row];
   }

   private void generateStalagmites() {
      var roomWidthAtIndex = new int[] {14, 16, 18, 20, 20, 18, 16, 14};
      var xBorder = 2;
      var yBorder = 1;
      stalagmites = new String[roomWidthAtIndex.length];

      var builder = new StringBuilder();
      var height = roomWidthAtIndex.length;
      for (var i = 0; i < height; i++) {
         var width = roomWidthAtIndex[i];
         for (var j = 0; j < width; j++) {
            if ((j < xBorder || j > width - 1 - xBorder) || (i < yBorder || i > height - 1 - yBorder)) {
               builder.append(Helper.probabilityTest(STALAGMITE_PROBABILITY) ? '^' : ' ');
            }
            else builder.append(' ');
         }
         stalagmites[i] = builder.toString();
         builder = new StringBuilder();
      }
   }
}
package project.UI;

public class Point {
   public int x;
   public int y;
   
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public Point add(Point other) { return new Point(this.x + other.x, this.y + other.y); }

   public Point subtract(Point other) { return new Point(this.x - other.x, this.y - other.y); }

   public Point divide(int num) { return new Point(x / num, y / num); }

   public Point multiply(int num) { return new Point(x * num, y * num); }
}
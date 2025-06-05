package project.UI.elements;

import project.UI.Point;

public abstract class UIElement {
   //position based on top left corner
   public Point pos;
   
   protected String[] graphic;

   public String[] rendered;

   public UIElement(Point pos) { this.pos = pos; }

   public void render() {
      rendered = graphic;
   }
}
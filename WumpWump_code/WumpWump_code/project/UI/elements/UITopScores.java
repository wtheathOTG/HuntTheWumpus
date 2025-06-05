package project.UI.elements;

import project.GameManager;
import project.UI.Point;

public class UITopScores extends UIElement{
    public UITopScores(Point pos) {
        super(pos);
    }

    @Override
    public void render() {
        rendered = GameManager.scoreMan.getTopScoresText();
    }
}
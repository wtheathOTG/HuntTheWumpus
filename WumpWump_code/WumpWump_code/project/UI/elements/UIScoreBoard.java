package project.UI.elements;

import project.ScoreManager;
import project.UI.Point;

public class UIScoreBoard extends UIElement {
    public UIScoreBoard(Point pos) {
        super(pos);
        graphic = new String[] {
            " Score:        |",
            "---------------+"
        };
    }

    @Override
    public void render() {
        var render = new String[2];
        var builder = new StringBuilder();

        var scoreText = String.valueOf(ScoreManager.score.getScore());
        builder.append(" Score: " + scoreText);
        for (var i = 7; i > scoreText.length(); i--) builder.append(' ');
        builder.append('|');

        render[0] = builder.toString();
        render[1] = graphic[1];
        rendered = render;
    }
}
package project.UI.elements;

import project.ScoreManager;
import project.UI.Point;

public class UIInventory extends UIElement{
    public UIInventory(Point pos) {
        super(pos);
        graphic = new String[] {
                "---------------+----------------+",
                " Coins:        | Arrows:        |"
        };
    }

    @Override
    public void render() {
        var render = new String[2];
        render[0] = graphic[0];

        var builder = new StringBuilder();

        var coinText = String.valueOf(ScoreManager.score.goldCoins);
        builder.append(" Coins: " + coinText);
        for (var i = 7; i > coinText.length(); i--) builder.append(' ');
        builder.append('|');

        var arrowsText = String.valueOf(ScoreManager.score.arrows);
        builder.append(" Arrows: " + arrowsText);
        for (var i = 7; i > arrowsText.length(); i--) builder.append(' ');
        builder.append('|');

        render[1] = builder.toString();
        rendered = render;
    }
}
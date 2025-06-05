package project.UI.elements;

import project.UI.Point;

public class UIMenuOption extends UIElement {
    private int buttonInteriorWidth = 18;
    private String text;

    public UIMenuOption(Point pos, String text) {
        super(pos);
        this.text = text;
        graphic = new String[] {
                "+------------------+",
                "|                  |",
                "+------------------+"
        };
    }

    @Override
    public void render() {
        var render = new String[3];

        render[0] = graphic[0];

        var builder = new StringBuilder();
        var spaceNum = buttonInteriorWidth - text.length();

        builder.append('|');
        var halfSpaces = spaceNum / 2;
        for (var i = 0; i < halfSpaces; i++) builder.append(' ');
        builder.append(text);
        for (var i = 0; i < spaceNum - halfSpaces; i++) builder.append(' ');
        builder.append('|');
        render[1] = builder.toString();

        render[2] = graphic[2];

        rendered = render;
    }
}

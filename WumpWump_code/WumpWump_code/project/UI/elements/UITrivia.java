package project.UI.elements;

import project.UI.Point;

public class UITrivia extends UIElement {
    private String question;
    private int questionNum;
    private int threshold;
    private int state = 0;

    private String[] quarter = new String[] {
            "",
            "",
            "             +-------------------+",
            "             |                   |",
            "             |                   |",
            "             +-------------------+",
            "",
            ""
    };
    private String[] threeQuarter = new String[] {
            "",
            "      +---------------------------------+",
            "      |                                 |",
            "      |                                 |",
            "      |                                 |",
            "      |                                 |",
            "      +---------------------------------+",
            ""
    };
    private String[] full = new String[] {
            "+----------------------------------------------+",
            "|                                              |",
            "|                                              |",
            "|                                              |",
            "|                                              |",
            "|                                              |",
            "|                                              |",
            "+----------------------------------------------+"
    };

    public UITrivia(Point pos, int questionNum, int threshold) {
        super(pos);
        this.questionNum = questionNum;
        this.threshold = threshold;
        graphic = new String[] {
                "+----------------------------------------------+",
                "|          Answer 2 out of 3 questions         |",
                "|                                              |",
                "|                   Yes or no?                 |",
                "|                                              |",
                "|                                              |",
                "|           Yes                    No          |",
                "+----------------------------------------------+"
        };
    }

    public void setQuestion(String question) { this.question = question; }

    public void setState(int state) { this.state = state;}
    @Override
    public void render() {
        switch (state) {
            case 0: rendered = quarter; return;
            case 1: rendered = threeQuarter; return;
            case 2:
                var render = new String[8];

                var interiorWidth = graphic[0].length() - 2;

                render[0] = graphic[0];
                render[1] = renderQuestionRow(
                        "Answer "+threshold+" out of "+questionNum+" questions correct",
                        interiorWidth
                );
                render[2] = graphic[2];

                var maxTextWidth = interiorWidth - 2;

                var lineText = question.length() > interiorWidth - 4
                        ? question.substring(0, maxTextWidth)
                        : question;
                render[3] = renderQuestionRow(lineText, interiorWidth);

                lineText = question.length() > interiorWidth - 4
                        ? question.substring(maxTextWidth, 2 * (maxTextWidth))
                        : "";
                render[4] = renderQuestionRow(lineText, interiorWidth);

                render[5] = graphic[5];
                render[6] = graphic[6];
                render[7] = graphic[7];

                rendered = render;
                break;
        }
    }

    private String renderQuestionRow(String lineText, int width) {
        var spaceNum = width - lineText.length();

        var builder = new StringBuilder();
        builder.append('|');
        var halfSpaces = spaceNum / 2;
        for (var i = 0; i < halfSpaces; i++) builder.append(' ');
        builder.append(lineText);
        for (var i = 0; i < spaceNum - halfSpaces; i++) builder.append(' ');
        builder.append('|');

        return builder.toString();
    }
}

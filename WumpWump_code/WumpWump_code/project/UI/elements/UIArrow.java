package project.UI.elements;

import project.UI.Point;

public class UIArrow extends UIElement {
    public static final Point[] dirOffset = new Point[] {
        new Point(4, -4),
        new Point(0, -8),
        new Point(-11, -4),
        new Point(-11, 0),
        new Point(0, 1),
        new Point(4, 0)
    };

    private final String[][] dir0 = new String[][] {
            new String[] {
                    "",
                    "",
                    "/",
            },
            new String[] {
                    "",
                    "    /",
                    ""
            },
            new String[] {
                    "        /",
                    "",
                    ""
            }
    };

    private final String[][] dir1 = new String[][] {
            new String[] {
                    "",
                    "",
                    "",
                    "",
                    "|",
            },
            new String[] {
                    "",
                    "",
                    "|",
                    "",
                    ""
            },
            new String[] {
                    "|",
                    "",
                    "",
                    "",
                    ""
            }
    };

    private final String[][] dir2 = new String[][] {
            new String[] {
                    "",
                    "",
                    "        \\",
            },
            new String[] {
                    "",
                    "    \\",
                    ""
            },
            new String[] {
                    "\\",
                    "",
                    ""
            }
    };

    private final String[][] dir3 = new String[][] {
            new String[] {
                    "        /",
                    "",
                    "",
            },
            new String[] {
                    "",
                    "    /",
                    ""
            },
            new String[] {
                    "",
                    "",
                    "/"
            }
    };

    private final String[][] dir4 = new String[][] {
            new String[] {
                    "|",
                    "",
                    "",
                    "",
                    ""
            },
            new String[] {
                    "",
                    "",
                    "|",
                    "",
                    ""
            },
            new String[] {
                    "",
                    "",
                    "",
                    "",
                    "|"
            }
    };

    private final String[][] dir5 = new String[][] {
            new String[] {
                    "\\",
                    "",
                    "",
            },
            new String[] {
                    "",
                    "     \\",
                    ""
            },
            new String[] {
                    "",
                    "",
                    "        \\"
            }
    };

    private String[][][] dirAnimations;
    private int direction;

    public UIArrow(Point pos, int direction) {
        super(pos);
        this.direction = direction;
        dirAnimations = new String[][][] { dir0, dir1, dir2, dir3, dir4, dir5 };
        setStage(0);
    }

    public void setStage(int stage) {
        graphic = dirAnimations[direction][stage];
    }
}

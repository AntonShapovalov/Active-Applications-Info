package ru.org.adons.slog.item.grid;

public class VerticalTimeGrid extends AbstractTimeGrid {

    private final static int ROW_COUNT = 24;
    private final static int COL_COUNT = 4;

    public VerticalTimeGrid(int w, int h) {
        /* 4 x 24 grid */
        super(w, h, ROW_COUNT, COL_COUNT);
        super.setIndexMap(ROW_COUNT, COL_COUNT);
    }

}

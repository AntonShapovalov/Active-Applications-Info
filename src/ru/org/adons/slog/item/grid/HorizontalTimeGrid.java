package ru.org.adons.slog.item.grid;

public class HorizontalTimeGrid extends AbstractTimeGrid {

	private final static int ROW_COUNT = 12;
	private final static int COL_COUNT = 8;

	public HorizontalTimeGrid(int w, int h) {
		/* 8 x 12 grid */
		super(w, h, ROW_COUNT, COL_COUNT);
		setIndexMap(ROW_COUNT, COL_COUNT);
	}

	@Override
	protected void setIndexMap(int rowCount, int colCount) {
		int k;
		int k0;
		String key;
		int[] value;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				k = (j + 1) / 5 + i * 2 + 1;
				k0 = j % 4 + 1;
				key = String.valueOf(k) + String.valueOf(k0);
				value = new int[] { i, j };
				index.put(key, value);
			}
		}
	}

}

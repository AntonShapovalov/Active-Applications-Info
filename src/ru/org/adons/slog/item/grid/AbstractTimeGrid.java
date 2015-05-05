package ru.org.adons.slog.item.grid;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;

public abstract class AbstractTimeGrid implements TimeGrid {

	private final List<Rect[]> cells = new ArrayList<Rect[]>();
	private Rect[] row;
	private Rect cell;
	private int left;
	private int top;
	private int right;
	private int bottom;
	private int wPad;
	private int hPad;

	public AbstractTimeGrid(int w, int h, int rowCount, int colCount) {
		w = w - colCount;
		wPad = (w % colCount) == 0 ? colCount / 2 : (w % 4) / 2;
		hPad = (h % rowCount) / 2;
		w = w / colCount;
		h = h / rowCount;
		for (int i = 0; i < rowCount; i++) {
			row = new Rect[colCount];
			for (int j = 0; j < colCount; j++) {
				left = j * w + wPad;
				top = i * h + hPad;
				right = (j + 1) * w + wPad;
				bottom = (i + 1) * h + hPad;
				cell = new Rect(left, top, right, bottom);
				row[j] = cell;
			}
			cells.add(row);
		}
	}

	@Override
	public List<Rect[]> getCells() {
		return cells;
	}

}

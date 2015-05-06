package ru.org.adons.slog.item.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Rect;

public abstract class AbstractTimeGrid implements TimeGrid {

	private final List<Rect[]> cells = new ArrayList<Rect[]>();
	protected final Map<String, int[]> index = new HashMap<String, int[]>();

	public AbstractTimeGrid(int w, int h, int rowCount, int colCount) {

		// set width and height for each cell
		w = w - colCount;
		int wPad = (w % colCount) == 0 ? colCount / 2 : (w % 4) / 2;
		int hPad = (h % rowCount) / 2;
		w = w / colCount;
		h = h / rowCount;

		// create all cells
		Rect[] row;
		Rect cell;
		int left;
		int top;
		int right;
		int bottom;
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

	protected void setIndexMap(int rowCount, int colCount) {
		int k;
		int k0;
		String key;
		int[] value;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				k = i + 1;
				k0 = j + 1;
				key = String.valueOf(k) + String.valueOf(k0);
				value = new int[] { i, j };
				index.put(key, value);
			}
		}
	}

	@Override
	public List<Rect[]> getCells() {
		return cells;
	}

	@Override
	public Map<String, int[]> getIndex() {
		return index;
	}

}

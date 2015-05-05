package ru.org.adons.slog.item.bar;

import android.graphics.Rect;

public class HorizontalBar extends AbstractBar {

	protected final static int LEGEND_SPACE = 36;
	private final static int LENGTH_PADDING = 4;

	public HorizontalBar(int index, int w, int h, int tSize) {
		this.index = index;
		length = w;
		thick = h / 24;
		thickPadding = (h % 24) / 2;
		// legend is centered by X and above Y in onDraw() with Align.CENTER
		// so there need set X = "in center of legend" and Y = "below text"
		legendX = LEGEND_SPACE / 2;
		legendY = (index + 1) * thick + thickPadding - (thick - tSize) / 2;
	}

	@Override
	public void setRectangle() {
		if (count > 0) {
			final int left = LEGEND_SPACE;
			final int top = index * thick + thickPadding;
			final int right = length * count / MAX_COUNT - LENGTH_PADDING;
			final int bottom = (index + 1) * thick + thickPadding;
			final Rect r = new Rect(left, top, right, bottom);
			rectangle = r;
		} else {
			final Rect r = new Rect();
			rectangle = r;
		}
	}

}

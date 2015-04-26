package ru.org.adons.slog.item;

import android.graphics.Rect;

public class VerticalBar extends AbstractBar {

	private final static int LEGEND_SPACE = 28;
	private final static int LENGTH_PADDING = 4;

	public VerticalBar(int index, int w, int h, int tSize) {
		this.index = index;
		length = h;
		thick = w / 24;
		thickPadding = (w % 24) / 2;
		// legend is centered by X and above Y in onDraw() with Align.CENTER
		// so there need set X = "in center of thick" and Y = "below text"
		legendX = index * thick + thickPadding + thick / 2;
		legendY = length - (LEGEND_SPACE - tSize) / 2;
	}

	@Override
	public void setRectangle() {
		if (count > 0) {
			final int left = index * thick + thickPadding;
			final int top = LENGTH_PADDING + length - length * count / MAX_COUNT;
			final int right = (index + 1) * thick + thickPadding;
			final int bottom = length - LEGEND_SPACE;
			final Rect r = new Rect(left, top, right, bottom);
			rectangle = r;
		} else {
			final Rect r = new Rect();
			rectangle = r;
		}
	}

}

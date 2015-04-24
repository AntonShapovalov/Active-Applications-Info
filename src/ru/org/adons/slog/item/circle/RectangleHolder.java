package ru.org.adons.slog.item.circle;

import android.graphics.Rect;

public class RectangleHolder {
	private final Rect[] rectangles = new Rect[12];
	private final int cx; // the X coordinate of circle's center
	private final int cy; // the Y coordinate of circle's center
	private final int r1; // internal circle radius
	private final int r2; // slice radius (r1+r2 = external radius)

	public RectangleHolder(int centerX, int centerY, int internalR, int sliceR) {
		this.cx = centerX;
		this.cy = centerY;
		this.r1 = internalR;
		this.r2 = sliceR;
	}

	public Rect[] getRectangles() {
		// three main rectangles size, x = width, y = height
		// calculated by pencil :)
		final int x1 = (int) Math.round(Math.sqrt(3) * r1 / 3);
		final int y1 = r2;
		final int x3 = (int) (Math.round(r1 * (1 - Math.sqrt(3) / 2) + r2));
		final int y3 = Math.round(r1 / 2);
		final int x2 = (int) Math.round((Math.sqrt(3) * (r1 + r2) - 2 * x1) / 2);
		final int y2 = (int) Math.round((Math.sqrt(3) * (r1 + r2) - 2 * y3) / 2);
		// 1-t rectangle
		int left = cx;
		int right = cx + x1;
		int top = cy - r1 - y1;
		int bottom = cy - r1;
		rectangles[0] = new Rect(left, top, right, bottom);
		// 2-t rectangle
		left = cx + x1;
		right = cx + x1 + x2;
		top = cy - y2 - y3;
		bottom = cy - y3;
		rectangles[1] = new Rect(left, top, right, bottom);
		// 3-d rectangle
		left = cx + r1 + r2 - x3;
		right = cx + r1 + r2;
		top = cy - y3;
		bottom = cy;
		rectangles[2] = new Rect(left, top, right, bottom);
		return rectangles;
	}
}

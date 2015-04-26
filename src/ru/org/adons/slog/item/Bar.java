package ru.org.adons.slog.item;

import android.graphics.Rect;

public interface Bar {

	public int getLegendX();

	public int getLegendY();

	public void incrCount();

	public void setRectangle();

	public Rect getRectangle();

}

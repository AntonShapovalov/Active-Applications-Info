package ru.org.adons.slog.item.grid;

import java.util.List;
import java.util.Map;

import android.graphics.Rect;

public interface TimeGrid {

    public List<Rect[]> getCells();

    public Map<String, int[]> getIndex();

}

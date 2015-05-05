package ru.org.adons.slog.item;

import ru.org.adons.slog.LogItem;
import ru.org.adons.slog.R;
import ru.org.adons.slog.item.grid.HorizontalTimeGrid;
import ru.org.adons.slog.item.grid.TimeGrid;
import ru.org.adons.slog.item.grid.VerticalTimeGrid;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ChartViewGrid extends View {

	private LogItem item;
	private TimeGrid grid;
	private final Paint gridPaint = new Paint();

	public ChartViewGrid(Context context, LogItem item) {
		super(context);
		this.item = item;
	}

	public ChartViewGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// Initialize grid
		if (w > h) {
			grid = new HorizontalTimeGrid(w, h);
		} else {
			grid = new VerticalTimeGrid(w, h);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect[] row;

		// draw entire of cell
		gridPaint.setStyle(Style.FILL);
		for (int i = 0; i < grid.getCells().size(); i++) {
			row = grid.getCells().get(i);
			for (int j = 0; j < row.length; j++) {
				if (i == 3 && j == 2) {
					gridPaint.setColor(getResources().getColor(R.color.blue10));
				} else {
					gridPaint.setColor(getResources().getColor(R.color.blue14));
				}
				canvas.drawRect(row[j], gridPaint);
			}
		}

		// draw border of cell
		gridPaint.setStyle(Style.STROKE);
		gridPaint.setColor(getResources().getColor(R.color.blue01));
		for (int i = 0; i < grid.getCells().size(); i++) {
			row = grid.getCells().get(i);
			for (int j = 0; j < row.length; j++) {
				canvas.drawRect(row[j], gridPaint);
			}
		}

		invalidate();
	}
}

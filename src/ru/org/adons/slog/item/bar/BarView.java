package ru.org.adons.slog.item.bar;

import ru.org.adons.slog.LogItem;
import ru.org.adons.slog.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarView extends View {

	private LogItem item;
	private final Bar[] bars = new Bar[24];
	private final Paint barPaint = new Paint();
	private final static int LEGEND_TEXT_SIZE = 12;

	public static enum BarType {
		VERTICAL, HORIZONTAL
	}

	private BarType barType;

	public BarView(Context context, LogItem item) {
		super(context);
		this.item = item;
	}

	public BarView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setBarType(BarType type) {
		barType = type;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// Initialize bar
		Bar bar;
		for (int i = 0; i < bars.length; i++) {
			switch (barType) {
			case VERTICAL:
				bar = new VerticalBar(i, w, h, LEGEND_TEXT_SIZE);
				break;
			case HORIZONTAL:
				bar = new HorizontalBar(i, w, h, LEGEND_TEXT_SIZE);
				break;
			default:
				bar = new VerticalBar(i, w, h, LEGEND_TEXT_SIZE);
				break;
			}
			bars[i] = bar;
		}

		// Set run's count
		String[] times = item.getTime().split(",");
		for (String s : times) {
			int hour = Integer.parseInt(s.substring(0, s.indexOf(":")));
			bars[hour].incrCount();
		}

		// Set rectangle
		for (int i = 0; i < bars.length; i++) {
			bars[i].setRectangle();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Bar bar;
		for (int i = 0; i < bars.length; i++) {
			bar = bars[i];
			if (i % 2 == 0) {
				barPaint.setColor(getResources().getColor(R.color.blue01));
			} else {
				barPaint.setColor(getResources().getColor(R.color.blue10));
			}
			canvas.drawRect(bar.getRectangle(), barPaint);
		}

		invalidate();
	}
}

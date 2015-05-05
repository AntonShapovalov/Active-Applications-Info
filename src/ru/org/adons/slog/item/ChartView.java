package ru.org.adons.slog.item;

import ru.org.adons.slog.LogItem;
import ru.org.adons.slog.R;
import ru.org.adons.slog.item.bar.Bar;
import ru.org.adons.slog.item.bar.HorizontalBar;
import ru.org.adons.slog.item.bar.VerticalBar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class ChartView extends View {

	private LogItem item;
	private final Bar[] bars = new Bar[24];
	private final Paint barPaint = new Paint();
	private final Paint legendPaint = new Paint();
	private final static int LEGEND_TEXT_SIZE = 12;

	public ChartView(Context context, LogItem item) {
		super(context);
		this.item = item;
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// Initialize bar
		Bar bar;
		for (int i = 0; i < bars.length; i++) {
			if (w > h) {
				bar = new VerticalBar(i, w, h, LEGEND_TEXT_SIZE);
			} else {
				bar = new HorizontalBar(i, w, h, LEGEND_TEXT_SIZE);
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

		legendPaint.setTextSize(LEGEND_TEXT_SIZE);
		legendPaint.setTextAlign(Align.CENTER);
		Typeface t = legendPaint.getTypeface();
		Typeface tf = Typeface.create(t, Typeface.BOLD);
		legendPaint.setTypeface(tf);

		Bar bar;
		for (int i = 0; i < bars.length; i++) {
			bar = bars[i];
			// legend
			canvas.drawText(String.format("%02dh", i), bar.getLegendX(), bar.getLegendY(), legendPaint);
			// bar
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

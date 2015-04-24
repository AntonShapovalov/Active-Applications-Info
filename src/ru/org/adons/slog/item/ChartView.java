package ru.org.adons.slog.item;

import ru.org.adons.slog.LogItem;
import ru.org.adons.slog.item.bar.BarView;
import ru.org.adons.slog.item.circle.CircleView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class ChartView extends ViewGroup {

	private CircleView circle;
	private BarView bar;

	public ChartView(Context context, LogItem item) {
		super(context);
		circle = new CircleView(context);
		addView(circle);
		bar = new BarView(context, item);
		addView(bar);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// do all in onSizeChanged()
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w > h) {
			circle.layout(0, 0, w / 2, h);
			bar.setBarType(BarView.BarType.VERTICAL);
			bar.layout(w / 2, 0, w, h);
		} else {
			circle.layout(0, 0, w, h / 2);
			bar.setBarType(BarView.BarType.HORIZONTAL);
			bar.layout(0, h / 2, w, h);
		}
	}

}

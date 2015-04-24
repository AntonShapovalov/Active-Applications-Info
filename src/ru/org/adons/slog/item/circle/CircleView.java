package ru.org.adons.slog.item.circle;

import ru.org.adons.slog.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CircleView extends View {

	private final Paint paint = new Paint();

	private int cx;
	private int cy;
	private int r1;
	private int r2;
	private RectF externalBounds;
	private Rect[] rectangles;

	public CircleView(Context context) {
		super(context);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		cx = w / 2;
		cy = h / 2;
		r1 = w > h ? h / 4 : w / 4;
		r2 = r1 / 2;
		externalBounds = new RectF(cx - r1 - r2, cy - r1 - r2, cx + r1 + r2, cy + r1 + r2);
		RectangleHolder rHolder = new RectangleHolder(cx, cy, r1, r2);
		rectangles = rHolder.getRectangles();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRGB(255, 255, 255);
		paint.setStyle(Style.FILL);

		// external circle
		paint.setColor(getResources().getColor(R.color.blue10));
		canvas.drawCircle(cx, cy, r1 + r2, paint);

		// slices
		paint.setColor(getResources().getColor(R.color.blue01));
		canvas.drawArc(externalBounds, -90, 30, true, paint);
		paint.setColor(getResources().getColor(R.color.blue04));
		canvas.drawArc(externalBounds, -60, 30, true, paint);
		paint.setColor(getResources().getColor(R.color.blue01));
		canvas.drawArc(externalBounds, -30, 30, true, paint);

		// internal circle
		paint.setColor(getResources().getColor(R.color.blue14));
		canvas.drawCircle(cx, cy, r1, paint);

		// rectangles - for debug only
		showRectangles(canvas);

		invalidate();
	}

	private void showRectangles(Canvas canvas) {
		paint.setStyle(Style.STROKE);
		paint.setColor(getResources().getColor(R.color.orange01));
		for (int i = 0; i < 3; i++) {
			canvas.drawRect(rectangles[i], paint);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		for (int i = 0; i < 3; i++) {
			Rect rect = rectangles[i];
			if (rect.contains(x, y)) {
				Toast.makeText(getContext(), "slice # " + (i + 1), Toast.LENGTH_SHORT).show();
			}
		}
		return super.onTouchEvent(event);
	}

}

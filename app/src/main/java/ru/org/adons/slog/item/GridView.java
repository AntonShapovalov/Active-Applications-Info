package ru.org.adons.slog.item;

import ru.org.adons.slog.LogDataHolder;
import ru.org.adons.slog.LogItem;
import ru.org.adons.slog.R;
import ru.org.adons.slog.item.grid.HorizontalTimeGrid;
import ru.org.adons.slog.item.grid.TimeGrid;
import ru.org.adons.slog.item.grid.VerticalTimeGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class GridView extends View {

    private int itemID;
    private TimeGrid grid;
    private final Paint paint = new Paint();

    public GridView(Context context, int itemID) {
        super(context);
        this.itemID = itemID;
    }

    public GridView(Context context, AttributeSet attrs) {
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

        // draw background color of cell
        paint.setStyle(Style.FILL);
        paint.setColor(getResources().getColor(R.color.blue14));
        for (int i = 0; i < grid.getCells().size(); i++) {
            row = grid.getCells().get(i);
            for (int j = 0; j < row.length; j++) {
                canvas.drawRect(row[j], paint);
            }
        }

        // time: fill cell and set text
        paint.setTextAlign(Align.CENTER);
        paint.setAntiAlias(true);
        LogDataHolder holder = LogDataHolder.getHolder(null);
        LogItem item = holder.getItems().get(itemID);
        String[] times = item.getTime().split(",");
        int hour;
        int minute;
        String key;
        int[] gridIndex;
        Rect rect;
        for (String s : times) {
            hour = Integer.parseInt(s.substring(0, s.indexOf(":")));
            minute = Integer.parseInt(s.substring(s.indexOf(":") + 1));
            key = String.valueOf(hour) + getMinuteIndex(minute);
            gridIndex = grid.getIndex().get(key);
            row = grid.getCells().get(gridIndex[0]);
            rect = row[gridIndex[1]];
            // fill cell
            paint.setColor(getResources().getColor(R.color.blue12));
            canvas.drawRect(rect, paint);
            // set text
            paint.setColor(Color.BLACK);
            paint.setTextSize(rect.height() - 4);
            canvas.drawText(s, rect.exactCenterX(), rect.exactCenterY() + paint.getTextSize() / 2 - 2, paint);
        }

        // draw border of cell
        paint.setStyle(Style.STROKE);
        paint.setColor(getResources().getColor(R.color.blue01));
        for (int i = 0; i < grid.getCells().size(); i++) {
            row = grid.getCells().get(i);
            for (int j = 0; j < row.length; j++) {
                canvas.drawRect(row[j], paint);
            }
        }

        invalidate();
    }

    private String getMinuteIndex(int min) {
        String res;
        if (min <= 15) {
            res = "1";
        } else if (15 < min && min <= 30) {
            res = "2";
        } else if (30 < min && min <= 45) {
            res = "3";
        } else {
            res = "4";
        }
        return res;
    }
}

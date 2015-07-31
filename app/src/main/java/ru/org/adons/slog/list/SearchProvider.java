package ru.org.adons.slog.list;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.ArrayList;

import ru.org.adons.slog.LogDataHolder;
import ru.org.adons.slog.LogItem;

public class SearchProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        if (selectionArgs[0].length() > 1) {
            MatrixCursor mCursor = new MatrixCursor(new String[]{"_id", SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA});
            LogDataHolder holder = LogDataHolder.getHolder(null);
            ArrayList<LogItem> list = (ArrayList<LogItem>) holder.getItems();
            int i = 1;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getName().contains(selectionArgs[0])) {
                    mCursor.addRow(new String[]{Integer.toString(i), list.get(j).getName(), Integer.toString(j)});
                    i++;
                }
            }
            if (i > 1) cursor = mCursor;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

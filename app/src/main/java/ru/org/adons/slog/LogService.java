package ru.org.adons.slog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LogService extends IntentService {

    private static final String TAG = "LogService";

    public LogService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogDataHolder holder = LogDataHolder.getHolder(getApplicationContext());
        holder.loadData();
        // write to file
        try {
            FileOutputStream fos = openFileOutput(getString(R.string.service_file_name), Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(holder.getResultInfo());
            osw.close();
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

}

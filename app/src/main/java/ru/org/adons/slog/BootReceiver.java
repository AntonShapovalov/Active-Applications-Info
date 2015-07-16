package ru.org.adons.slog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.app_preferences_file),
                    Context.MODE_PRIVATE);
            if (sp.getBoolean(context.getString(R.string.log_pref_key_switch), false)) {
                ServiceScheduler sh = new ServiceScheduler(context);
                sh.startLogService();
            }
        }
    }

}

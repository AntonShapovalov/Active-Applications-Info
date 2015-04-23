package ru.org.adons.slog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class ServiceScheduler {

	private Context context;
	private PendingIntent serviceIntent;
	private AlarmManager alarmManager;

	public ServiceScheduler(Context context) {
		this.context = context;
		serviceIntent = PendingIntent.getService(context, 0, new Intent(context, LogService.class), 0);
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	public void startLogService() {
		long firstTime = SystemClock.elapsedRealtime();
		long interval;
		int prefInt = Integer.parseInt(context.getString(R.string.service_interval_default));
		switch (prefInt) {
		case 15:
			interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
			break;
		case 30:
			interval = AlarmManager.INTERVAL_HALF_HOUR;
			break;
		case 45:
			interval = AlarmManager.INTERVAL_HALF_HOUR + AlarmManager.INTERVAL_FIFTEEN_MINUTES;
			break;
		case 60:
			interval = AlarmManager.INTERVAL_HOUR;
			break;
		default:
			interval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
			break;
		}
		// Schedule the alarm
		// TODO: setInexactRepeating for main version, setRepeating for test only
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, interval, serviceIntent);
	}

	public void stopLogService() {
		// Cancel the alarm.
		alarmManager.cancel(serviceIntent);
	}

}

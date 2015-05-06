package ru.org.adons.slog;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.text.format.Time;

public class LogDataHolder {

	// class fields
	private static LogDataHolder holder = new LogDataHolder();
	private Context context;
	// data fields
	private int day;
	private int count;
	private List<LogItem> items = new ArrayList<LogItem>();
	// utils fields
	private boolean isFileEmpty = true;
	private Time time = new Time(Time.getCurrentTimezone());
	String[] excludeFilter;
	String[] includeFilter;

	// private constructor
	private LogDataHolder() {
	}

	// to create new single holder
	public static LogDataHolder getHolder(Context context) {
		if (context != null) {
			holder.setContext(context);
		}
		return holder;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public synchronized void loadData() {
		// clear previous value
		isFileEmpty = true;
		time.setToNow();
		items.clear();
		excludeFilter = context.getResources().getStringArray(R.array.service_filter_exclude_app);
		includeFilter = context.getResources().getStringArray(R.array.service_filter_include_app);
		// try load from file
		loadFile();
		// load current activity
		loadSystem();
	}

	private void loadFile() {
		try {
			InputStream is = context.openFileInput(context.getString(R.string.service_file_name));
			if (is != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String s = "";
				boolean firstRow = true;
				while ((s = br.readLine()) != null) {
					if (firstRow) {
						firstRow = false;
						String[] sb = getSplittedString(s, 2);
						setDay(sb[0]);
						setCount(sb[1]);
					} else {
						addItem(getSplittedString(s, 3));
					}
				}
				is.close();
				isFileEmpty = false;
			}
		} catch (FileNotFoundException e) {
			isFileEmpty = true;
		} catch (IOException e) {
			isFileEmpty = true;
		}
	}

	private void loadSystem() {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// running processes
		List<RunningAppProcessInfo> rps = am.getRunningAppProcesses();
		String pName;
		// running applications
		PackageManager pm = context.getPackageManager();
		ApplicationInfo ai;
		String aName;

		// SET day, count
		if (!isFileEmpty) {
			if (getDay() != time.monthDay) {
				items.clear();
				setDay(time.monthDay);
				setCount(1);
			} else {
				setCount(getCount() + 1);
			}
		} else {
			setDay(time.monthDay);
			setCount(1);
		}

		// SET items
		StringBuilder sb = new StringBuilder();
		ArrayList<String> newItems = new ArrayList<String>();
		boolean isNewItem = true;
		for (RunningAppProcessInfo rp : rps) {
			sb.setLength(0);
			pName = rp.processName;
			try {
				ai = pm.getApplicationInfo(pName, 0);
				aName = (String) pm.getApplicationLabel(ai);
			} catch (NameNotFoundException e) {
				aName = "NO_DEF_NAME";
			}
			if (!isMatchFilter(pName)) {
				sb.append(rp.processName).append("/").append(aName);
				// compare with exist items and increase count
				if (!isFileEmpty) {
					isNewItem = true;
					for (LogItem item : getItems()) {
						if (item.getName().equals(sb.toString())) {
							item.setCount(item.getCount() + 1);
							item.setTime(item.getTime() + "," + time.hour + ":"
									+ (time.minute < 10 ? "0" + time.minute : time.minute));
							isNewItem = false;
						}
					}
					if (isNewItem) {
						newItems.add(sb.toString());
					}
				} else {
					addItem(sb.toString());
				}
			}
		}
		// add new items to existing
		for (String s : newItems) {
			addItem(s);
		}
		Collections.sort(items);
	}

	/**
	 * setters
	 * 
	 */
	private void setDay(String day) {
		this.day = (!TextUtils.isEmpty(day)) ? Integer.parseInt(day) : time.monthDay;
	}

	private void setDay(int day) {
		this.day = day;
	}

	private void setCount(String count) {
		this.count = (!TextUtils.isEmpty(count)) ? Integer.parseInt(count) : 1;
	}

	private void setCount(int count) {
		this.count = count;
	}

	private void addItem(String[] sb) {
		String iName = (!TextUtils.isEmpty(sb[0])) ? sb[0] : "NO_DEF_NAME";
		int iCount = (!TextUtils.isEmpty(sb[1])) ? Integer.parseInt(sb[1]) : 1;
		String iTime = (!TextUtils.isEmpty(sb[2])) ? sb[2] : time.hour + ":"
				+ (time.minute < 10 ? "0" + time.minute : time.minute);
		items.add(new LogItem(iName, iCount, iTime));
	}

	private void addItem(String name) {
		String iName = (!TextUtils.isEmpty(name)) ? name : "NO_DEF_NAME";
		int iCount = 1;
		String iTime = time.hour + ":" + (time.minute < 10 ? "0" + time.minute : time.minute);
		items.add(new LogItem(iName, iCount, iTime));
	}

	/**
	 * getters and utils
	 * 
	 */
	private String[] getSplittedString(String s, int length) {
		String[] t = s.split(";");
		String[] r = new String[length];
		r[0] = t[0];
		for (int i = 1; i < length; i++) {
			r[i] = (t.length == length) ? t[i] : "";
		}
		return r;
	}

	private boolean isMatchFilter(String name) {
		boolean result = false;
		for (String f : excludeFilter) {
			if (name.startsWith(f)) {
				result = true;
			}
		}
		for (String f : includeFilter) {
			if (name.startsWith(f)) {
				result = false;
			}
		}
		return result;
	}

	// create result string to save into file
	public String getResultInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDay()).append(";").append(getCount()).append("\n"); // first
		for (LogItem item : getItems()) {
			sb.append(item.getName()).append(";");
			sb.append(item.getCount()).append(";");
			sb.append(item.getTime()).append("\n");
		}
		// Log.i("LogService", sb.toString());
		return sb.toString();
	}

	public int getDay() {
		return day;
	}

	public int getCount() {
		return count;
	}

	public List<LogItem> getItems() {
		return items;
	}
}

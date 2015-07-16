package ru.org.adons.slog.list;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.org.adons.slog.BootReceiver;
import ru.org.adons.slog.LogDataHolder;
import ru.org.adons.slog.R;
import ru.org.adons.slog.ServiceScheduler;
import ru.org.adons.slog.item.ItemActivity;

public class LogActivity extends ListActivity {

    private ServiceScheduler scheduler;
    private ListDataAdapter adapter;
    private LogDataHolder dataHolder;
    // Service switch
    private SharedPreferences pref;
    private ToggleButton sw;
    private boolean swState;
    private View statusBackground;
    // utils fields
    private final StringBuilder sb = new StringBuilder();
    private final static int RED = R.color.orange01;
    private final static int GREEN = R.color.green01;
    private boolean isDataUpdated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduler = new ServiceScheduler(this);
        setContentView(R.layout.activity_log);
        adapter = new ListDataAdapter(this);
        setListAdapter(adapter);
        dataHolder = LogDataHolder.getHolder(null);

        // to update UI list if data were updated by service
        isDataUpdated = true;
        dataHolder.observable.registerObserver(new DataObserver());

        // initialize text status, set in setOnCheckedChangeListener
        statusBackground = (View) findViewById(R.id.log_view_status_backgroud);

        // handle Service switch
        sw = (ToggleButton) findViewById(R.id.log_button_switch_service);
        sw.setOnCheckedChangeListener(switchChange);
        sw.setOnClickListener(switchClick);

        // set switch value from preferences
        pref = getSharedPreferences(getString(R.string.app_preferences_file), MODE_PRIVATE);
        swState = pref.getBoolean(getString(R.string.log_pref_key_switch), false);

        if (swState) {
            sw.setChecked(swState); // call switchChange event
        } else {
            statusBackground.setBackgroundColor(getResources().getColor(RED));
        }
    }

    /*
     * load data in list
     */
    @Override
    protected void onResume() {
        if (isDataUpdated) {
            adapter.setData(dataHolder);
            adapter.notifyDataSetChanged();
            isDataUpdated = false;
        }
        super.onResume();
    }

    /*
     * set Button State and Service Text status from Preferences, even user not
     * click Button in this session
     */
    private OnCheckedChangeListener switchChange = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // save switch state to preferences
            swState = isChecked;
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(getString(R.string.log_pref_key_switch), swState);
            editor.commit();
            // set background
            if (swState) {
                statusBackground.setBackgroundColor(getResources().getColor(GREEN));
            } else {
                statusBackground.setBackgroundColor(getResources().getColor(RED));
            }
        }
    };

    /*
     * start or stop Log Service
     */
    private OnClickListener switchClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ComponentName receiver = new ComponentName(getApplicationContext(), BootReceiver.class);
            PackageManager pm = getApplicationContext().getPackageManager();
            sb.setLength(0);
            if (swState) {
                // start service
                scheduler.startLogService();
                // show message
                sb.append(getString(R.string.service_toast_scheduled)).append(" ");
                sb.append(getString(R.string.service_interval_default));
                sb.append(" min");
                // enable Boot Receiver, to start Log Service after reboot
                pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            } else {
                // stop service
                scheduler.stopLogService();
                // show message
                sb.append(getString(R.string.service_toast_unscheduled));
                // disable Boot Receiver
                pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }
            Toast.makeText(LogActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra(getString(R.string.log_intent_key_item_id), position);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    /*
     * get notification if data were updated by service
     */
    private class DataObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            isDataUpdated = true;
        }

        @Override
        public void onInvalidated() {
            isDataUpdated = true;
        }
    }

    /**
     * handle App Search action button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_log_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.log_activity_action_search:
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package ru.org.adons.slog.main;

import ru.org.adons.slog.LogDataHolder;
import ru.org.adons.slog.R;
import ru.org.adons.slog.list.LogActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_title);
        setContentView(R.layout.activity_main);
        new LoadLogs().execute();
    }

    public void login(View view) {
        String pass = ((EditText) findViewById(R.id.main_text_password)).getText().toString();
        if (TextUtils.isEmpty(pass) || getString(R.string.main_string_password_default_value).equals(pass)) {
            Intent intent = new Intent(this, LogActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.main_string_logging_warning, Toast.LENGTH_LONG).show();
        }
    }

    private class LoadLogs extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            LogDataHolder.getHolder(MainActivity.this).loadData();
            return null;
        }
    }

}

package ru.org.adons.slog.item;

import ru.org.adons.slog.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get extra intent
		Intent intent = getIntent();
		int itemID = intent.getIntExtra(getString(R.string.log_intent_key_item_id), 0);

		setContentView(new GridView(this, itemID));
	}

}

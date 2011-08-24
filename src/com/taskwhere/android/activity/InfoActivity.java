package com.taskwhere.android.activity;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		ActionBar actioBar = (ActionBar) findViewById(R.id.actionbar);
		actioBar.setHomeAction(new IntentAction(this, TaskWhereActivity.createIntent(this), R.drawable.home));
	}

	public static Intent createIntent(Context context) {
		
		Intent i = new Intent(context, InfoActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
	}
	
	
}

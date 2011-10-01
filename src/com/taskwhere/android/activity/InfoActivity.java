package com.taskwhere.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

/**
 * 
 * @author burak
 * @date 26 Aug 2011
 * 
 * activity that gives information about
 * how to use application and send feedback
 */
public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, TaskWhereActivity.createIntent(this), R.drawable.home));
		
		Intent mailIntent = new Intent(Intent.ACTION_SEND);
		mailIntent.setType("text/plain");
		mailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
		mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Location Aware Task Management App : [TaskWhere]");
		mailIntent.putExtra(Intent.EXTRA_TEXT, "http://www.amazon.com/gp/mas/dl/android?p=com.taskwhere.android");
		final Action mailAction = new IntentAction(this, mailIntent , R.drawable.email);
        actionBar.addAction(mailAction);	
	}
	
	public static Intent createIntent(Context context) {
		
		Intent i = new Intent(context, InfoActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
	}
}

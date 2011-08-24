package com.taskwhere.android.activity;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class TaskWhereActivity extends Activity {
    
	private final static String TW = "TaskWhere";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this),R.drawable.ic_title_home_default));
        
        final Action addAction = new IntentAction(this, AddTaskActivity.createIntent(this), R.drawable.ic_menu_tick);
        actionBar.addAction(addAction);
    }
    
    public static Intent createIntent(Context context) {
		
		Intent i = new Intent(context, TaskWhereActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
	}
}

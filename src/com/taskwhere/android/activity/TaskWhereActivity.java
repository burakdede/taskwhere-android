package com.taskwhere.android.activity;

import android.app.Activity;
import android.os.Bundle;

public class TaskWhereActivity extends Activity {
    
	private final static String TW = "TaskWhere";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}

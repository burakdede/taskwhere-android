package com.taskwhere.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.taskwhere.android.adapter.TaskListAdapter;
import com.taskwhere.android.adapter.TaskListDbAdapter;
import com.taskwhere.android.model.Task;

/**
 * 
 * @author burak
 * @date 26 Aug 2011
 * main activity that show the list 
 * of saved tasks to the user
 */
public class TaskWhereActivity extends Activity {
    
	private final static String TW = "TaskWhere";
	private ArrayList<Task> taskList;
	private ListView taskListView;
	private static ActionBar actionBar;
	private TaskListDbAdapter dbAdapter;
	
	/**
	 * setup actionbar pattern using {@link ActionBar}
	 * class and set {@link IntentAction} accordingly
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main);
        setContentView(R.layout.main);
        taskList = new ArrayList<Task>();
        
        //set actionbar intents and activities accordingly
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this),R.drawable.home));
        final Action infoAction = new IntentAction(this, InfoActivity.createIntent(this), R.drawable.info);
        actionBar.addAction(infoAction);
        final Action addAction = new IntentAction(this, AddTaskActivity.createIntent(this), R.drawable.add_item);
        actionBar.addAction(addAction);
        
        showSavedProfiles();
        
        taskListView = (ListView) findViewById(R.id.taskList);
        taskListView.setAdapter(new TaskListAdapter(this, taskList));
    }
    
    /**
     * get profiles from {@link TaskListDbAdapter}
     * and store in list to show with listview
     */
    public void showSavedProfiles(){
    	
    	dbAdapter = new TaskListDbAdapter(getApplicationContext());
    	dbAdapter.open();
    	
    /*	Task task = new Task("Test Task 1", "Place1", 40.459459, 29.04545, 2343,0);
    	Task task2 = new Task("Test Task 2", "Place2", 45.45454, 34.45454, 3234, 1);
    	Task task3 = new Task("Test Task 3", "Place3", 42.3454, 45.3434, 3434,0);
    	dbAdapter.insertNewTask(task);
    	dbAdapter.insertNewTask(task2);
    	dbAdapter.insertNewTask(task3); */
    	
    	Cursor taskCursor = dbAdapter.getAllTasks();
    	startManagingCursor(taskCursor);
    	
    	Log.d(TW, "Cursor count : " + taskCursor.getCount());
    	
    	if(taskCursor.getCount() > 0){
    		taskCursor.moveToFirst();
    		do{
        		taskList.add(new Task(taskCursor.getString(1), taskCursor.getString(2),
        				taskCursor.getDouble(3), taskCursor.getDouble(4), taskCursor.getInt(5),taskCursor.getInt(6)));
    		}while(taskCursor.moveToNext());
    	}
    	
    	if(taskCursor != null)
    		taskCursor.close();
    }
    
    /**
     * do some database cleanup
     */
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	dbAdapter.close();
    }
    
    public static Intent createIntent(Context context) {
    	Intent i = new Intent(context, TaskWhereActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}

package com.taskwhere.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
import com.taskwhere.android.adapter.TaskListAdapter;
import com.taskwhere.android.adapter.TaskListDbAdapter;
import com.taskwhere.android.model.Task;
import com.taskwhere.android.widget.ActionItem;
import com.taskwhere.android.widget.QuickAction;

/**
 * 
 * @author burak
 * @date 26 Aug 2011,
 * 
 * main activity that show the list 
 * of saved tasks to the user
 */
public class TaskWhereActivity extends Activity {
    
	private final static String TW = "TaskWhere";
	private ArrayList<Task> taskList;
	private ListView taskListView;
	private static ActionBar actionBar;
	private TaskListDbAdapter dbAdapter;
	private Cursor taskCursor;
	private static int mSelectedRow;
	private final static String EDIT_TASK = "com.taskwhere.android.Task";
	private static final String ARRIVED_ACTION = "com.taskwhere.android.ARRIVED_ACTION";
	private static final String ACTIVE_TASK_LOC = "com.taskwhere.android.model.TaskLoc";
	private static final String ACTIVE_TASK_TEXT = "com.taskwhere.android.model.TaskText";
	private SharedPreferences preferences;
	private TaskListAdapter taskListAdapter;
	private LocationManager locationManager;
	
	/**
	 * setup actionbar pattern using {@link ActionBar}
	 * class and set {@link IntentAction} accordingly
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        
        Intent intent = new Intent(getApplicationContext(),LocationProxyService.class);
      	startService(intent);
        
        taskList = new ArrayList<Task>();
        openDatabaseAccess();   
        taskCursor = dbAdapter.getAllTasks();
        
        if(taskCursor.getCount() == 0){
        	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tasklist_empty);
        	setContentView(R.layout.tasklist_empty);
        	
        }else{
        	
        	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main);
        	setContentView(R.layout.main);
        	
        	showSavedProfiles();
            taskListView = (ListView) findViewById(R.id.taskListView);
            taskListAdapter = new TaskListAdapter(this, taskList);
            taskListView.setAdapter(taskListAdapter);
            
        	ActionItem addAction = new ActionItem();
			addAction.setTitle("Edit");
			addAction.setIcon(getResources().getDrawable(R.drawable.edit));
	
			ActionItem accAction = new ActionItem();
			accAction.setTitle("Mark As Done");
			accAction.setIcon(getResources().getDrawable(R.drawable.done));
			
			ActionItem upAction = new ActionItem();
			upAction.setTitle("Delete");
			upAction.setIcon(getResources().getDrawable(R.drawable.delete));
			
			ActionItem activeAction = new ActionItem();
			activeAction.setTitle("Activate");
			activeAction.setIcon(getResources().getDrawable(R.drawable.activate));
			
			ActionItem deactiveAction = new ActionItem();
			deactiveAction.setTitle("Deactivate");
			deactiveAction.setIcon(getResources().getDrawable(R.drawable.deactivate));
			
			final QuickAction mQuickAction 	= new QuickAction(this);
			mQuickAction.addActionItem(addAction);
			mQuickAction.addActionItem(accAction);
			mQuickAction.addActionItem(upAction);
			mQuickAction.addActionItem(activeAction);
			mQuickAction.addActionItem(deactiveAction);
			
			/*
			 * click listener for quick action
			 * widget
			 */
			mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
				@Override
				public void onItemClick(int pos) {
					
					Intent callIntent;
					Log.d(TW, "Selected position : " + mSelectedRow);
					Task selectedTask = taskList.get(mSelectedRow);
					
					if (pos == 0) { //edit task item 
						
						
						callIntent = new Intent();
						callIntent.setClass(getApplicationContext(), AddTaskActivity.class);
						callIntent.putExtra(EDIT_TASK, selectedTask);
						startActivity(callIntent);
						
					} else if (pos == 1 && selectedTask.getStatus() != 1) { // mark as done
						
						selectedTask.setStatus(1);
						taskList.get(mSelectedRow).setStatus(1);
						taskList.set(mSelectedRow, selectedTask);
						taskListAdapter.updateData();
						
						if(dbAdapter.updateTaskByUniqueId(selectedTask)){
							Log.d(TW, "Updated item succesfully");
							removeOldProximityAlert(taskList.get(mSelectedRow).getUnique_taskid());
							taskListAdapter.updateData();
						}
						
					} else if (pos == 2) { //delete task
						
						taskList.remove(mSelectedRow);
						taskListAdapter.updateData();
						
						if(dbAdapter.deleteTaskByUniqueId(selectedTask.getUnique_taskid())){
							Log.d(TW, "Deleted succesfully");
							removeOldProximityAlert(selectedTask.getUnique_taskid());
							taskListAdapter.updateData();
							
							//no more task to show change layout
							if(taskList.size() == 0){
								getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.tasklist_empty);
								setContentView(R.layout.tasklist_empty);
								setUpActionBar(actionBar);
							}
						}
					} else if (pos == 3 && selectedTask.getStatus() == -1){ // activate task

						Log.d(TW, "Selected task to activate");
						selectedTask.setStatus(0);
						taskList.get(mSelectedRow).setStatus(0);
						taskList.set(mSelectedRow, selectedTask);
						activateProximityAlert(selectedTask);
						taskListAdapter.updateData();
						
					} else if (pos == 4 && selectedTask.getStatus() != -1){ // deactivate task

						Log.d(TW, "Selected task to deactivate");
						selectedTask.setStatus(-1);
						taskList.get(mSelectedRow).setStatus(-1);
						taskList.set(mSelectedRow, selectedTask);
						removeOldProximityAlert(selectedTask.getUnique_taskid());
						taskListAdapter.updateData();
					}
				}
			});

			/*
			 * send clicked list item info to quick action
			 * widget
			 */
			taskListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					mSelectedRow = arg2;
					Log.d(TW, "Selected Profile : " + taskList.get(mSelectedRow));
					mQuickAction.show(view);
				}
			});
        }
        
        
        setUpActionBar(actionBar);
    }
    
    public void setUpActionBar(ActionBar actionBar){
    	
    	//set actionbar intents and activities accordingly
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this),R.drawable.home));
        final Action infoAction = new IntentAction(this, InfoActivity.createIntent(this), R.drawable.info);
        actionBar.addAction(infoAction);
        final Action addAction = new IntentAction(this, AddTaskActivity.createIntent(this), R.drawable.add_item);
        actionBar.addAction(addAction);
    }
    
    protected void removeOldProximityAlert(int unique_taskid) {
    	
    	String context = Context.LOCATION_SERVICE;
    	LocationManager locationManager = (LocationManager) getSystemService(context);

    	Intent anIntent = new Intent(ARRIVED_ACTION);
		PendingIntent operation = 
				PendingIntent.getBroadcast(getApplicationContext(), unique_taskid , anIntent, 0);
		locationManager.removeProximityAlert(operation);
	}
    
    private void activateProximityAlert(Task newTask) {
		
		String contenxt = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(contenxt);
		
		Intent anIntent = new Intent(ARRIVED_ACTION);
		anIntent.putExtra(ACTIVE_TASK_LOC, newTask.getTaskLoc());
		anIntent.putExtra(ACTIVE_TASK_TEXT, newTask.getTaskText());

		PendingIntent operation = PendingIntent.getBroadcast(getApplicationContext(), newTask.getUnique_taskid() , anIntent, 0);
		locationManager.addProximityAlert(newTask.getTaskLat(), newTask.getTaskLon(), newTask.getProx_radius() , -1, operation);
	}

	/**
     * get profiles from {@link TaskListDbAdapter}
     * and store in list to show with listview
     */
    public void showSavedProfiles(){
    	
    	taskList.clear();
    	startManagingCursor(taskCursor);
    	Log.d(TW, "Cursor count : " + taskCursor.getCount());
    	
    	if(taskCursor.getCount() > 0){
    		taskCursor.moveToFirst();
    		do{
        		taskList.add(new Task(taskCursor.getString(1), taskCursor.getString(2),
        				taskCursor.getDouble(3), taskCursor.getDouble(4), taskCursor.getInt(5),taskCursor.getInt(6),taskCursor.getInt(7)));
    		}while(taskCursor.moveToNext());
    	}
    }
    
    
    public void openDatabaseAccess(){
    	dbAdapter = new TaskListDbAdapter(getApplicationContext());
    	dbAdapter.open();
    }
    
    
    /**
     * do some database cleanup
     */
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(dbAdapter != null)
    		dbAdapter.close();
    	if(taskCursor != null)
    		taskCursor.close();
    }
    
    public static Intent createIntent(Context context) {

    	Intent i = new Intent(context, TaskWhereActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
}

package com.taskwhere.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

/**
 * 
 * @author burak
 * @date 26 Aug 2011
 * 
 * activity allows user to search address
 * on maps and animate to that address on map.
 */
public class SearchAddressActivity extends Activity {

	private static boolean addressSearch;
	private final static String SEARCH_REDIRECT = "com.taskwhere.android.activity.SEARCH_REDIRECT";
	private final static String SEARCH_ADDRESS = "com.taskwhere.android.activity.SEARCH_ADDRESS";
	private final static String TW = "Task Where";
	private ProgressDialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address);
		
		addressSearch = true;
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeAction(new IntentAction(this, TaskWhereActivity.createIntent(this),R.drawable.home));
		
		Intent cancelTaskIntent = new Intent(this, AddTaskActivity.class);
		cancelTaskIntent.putExtra(SEARCH_REDIRECT, addressSearch);
        cancelTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final Action infoAction = new IntentAction(this, cancelTaskIntent, R.drawable.delete_item);
        actionBar.addAction(infoAction);
        
        final EditText searchBar = (EditText) findViewById(R.id.searchBar);
        
        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(!searchBar.getText().toString().equals("")){
					
					showDialog(1);
					Log.d(TW, "Search is not empty");
					Intent accepTaskIntent = new Intent();
					accepTaskIntent.setClass(getApplicationContext(), AddTaskActivity.class);
					accepTaskIntent.putExtra(SEARCH_ADDRESS, searchBar.getText().toString());
					accepTaskIntent.putExtra(SEARCH_REDIRECT, addressSearch);
			        accepTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        startActivity(accepTaskIntent);
					
				}else{
					Log.d(TW, "Search is empty make toast and show");
					Toast warn = Toast.makeText(getApplicationContext(), "Enter valid address to search", Toast.LENGTH_SHORT);
					warn.show();
				}
			}
		});
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	
		loadingDialog = ProgressDialog.show(SearchAddressActivity.this, "", "Searching location of given address...",true);
    	loadingDialog.setCancelable(true);
    	
		return loadingDialog;
    }
	
    public static Intent createIntent(Context context) {
		
		Intent i = new Intent(context, SearchAddressActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
	}
}

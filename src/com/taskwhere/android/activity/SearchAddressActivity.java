package com.taskwhere.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class SearchAddressActivity extends Activity {

	private static boolean addressSearch;
	private final static String SEARCH_REDIRECT = "search_redirect";
	private final static String SEARCH_ADDRESS = "search_address";
	
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
        
        final Intent accepTaskIntent = new Intent(this, AddTaskActivity.class);
		accepTaskIntent.putExtra(SEARCH_REDIRECT, addressSearch);
        accepTaskIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final Action addAction = new IntentAction(this, accepTaskIntent, R.drawable.accept_item);
        actionBar.addAction(addAction);
        
        final EditText searchBar = (EditText) findViewById(R.id.searchBar);
		searchBar.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				accepTaskIntent.putExtra(SEARCH_ADDRESS, searchBar.getText().toString());
			}
		});
	}
	
    public static Intent createIntent(Context context) {
		
		Intent i = new Intent(context, SearchAddressActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
	}
}

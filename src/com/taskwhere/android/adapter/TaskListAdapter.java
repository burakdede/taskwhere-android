package com.taskwhere.android.adapter;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.taskwhere.android.activity.R;
import com.taskwhere.android.model.Task;

/**
 * 
 * @author burak
 * @date 26 Aug 2011
 * 
 * {@link ArrayAdapter} implementation for 
 * TaskWhereActivity activity 
 */
public class TaskListAdapter extends ArrayAdapter<Task>{

	private final static String TW = "TaskWhere";
	private final ArrayList<Task> taskList;
	private final Activity context;
	private LayoutInflater inflater;
	
	public TaskListAdapter(Activity context, ArrayList<Task> objects) {
		super(context, R.layout.task_item, objects);
		this.context = context;
		this.taskList = objects;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	//simple Holder pattern for different objects on row
	static class ViewHolder {
		public ImageView taskStatus;
		public TextView taskText;
		public TextView taskLoc;
	}
	
	/**
	 * 
	 * @author burak
	 * class to remove underline text
	 * from the textview linkify
	 */
	private class PlaceSpanNoUnderline extends URLSpan{

		public PlaceSpanNoUnderline(String url) {
			super(url);
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		ViewHolder holder;
		
		if(v == null){
			holder = new ViewHolder();
			inflater = context.getLayoutInflater();
			v = inflater.inflate(R.layout.task_item,null);
			holder.taskStatus = (ImageView) v.findViewById(R.id.taskStatus);
			holder.taskText = (TextView) v.findViewById(R.id.taskText);
			holder.taskLoc = (TextView) v.findViewById(R.id.taskLoc);
			v.setTag(holder);
		}else
			holder = (ViewHolder) v.getTag();
		
		
		//set task resource according to status
		Task task = taskList.get(position);
		Log.d(TW, task.toString());
		holder.taskStatus.setImageResource(task.getStatus() == 0 ?
							R.drawable.location_icon : R.drawable.checkmark);		
		holder.taskText.setText(task.getTaskText());
		
		//make location looks linkfied so that user can edit
		holder.taskLoc.setText("@" + task.getTaskLoc(),BufferType.SPANNABLE);
		holder.taskLoc.setLinkTextColor(R.color.grey_start);
		Pattern placeMatcher = Pattern.compile("\\B@[^:\\s]+");
		String placeViewURL = "place://";
		Linkify.addLinks(holder.taskLoc, placeMatcher, placeViewURL);
		
		return v;
	}
}
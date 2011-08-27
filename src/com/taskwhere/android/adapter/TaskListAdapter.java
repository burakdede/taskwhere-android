package com.taskwhere.android.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
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
		super(context, R.layout.tasklist_item, objects);
		this.context = context;
		this.taskList = objects;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	//simple Holder pattern for different objects on row
	static class ViewHolder {
		public ImageView taskStatus;
		public TextView taskText;
		public TextView taskLoc;
		public ImageView taskStatusImage;
	}
	
	/**
	 * 
	 * @author burak
	 * class to remove underline text
	 * from the textview linkify
	 */
/*	private class PlaceSpanNoUnderline extends URLSpan{

		public PlaceSpanNoUnderline(String url) {
			super(url);
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setUnderlineText(false);
		}
	}*/
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		ViewHolder holder;
		
		if(v == null){
			holder = new ViewHolder();
			inflater = context.getLayoutInflater();
			v = inflater.inflate(R.layout.tasklist_item,null);
			holder.taskStatus = (ImageView) v.findViewById(R.id.taskStatus);
			holder.taskText = (TextView) v.findViewById(R.id.taskText);
			holder.taskLoc = (TextView) v.findViewById(R.id.taskLoc);
			holder.taskStatusImage = (ImageView) v.findViewById(R.id.taskStatusImage);
			v.setTag(holder);
		}else
			holder = (ViewHolder) v.getTag();
		
		//set task resource according to status
		Task task = taskList.get(position);
		holder.taskStatus.setImageResource(R.drawable.location_icon);		
		holder.taskText.setText(task.getTaskText());
		//set paint strike if task is already done
		if(task.getStatus() == 1)
			holder.taskText.setPaintFlags(holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		holder.taskLoc.setText("@ " + task.getTaskLoc(),BufferType.SPANNABLE);
		holder.taskStatusImage.setImageResource(task.getStatus() == 0 ? R.drawable.taskwait : R.drawable.taskdone);
		
		return v;
	}
}
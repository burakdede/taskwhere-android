package com.taskwhere.android.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taskwhere.android.activity.R;
import com.taskwhere.android.model.Task;

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
	
	static class ViewHolder {
		public ImageView taskStatus;
		public TextView taskText;
		public TextView taskLoc;
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
		
		Task task = taskList.get(position);
		Log.d(TW, task.toString());
		holder.taskStatus.setImageResource(task.getStatus() == 0 ?
				R.drawable.question : R.drawable.tick);		
		holder.taskText.setText(task.getTaskText());
		holder.taskLoc.setText(task.getTaskLoc());
		return v;
	}
}
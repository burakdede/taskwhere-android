package com.taskwhere.android.model;

public class Task {

	private String taskText;
	
	private String taskLoc;
	
	private double taskLat;
	
	private double taskLon;
	
	private int unique_taskid;	

	public Task() {}
	
	public Task(String taskText, String taskLoc, double taskLat, double taskLon, int unique_taskid){
		
		this.taskText = taskText;
		this.taskLoc = taskLoc;
		this.taskLat = taskLat;
		this.taskLon = taskLon;
		this.unique_taskid = unique_taskid;
	}

	public String getTaskText() {
		return taskText;
	}

	public void setTaskText(String taskText) {
		this.taskText = taskText;
	}

	public String getTaskLoc() {
		return taskLoc;
	}

	public void setTaskLoc(String taskLoc) {
		this.taskLoc = taskLoc;
	}

	public double getTaskLat() {
		return taskLat;
	}

	public void setTaskLat(double taskLat) {
		this.taskLat = taskLat;
	}

	public double getTaskLon() {
		return taskLon;
	}

	public void setTaskLon(double taskLon) {
		this.taskLon = taskLon;
	}
		
	public int getUnique_taskid() {
		return unique_taskid;
	}

	public void setUnique_taskid(int unique_taskid) {
		this.unique_taskid = unique_taskid;
	}
}

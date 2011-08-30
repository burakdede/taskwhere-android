package com.taskwhere.android.model;

import java.io.Serializable;


/**
 * 
 * @author burak
 * @date 26 Aug 2011
 * 
 * Simple {@link Task} model object
 */
public class Task implements Serializable{

	private static final long serialVersionUID = 3762749485709681937L;

	private String taskText;
	
	private String taskLoc;
	
	private double taskLat;
	
	private double taskLon;
	
	private int unique_taskid;
	
	private int status;
	
	private int prox_radius;

	public Task() {}
	
	public Task(String taskText, String taskLoc, double taskLat, double taskLon, int unique_taskid, int prox_radius){
		
		this.taskText = taskText;
		this.taskLoc = taskLoc;
		this.taskLat = taskLat;
		this.taskLon = taskLon;
		this.unique_taskid = unique_taskid;
		this.status = 0;
		this.prox_radius = prox_radius;
	}
	
	public Task(String taskText, String taskLoc, double taskLat, double taskLon, int unique_taskid,int status, int prox_radius){
		
		this(taskText, taskLoc, taskLat, taskLon, unique_taskid, prox_radius);
		this.status = status;
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
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getProx_radius() {
		return prox_radius;
	}

	public void setProx_radius(int prox_radius) {
		this.prox_radius = prox_radius;
	}
	
	//for debugging
	@Override
	public String toString() {

		return "Task Text : " + this.getTaskText() + " Task Loc : " + this.getTaskLoc() + " Task Status : " + this.getStatus();
	}
}

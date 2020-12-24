package com.cargomonitoring;

import com.google.gson.annotations.SerializedName;

public class TaskModel {

    @SerializedName("_id")
    private String _id;

    @SerializedName("driver")
    private String driver;

    @SerializedName("task")
    private String task;

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

    public String getDriver() { return driver; }

    public void setDriver(String driver) { this.driver = driver; }

    public String getTask() { return task; }

    public void setTask(String task) { this.task = task; }
}

package com.example.fypapplication1.Models;

public class Timetable {
    String module, lecturer, room, time, day, key;

    //Empty constructor
    public Timetable() {
    }

    //Constructor

    public Timetable(String module, String lecturer, String room, String time) {
        this.module = module;
        this.lecturer = lecturer;
        this.room = room;
        this.time = time;

    }

    //Getter & Setter Methods
    public String getModule() {
        return module;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}

package com.example.examiner.Model;

public class ProjectModel {
    private String Teacher,Subject,Link,Action,Time;

    public ProjectModel() {
    }

    public ProjectModel(String teacher, String subject, String link, String action, String time) {
        Teacher = teacher;
        Subject = subject;
        Link = link;
        Action = action;
        Time = time;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}

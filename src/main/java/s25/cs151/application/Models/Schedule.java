package s25.cs151.application.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Schedule {
    private final StringProperty studentName;
    private final StringProperty date;
    private final StringProperty time;
    private final StringProperty course;
    private final StringProperty reason;
    private final StringProperty comment;

    public Schedule(String studentName, String date, String time, String course, String reason, String comment) {
        this.studentName = new SimpleStringProperty(studentName);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
        this.course = new SimpleStringProperty(course);
        this.reason = new SimpleStringProperty(reason);
        this.comment = new SimpleStringProperty(comment);
    }

    // Getters for properties
    public StringProperty studentNameProperty() {
        return studentName;
    }

    public StringProperty dateProperty() {
        return date;
    }

    public StringProperty timeProperty() {
        return time;
    }

    public StringProperty courseProperty() {
        return course;
    }

    public StringProperty reasonProperty() {
        return reason;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    // Regular getters
    public String getStudentName() {
        return studentName.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getCourse() {
        return course.get();
    }

    public String getReason() {
        return reason.get();
    }

    public String getComment() {
        return comment.get();
    }
}
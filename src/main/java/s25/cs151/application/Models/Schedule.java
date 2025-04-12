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


    /**
     * Calculate miltary time given a string in format of 09:00 AM
     */
    public Integer miltaryTimeCalc(String timeString) {

        String[] parts = timeString.split(" ");
        String timePart = parts[0];
        String noonVal = parts[1];
        String[] timeComponents = timePart.split(":");

        int hour = Integer.parseInt(timeComponents[0]);
        int minutes = Integer.parseInt(timeComponents[1]);

        // convert to miltary time
        if(noonVal.equals("AM")) {
            if(hour == 12) {
                hour = 0;
            }
        }
        else {
            if(hour != 12) {
                hour += 12;
            }
        }
        return (hour * 60) + minutes;
    }

    /**
     * Get order of time for ordering in table
     */
    public Integer getFromTimeOrder()
    {
        String timeString = getTime();

        String[] parts = timeString.split(" - ");
        String fromTimeString = parts[0].trim();

        return miltaryTimeCalc(fromTimeString);
    }

    public Integer getToTimeOrder()
    {
        String timeString = getTime();

        String[] parts = timeString.split(" - ");
        String toTimeString = parts[1].trim();

        return miltaryTimeCalc(toTimeString);
    }

}
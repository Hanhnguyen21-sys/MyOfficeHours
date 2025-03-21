package s25.cs151.application.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OfficeHours {
    private final StringProperty semester;
    private final StringProperty year;
    private final StringProperty days;
//    private final StringProperty time;
//    private final StringProperty course;

    public OfficeHours(String semester, String year, String days) {
        this.semester = new SimpleStringProperty(semester);
        this.year = new SimpleStringProperty(year);
        this.days = new SimpleStringProperty(days);
//        this.time = new SimpleStringProperty(time);
//        this.course = new SimpleStringProperty(course);
    }

    // Getters
    public String getSemester() {
        return semester.get();
    }

    public String getYear() {
        return year.get();
    }

    public String getDays() {
        return days.get();
    }

//    public String getTime() {
//        return time.get();
//    }
//
//    public String getCourse() {
//        return course.get();
//    }

    // Setters
    public void setSemester(String value) {
        semester.set(value);
    }

    public void setYear(String value) {
        year.set(value);
    }

    public void setDays(String value) {
        days.set(value);
    }

//    public void setTime(String value) {
//        time.set(value);
//    }
//
//    public void setCourse(String value) {
//        course.set(value);
//    }

    // Property getters
    public StringProperty semesterProperty() {
        return semester;
    }

    public StringProperty yearProperty() {
        return year;
    }

    public StringProperty daysProperty() {
        return days;
    }
//
//    public StringProperty timeProperty() {
//        return time;
//    }
//
//    public StringProperty courseProperty() {
//        return course;
//    }
}

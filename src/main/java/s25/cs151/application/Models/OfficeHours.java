package s25.cs151.application.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class OfficeHours {
    private IntegerProperty id;
    private final StringProperty semester;
    private final StringProperty year;
    private final StringProperty days;
    // private final StringProperty time;
    // private final StringProperty course;

    public OfficeHours(String semester, String year, String days) {
        this.id = new SimpleIntegerProperty(0);
        this.semester = new SimpleStringProperty(semester);
        this.year = new SimpleStringProperty(year);
        this.days = new SimpleStringProperty(days);
        // this.time = new SimpleStringProperty(time);
        // this.course = new SimpleStringProperty(course);
    }

    // ID Getter & Setter
    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        this.id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
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

    // public String getTime() {
    // return time.get();
    // }
    //
    // public String getCourse() {
    // return course.get();
    // }

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

    // public void setTime(String value) {
    // time.set(value);
    // }
    //
    // public void setCourse(String value) {
    // course.set(value);
    // }

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
    // public StringProperty timeProperty() {
    // return time;
    // }
    //
    // public StringProperty courseProperty() {
    // return course;
    // }

    /**
     * Semester order getter, this is used for the hidden column to order our table
     */
    public Integer getSemesterOrder() {
        switch (semester.get()) {
            case "Spring": return 1;
            case "Summer": return 2;
            case "Fall":   return 3;
            case "Winter": return 4;
            default:       return -1;
        }
    }

}

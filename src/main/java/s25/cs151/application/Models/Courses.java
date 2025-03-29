package s25.cs151.application.Models;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Courses {
    private final StringProperty courseName;
    private final StringProperty courseCode;
    private final StringProperty sectionNumber;

    public Courses(String courseCode, String courseName, String sectionNumber) {
        this.courseName = new SimpleStringProperty(courseName);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.sectionNumber = new SimpleStringProperty(sectionNumber);
    }

    // Getter and Setter for Course Name
    public String getCourseName() {
        return courseName.get();
    }
    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    // Getters and Setter for Course Code
    public String getCourseCode() {
        return courseCode.get();
    }
    public void setCourseCode(String courseCode) {
        this.courseCode.set(courseCode);
    }

    // Getter and Setter for Section Number
    public String getSectionNumber() {
        return sectionNumber.get();
    }
    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber.set(sectionNumber);
    }

    // Property Getters
    public StringProperty courseNameProperty() {
        return courseName;
    }
    public StringProperty courseCodeProperty() {
        return courseCode;
    }
    public StringProperty sectionNumberProperty() {
        return sectionNumber;
    }

}

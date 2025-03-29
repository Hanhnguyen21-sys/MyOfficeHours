package s25.cs151.application.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TimeSlots {
    private final StringProperty fromHour;
    private final StringProperty toHour;

    //constructors
    public TimeSlots(String fromHour, String toHour){
        this.fromHour = new SimpleStringProperty(fromHour);
        this.toHour = new SimpleStringProperty(toHour);
    }

    //getter & setters for from hours
    public String getFromHour() {
        return fromHour.get();
    }
    public void setFromHour(String fromHour) {
        this.fromHour.set(fromHour);
    }

    // getters and setters for to hour
    public String getToHour() {
        return toHour.get();
    }
    public void setToHour(String toHour) {
        this.toHour.set(toHour);
    }

    // property

    public StringProperty fromHourProperty() {
        return fromHour;
    }
    public StringProperty toHourProperty() {
        return toHour;
    }

    /**
     * From Time Slot order getter, this is used for the hidden column to order our table
     */
    public Integer getFromTimeSlotOrder() {
        String timeString = getFromHour();
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
     * To Time Slot order getter, this is used for the hidden column to order our table
     */
    public Integer getToTimeSlotOrder() {
        String timeString = getToHour();
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

}

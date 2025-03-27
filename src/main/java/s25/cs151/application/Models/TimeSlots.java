package s25.cs151.application.Models;

import java.time.LocalTime;

public class TimeSlots {
    private LocalTime fromHour;
    private LocalTime toHour;

    //constructors
    public TimeSlots(LocalTime fromHour, LocalTime toHour){
        this.fromHour= fromHour;
        this.toHour = toHour;
    }
    //getters and setters
    public LocalTime getFromHour() {
        return fromHour;
    }

    public void setFromHour(LocalTime fromHour) {
        this.fromHour = fromHour;
    }

    public LocalTime getToHour() {
        return toHour;
    }

    public void setToHour(LocalTime toHour) {
        this.toHour = toHour;
    }

}

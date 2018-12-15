package in.programmeraki.glaty.model;

import java.util.Calendar;

public class CalenderFeed {

    Calendar c;
    int area;

    public CalenderFeed(Calendar c, int area) {
        this.c = c;
        this.area = area;
    }

    public CalenderFeed(long millis, String area) {
        setCalendarByTimeInMillis(millis);
        this.area = Integer.parseInt(area);
    }

    public Calendar getC() {
        return c;
    }

    public void setC(Calendar c) {
        this.c = c;
    }

    public void setCalendarByTimeInMillis(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        this.c = c;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

}

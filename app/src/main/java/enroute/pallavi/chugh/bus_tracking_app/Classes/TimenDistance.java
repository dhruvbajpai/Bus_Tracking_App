package enroute.pallavi.chugh.bus_tracking_app.Classes;

/**
 * Created by Dhruv on 31-Aug-15.
 */
public class TimenDistance {
    String time;
    String distance;
    String t_value;
    String d_value;
    boolean priority_set= false;
    boolean smsSent = false;

    public TimenDistance(String time, String distance, String t_value, String d_values) {
        this.time = time;
        this.distance = distance;
        this.t_value = t_value;
        this.d_value = d_values;
    }

    public boolean isSmsSent() {
        return smsSent;
    }

    public void setSmsSent(boolean smsSent) {
        this.smsSent = smsSent;
    }

    public TimenDistance(String time, String distance) {
        this.time = time;
        this.distance = distance;
    }


    public boolean isPriority_set() {
        return priority_set;
    }

    public void setPriority_set(boolean priority_set) {
        this.priority_set = priority_set;
    }

    public String getD_value() {
        return d_value;
    }

    public void setD_value(String d_value) {
        this.d_value = d_value;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setT_value(String t_value) {
        this.t_value = t_value;
    }

    public void setD_values(String d_values) {
        this.d_value = d_values;
    }

    public String getTime() {
        return time;
    }

    public String getDistance() {
        return distance;
    }

    public String getT_value() {
        return t_value;
    }

    public String getD_values() {
        return d_value;
    }
}
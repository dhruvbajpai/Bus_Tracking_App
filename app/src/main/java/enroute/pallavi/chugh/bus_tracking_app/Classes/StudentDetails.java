package enroute.pallavi.chugh.bus_tracking_app.Classes;

/**
 * Created by Dhruv on 23-Oct-15.
 */
public class StudentDetails {

    public String name;
    public String class_;
    public int roll_no;
    public String phone_no;

    public StudentDetails(String name) {
        this.name = name;
    }

    public StudentDetails(String name, String class_, int roll_no, String phone_no) {
        this.name = name;
        this.class_ = class_;
        this.roll_no = roll_no;
        this.phone_no = phone_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClass_(String class_) {
        this.class_ = class_;
    }

    public void setRoll_no(int roll_no) {
        this.roll_no = roll_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getName() {
        return name;
    }

    public String getClass_() {
        return class_;
    }

    public int getRoll_no() {
        return roll_no;
    }

    public String getPhone_no() {
        return phone_no;
    }
}

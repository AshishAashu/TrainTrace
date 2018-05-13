package ashish.com.myapp1.List;

public class PassengerList {
    String id,booking_status,current_status;
    public PassengerList(String id,String bs,String cs){
        this.id = id;
        this.booking_status = bs;
        this.current_status = cs;
    }

    public String getId() {
        return id;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public String getCurrent_status() {
        return current_status;
    }
}

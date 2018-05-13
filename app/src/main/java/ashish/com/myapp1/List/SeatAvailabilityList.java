package ashish.com.myapp1.List;

public class SeatAvailabilityList {
    String id,date,status;

    public SeatAvailabilityList(String id, String date, String status) {
        this.id = id;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}

package ashish.com.myapp1.List;

public class LiveTrainStatusList {
    long id;
    String station,actarr,actdep,distance;

    public LiveTrainStatusList(long id, String station, String actarr, String actdep, String distance) {
        this.id = id;
        this.station = station;
        this.actarr = actarr;
        this.actdep = actdep;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public String getStation() {
        return station;
    }

    public String getActarr() {
        return actarr;
    }

    public String getActdep() {
        return actdep;
    }

    public String getDistance() {
        return distance;
    }
}

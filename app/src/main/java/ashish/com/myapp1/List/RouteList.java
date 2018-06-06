package ashish.com.myapp1.List;

public class RouteList {
    String station_name,station_code, sch_arr, sch_dep;
     int no, day;
    public RouteList(String sn,String sc,String sa,String sd,int day, int no){
        this.station_name = sn;
        this.station_code = sc;
        this.sch_arr = sa;
        this.sch_dep = sd;
        this.day = day;
        this.no = no;
    }

    public String getStation_name() {
        return this.station_name;
    }

    public String getStation_code() {
        return station_code;
    }

    public String getSch_arr() {
        return this.sch_arr;
    }

    public String getSch_dep() {
        return this.sch_dep;
    }

    public int getDay() {
        return this.day;
    }

    public int getNo() {
        return this.no;
    }
}

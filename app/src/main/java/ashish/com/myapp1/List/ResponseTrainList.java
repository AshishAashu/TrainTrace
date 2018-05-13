package ashish.com.myapp1.List;

import java.util.ArrayList;

public class ResponseTrainList {
    String traincode,trainname,from,to,dep,arrival,total;
    ArrayList days,classes;

    public ResponseTrainList(String traincode, String trainname, String from, String to, String dep, String arrival, String total, ArrayList days, ArrayList classes) {
        this.traincode = traincode;
        this.trainname = trainname;
        this.from = from;
        this.to = to;
        this.dep = dep;
        this.arrival = arrival;
        this.total = total;
        this.days = days;
        this.classes = classes;
    }

    public String getTraincode() {
        return traincode;
    }

    public String getTrainname() {
        return trainname;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDep() {
        return dep;
    }

    public String getArrival() {
        return arrival;
    }

    public String getTotal() {
        return total;
    }

    public ArrayList getDays() {
        return days;
    }

    public ArrayList getClasses() {
        return classes;
    }
}

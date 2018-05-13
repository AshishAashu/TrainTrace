package ashish.com.myapp1.List;

public class SourceDestinationList {
    String name,code;
    long id;
    public SourceDestinationList(long id,String name, String code) {
        this.id =id;
        this.name = name;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}

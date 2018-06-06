package ashish.com.myapp1.List;

public class SuggestionList {
    long id;
    String name;
    String number;

    public SuggestionList(long id,String name, String number) {
        this.name = name;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}

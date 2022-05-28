import java.nio.file.Path;

public class GroupInvoice {
    public String time; // timestamp wygenerowania faktury, System.currentTimeMillis()
    public String title; // tytuł/numer faktury

    public String path;
    Integer group;

    public GroupInvoice(){
        group = 1;
        time = String.valueOf(System.currentTimeMillis());
        title = "lol";
    }
}

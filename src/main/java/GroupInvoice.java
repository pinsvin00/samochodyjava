import java.nio.file.Path;

public class GroupInvoice {
    public String time;
    public String title;

    public String path;
    Integer group;

    public GroupInvoice(){
        group = 1;
        time = String.valueOf(System.currentTimeMillis());
        title = "Invoice";
    }
}
